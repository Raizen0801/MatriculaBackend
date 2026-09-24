package pe.edu.upeu.MatriculaBackend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleMatriculaResponseDTO {
    private Long id;
    private Long cursoId;
    private String cursoCodigo;
    private String cursoNombre;
    private Integer creditos;
    private BigDecimal costo;
}