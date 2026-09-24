package pe.edu.upeu.MatriculaBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.CarreraRepository;
import pe.edu.upeu.MatriculaBackend.repository.CursoRepository;
import pe.edu.upeu.MatriculaBackend.repository.EstudianteRepository;
import pe.edu.upeu.MatriculaBackend.service.service.CarreraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraServiceImpl implements CarreraService {

    private static final Logger log = LoggerFactory.getLogger(CarreraServiceImpl.class);

    private final CarreraRepository carreraRepository;
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;

    @Override
    @Transactional
    public CarreraResponseDTO create(CarreraRequestDTO request) {
        String nombreLimpio = request.getNombre().trim();
        if (carreraRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new ReglaNegocioException("Ya existe una carrera registrada con el nombre: " + nombreLimpio);
        }

        Carrera carrera = new Carrera();
        carrera.setNombre(nombreLimpio);
        carrera.setDescripcion(request.getDescripcion());
        carrera.setEstado(request.getEstado() != null ? request.getEstado() : true);

        Carrera guardada = carreraRepository.save(carrera);
        log.info("Carrera creada exitosamente con ID: {}", guardada.getId());
        return mapToResponse(guardada);
    }

    @Override
    @Transactional
    public CarreraResponseDTO update(Long id, CarreraRequestDTO request) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + id));

        String nombreLimpio = request.getNombre().trim();
        if (carreraRepository.existsByNombreIgnoreCaseAndIdNot(nombreLimpio, id)) {
            throw new ReglaNegocioException("Ya existe otra carrera registrada con el nombre: " + nombreLimpio);
        }

        carrera.setNombre(nombreLimpio);
        carrera.setDescripcion(request.getDescripcion());
        if (request.getEstado() != null) {
            carrera.setEstado(request.getEstado());
        }

        return mapToResponse(carreraRepository.save(carrera));
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO read(Long id) {
        return carreraRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!carreraRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Carrera no encontrada con ID: " + id);
        }
        if (cursoRepository.existsByCarreraId(id)) {
            throw new ReglaNegocioException("No se puede eliminar la carrera porque tiene cursos asociados");
        }
        if (estudianteRepository.existsByCarreraId(id)) {
            throw new ReglaNegocioException("No se puede eliminar la carrera porque tiene estudiantes asociados");
        }
        carreraRepository.deleteById(id);
        log.info("Carrera eliminada con ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> readAll() {
        return carreraRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CarreraResponseDTO mapToResponse(Carrera c) {
        return CarreraResponseDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .estado(c.getEstado())
                .fechaCreacion(c.getFechaCreacion())
                .fechaModificacion(c.getFechaModificacion())
                .build();
    }
}