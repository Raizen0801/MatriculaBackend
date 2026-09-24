package pe.edu.upeu.MatriculaBackend.service.service;

import pe.edu.upeu.MatriculaBackend.dto.MatriculaRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.MatriculaResponseDTO;

import java.util.List;

public interface MatriculaService {
    MatriculaResponseDTO matricular(MatriculaRequestDTO request);
    MatriculaResponseDTO findById(Long id);
    List<MatriculaResponseDTO> findAll();
    MatriculaResponseDTO anular(Long id);
}