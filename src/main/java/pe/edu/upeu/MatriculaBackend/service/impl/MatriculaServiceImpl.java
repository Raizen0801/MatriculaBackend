package pe.edu.upeu.MatriculaBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.*;
import pe.edu.upeu.MatriculaBackend.entity.*;
import pe.edu.upeu.MatriculaBackend.enums.EstadoMatricula;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.*;
import pe.edu.upeu.MatriculaBackend.service.service.MatriculaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl implements MatriculaService {

    private static final Logger log = LoggerFactory.getLogger(MatriculaServiceImpl.class);

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    @Value("${matricula.costo-credito:120.00}")
    private BigDecimal costoPorCredito;

    @Override
    @Transactional
    public MatriculaResponseDTO matricular(MatriculaRequestDTO request) {
        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + request.getEstudianteId()));

        if (Boolean.FALSE.equals(estudiante.getEstado())) {
            log.warn("Intento de matricula con estudiante inactivo ID: {}", estudiante.getId());
            throw new ReglaNegocioException("El estudiante se encuentra inactivo");
        }

        if (matriculaRepository.existsByEstudianteIdAndPeriodoAndEstado(estudiante.getId(), request.getPeriodo(), EstadoMatricula.REGISTRADA)) {
            log.warn("Estudiante {} ya cuenta con matricula activa en periodo {}", estudiante.getId(), request.getPeriodo());
            throw new ReglaNegocioException("El estudiante ya posee una matrícula REGISTRADA en el periodo " + request.getPeriodo());
        }

        Matricula matricula = new Matricula();
        matricula.setEstudiante(estudiante);
        matricula.setPeriodo(request.getPeriodo());
        matricula.setFecha(LocalDateTime.now());
        matricula.setEstado(EstadoMatricula.REGISTRADA);

        int totalCreditos = 0;
        BigDecimal montoTotal = BigDecimal.ZERO;
        Set<Long> cursosEnPeticion = new HashSet<>();

        for (DetalleMatriculaRequestDTO detalleDTO : request.getDetalles()) {
            if (!cursosEnPeticion.add(detalleDTO.getCursoId())) {
                throw new ReglaNegocioException("No se puede registrar cursos repetidos en la misma matrícula");
            }

            Curso curso = cursoRepository.findById(detalleDTO.getCursoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + detalleDTO.getCursoId()));

            if (Boolean.FALSE.equals(curso.getEstado())) {
                throw new ReglaNegocioException("El curso '" + curso.getNombre() + "' no se encuentra activo");
            }
            if (!curso.getCarrera().getId().equals(estudiante.getCarrera().getId())) {
                log.warn("Curso {} pertenece a carrera {}, no coincide con carrera {}", curso.getId(), curso.getCarrera().getId(), estudiante.getCarrera().getId());
                throw new ReglaNegocioException("El curso '" + curso.getNombre() + "' no pertenece a la carrera del estudiante");
            }

            if (curso.getVacantes() <= 0) {
                log.warn("Curso {} sin vacantes disponibles", curso.getId());
                throw new ReglaNegocioException("El curso '" + curso.getNombre() + "' no tiene vacantes disponibles");
            }

            curso.setVacantes(curso.getVacantes() - 1);

            BigDecimal costoCurso = costoPorCredito.multiply(BigDecimal.valueOf(curso.getCreditos())).setScale(2, RoundingMode.HALF_UP);

            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setCurso(curso);
            detalle.setCreditos(curso.getCreditos());
            detalle.setCosto(costoCurso);

            matricula.agregarDetalle(detalle);

            totalCreditos += curso.getCreditos();
            montoTotal = montoTotal.add(costoCurso);
        }

        if (totalCreditos > 20) {
            log.warn("Matricula excede el limite de creditos: {}", totalCreditos);
            throw new ReglaNegocioException("La matrícula no puede superar 20 créditos (Créditos solicitados: " + totalCreditos + ")");
        }

        matricula.setTotalCreditos(totalCreditos);
        matricula.setMontoTotal(montoTotal);

        Matricula guardada = matriculaRepository.save(matricula);
        log.info("Matricula registrada exitosamente con ID: {} para estudiante: {}", guardada.getId(), estudiante.getCodigo());
        return mapToResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public MatriculaResponseDTO findById(Long id) {
        return matriculaRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matrícula no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> findAll() {
        return matriculaRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public MatriculaResponseDTO anular(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matrícula no encontrada con ID: " + id));

        if (matricula.getEstado() == EstadoMatricula.ANULADA) {
            throw new ReglaNegocioException("La matrícula ya se encuentra ANULADA");
        }

        for (DetalleMatricula detalle : matricula.getDetalles()) {
            Curso curso = detalle.getCurso();
            curso.setVacantes(curso.getVacantes() + 1);
        }

        matricula.setEstado(EstadoMatricula.ANULADA);
        log.info("Matricula ID: {} anulada con éxito y vacantes restituidas", id);
        return mapToResponse(matriculaRepository.save(matricula));
    }

    private MatriculaResponseDTO mapToResponse(Matricula m) {
        List<DetalleMatriculaResponseDTO> detallesDTO = m.getDetalles().stream()
                .map(d -> DetalleMatriculaResponseDTO.builder()
                        .id(d.getId())
                        .cursoId(d.getCurso().getId())
                        .cursoCodigo(d.getCurso().getCodigo())
                        .cursoNombre(d.getCurso().getNombre())
                        .creditos(d.getCreditos())
                        .costo(d.getCosto())
                        .build())
                .toList();

        return MatriculaResponseDTO.builder()
                .id(m.getId())
                .fecha(m.getFecha())
                .periodo(m.getPeriodo())
                .estudianteId(m.getEstudiante().getId())
                .estudianteNombreCompleto(m.getEstudiante().getNombres() + " " + m.getEstudiante().getApellidos())
                .totalCreditos(m.getTotalCreditos())
                .montoTotal(m.getMontoTotal())
                .estado(m.getEstado())
                .detalles(detallesDTO)
                .fechaCreacion(m.getFechaCreacion())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> obtenerHistorialEstudiante(Long estudianteId, String periodo) {
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + estudianteId);
        }
        return matriculaRepository.findHistorialByEstudiante(estudianteId, periodo)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MatriculaResponseDTO retirarCurso(Long matriculaId, Long cursoId) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matrícula no encontrada con ID: " + matriculaId));

        if (matricula.getEstado() != EstadoMatricula.REGISTRADA) {
            throw new ReglaNegocioException("Solo se pueden retirar cursos de matrículas en estado REGISTRADA");
        }

        if (matricula.getDetalles().size() <= 1) {
            throw new ReglaNegocioException("No se permite dejar la matrícula sin cursos");
        }

        DetalleMatricula detalleAEliminar = matricula.getDetalles().stream()
                .filter(d -> d.getCurso().getId().equals(cursoId))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("El curso no pertenece a esta matrícula"));

        Curso curso = detalleAEliminar.getCurso();
        curso.setVacantes(curso.getVacantes() + 1);
        cursoRepository.save(curso);

        matricula.getDetalles().remove(detalleAEliminar);

        int totalCreditos = matricula.getDetalles().stream()
                .mapToInt(d -> d.getCurso().getCreditos())
                .sum();
        BigDecimal costoTotal = costoPorCredito.multiply(BigDecimal.valueOf(totalCreditos)).setScale(2, RoundingMode.HALF_UP);

        matricula.setTotalCreditos(totalCreditos);
        matricula.setMontoTotal(costoTotal);

        return mapToResponse(matriculaRepository.save(matricula));
    }
}