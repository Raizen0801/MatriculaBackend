package pe.edu.upeu.MatriculaBackend.service.service;

import pe.edu.upeu.MatriculaBackend.dto.EstudianteRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.generic.CrudService;

public interface EstudianteService extends CrudService<EstudianteRequestDTO, EstudianteResponseDTO, Long> {
}