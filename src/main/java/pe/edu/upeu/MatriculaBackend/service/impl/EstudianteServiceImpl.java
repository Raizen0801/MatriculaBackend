package pe.edu.upeu.MatriculaBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.EstudianteRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;
import pe.edu.upeu.MatriculaBackend.entity.Estudiante;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.CarreraRepository;
import pe.edu.upeu.MatriculaBackend.repository.EstudianteRepository;
import pe.edu.upeu.MatriculaBackend.service.service.EstudianteService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private static final Logger log = LoggerFactory.getLogger(EstudianteServiceImpl.class);

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;

    @Override
    @Transactional
    public EstudianteResponseDTO create(EstudianteRequestDTO request) {
        if (estudianteRepository.existsByCodigo(request.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un estudiante con el código: " + request.getCodigo());
        }
        if (estudianteRepository.existsByDni(request.getDni())) {
            throw new ReglaNegocioException("Ya existe un estudiante con el DNI: " + request.getDni());
        }

        Carrera carrera = carreraRepository.findById(request.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + request.getCarreraId()));

        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo(request.getCodigo().trim());
        estudiante.setDni(request.getDni().trim());
        estudiante.setNombres(request.getNombres().trim());
        estudiante.setApellidos(request.getApellidos().trim());
        estudiante.setEmail(request.getEmail().trim());
        estudiante.setEstado(request.getEstado() != null ? request.getEstado() : true);
        estudiante.setCarrera(carrera);

        return mapToResponse(estudianteRepository.save(estudiante));
    }

    @Override
    @Transactional
    public EstudianteResponseDTO update(Long id, EstudianteRequestDTO request) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));

        if (estudianteRepository.existsByCodigoAndIdNot(request.getCodigo(), id)) {
            throw new ReglaNegocioException("Ya existe otro estudiante con el código: " + request.getCodigo());
        }
        if (estudianteRepository.existsByDniAndIdNot(request.getDni(), id)) {
            throw new ReglaNegocioException("Ya existe otro estudiante con el DNI: " + request.getDni());
        }

        Carrera carrera = carreraRepository.findById(request.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + request.getCarreraId()));

        estudiante.setCodigo(request.getCodigo().trim());
        estudiante.setDni(request.getDni().trim());
        estudiante.setNombres(request.getNombres().trim());
        estudiante.setApellidos(request.getApellidos().trim());
        estudiante.setEmail(request.getEmail().trim());
        if (request.getEstado() != null) {
            estudiante.setEstado(request.getEstado());
        }
        estudiante.setCarrera(carrera);

        return mapToResponse(estudianteRepository.save(estudiante));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponseDTO read(Long id) {
        return estudianteRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id);
        }
        estudianteRepository.deleteById(id);
        log.info("Estudiante eliminado con ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> readAll() {
        return estudianteRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EstudianteResponseDTO mapToResponse(Estudiante e) {
        return EstudianteResponseDTO.builder()
                .id(e.getId())
                .codigo(e.getCodigo())
                .dni(e.getDni())
                .nombres(e.getNombres())
                .apellidos(e.getApellidos())
                .email(e.getEmail())
                .estado(e.getEstado())
                .carreraId(e.getCarrera().getId())
                .carreraNombre(e.getCarrera().getNombre())
                .fechaCreacion(e.getFechaCreacion())
                .fechaModificacion(e.getFechaModificacion())
                .build();
    }
}