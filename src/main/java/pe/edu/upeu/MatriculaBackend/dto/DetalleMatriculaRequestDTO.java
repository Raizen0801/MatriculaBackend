package pe.edu.upeu.MatriculaBackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleMatriculaRequestDTO {
    @NotNull(message = "El cursoId es obligatorio")
    private Long cursoId;
}