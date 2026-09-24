package pe.edu.upeu.MatriculaBackend.dto;

import lombok.*;
import pe.edu.upeu.MatriculaBackend.enums.EstadoMatricula;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculaResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private String periodo;
    private Long estudianteId;
    private String estudianteNombreCompleto;
    private Integer totalCreditos;
    private BigDecimal montoTotal;
    private EstadoMatricula estado;
    private List<DetalleMatriculaResponseDTO> detalles;
    private LocalDateTime fechaCreacion;
}