package pe.edu.upeu.MatriculaBackend.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.CursoRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CursoResponseDTO;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;
import pe.edu.upeu.MatriculaBackend.entity.Curso;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.CarreraRepository;
import pe.edu.upeu.MatriculaBackend.repository.CursoRepository;
import pe.edu.upeu.MatriculaBackend.service.service.CursoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private static final Logger log = LoggerFactory.getLogger(CursoServiceImpl.class);
    private static final Set<String> ORDENES_PERMITIDOS = Set.of("nombre", "creditos", "vacantes");

    private final CursoRepository cursoRepository;
    private final CarreraRepository carreraRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CursoResponseDTO create(CursoRequestDTO request) {
        if (cursoRepository.existsByCodigoIgnoreCase(request.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un curso con el código: " + request.getCodigo());
        }

        Carrera carrera = carreraRepository.findById(request.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + request.getCarreraId()));

        Curso curso = new Curso();
        curso.setCodigo(request.getCodigo().toUpperCase().trim());
        curso.setNombre(request.getNombre().trim());
        curso.setCreditos(request.getCreditos());
        curso.setCiclo(request.getCiclo());
        curso.setVacantes(request.getVacantes());
        curso.setEstado(request.getEstado() != null ? request.getEstado() : true);
        curso.setCarrera(carrera);

        return mapToResponse(cursoRepository.save(curso));
    }

    @Override
    @Transactional
    public CursoResponseDTO update(Long id, CursoRequestDTO request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + id));

        if (cursoRepository.existsByCodigoIgnoreCaseAndIdNot(request.getCodigo(), id)) {
            throw new ReglaNegocioException("Ya existe otro curso con el código: " + request.getCodigo());
        }

        Carrera carrera = carreraRepository.findById(request.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + request.getCarreraId()));

        curso.setCodigo(request.getCodigo().toUpperCase().trim());
        curso.setNombre(request.getNombre().trim());
        curso.setCreditos(request.getCreditos());
        curso.setCiclo(request.getCiclo());
        curso.setVacantes(request.getVacantes());
        if (request.getEstado() != null) {
            curso.setEstado(request.getEstado());
        }
        curso.setCarrera(carrera);

        return mapToResponse(cursoRepository.save(curso));
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponseDTO read(Long id) {
        return cursoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
        log.info("Curso eliminado con ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> readAll() {
        return cursoRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> findByCarreraId(Long carreraId) {
        if (!carreraRepository.existsById(carreraId)) {
            throw new RecursoNoEncontradoException("Carrera no encontrada con ID: " + carreraId);
        }
        return cursoRepository.findByCarreraId(carreraId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> buscar(String nombre, Long carreraId, Integer ciclo, Boolean conVacantes, String orden, String dir) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Curso> cq = cb.createQuery(Curso.class);
        Root<Curso> root = cq.from(Curso.class);
        root.fetch("carrera", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase().trim() + "%"));
        }
        if (carreraId != null) {
            predicates.add(cb.equal(root.get("carrera").get("id"), carreraId));
        }
        if (ciclo != null) {
            predicates.add(cb.equal(root.get("ciclo"), ciclo));
        }
        if (conVacantes != null && conVacantes) {
            predicates.add(cb.gt(root.get("vacantes"), 0));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        String ordenFinal = (orden != null && ORDENES_PERMITIDOS.contains(orden.toLowerCase())) ? orden.toLowerCase() : "nombre";
        boolean esDesc = "desc".equalsIgnoreCase(dir);

        if (esDesc) {
            cq.orderBy(cb.desc(root.get(ordenFinal)));
        } else {
            cq.orderBy(cb.asc(root.get(ordenFinal)));
        }

        return entityManager.createQuery(cq).getResultList().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CursoResponseDTO mapToResponse(Curso c) {
        return CursoResponseDTO.builder()
                .id(c.getId())
                .codigo(c.getCodigo())
                .nombre(c.getNombre())
                .creditos(c.getCreditos())
                .ciclo(c.getCiclo())
                .vacantes(c.getVacantes())
                .estado(c.getEstado())
                .carreraId(c.getCarrera().getId())
                .carreraNombre(c.getCarrera().getNombre())
                .fechaCreacion(c.getFechaCreacion())
                .fechaModificacion(c.getFechaModificacion())
                .build();
    }
}