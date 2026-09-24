package pe.edu.upeu.MatriculaBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaRequestDTO {

    @NotNull(message = "El estudianteId es obligatorio")
    private Long estudianteId;

    @NotBlank(message = "El periodo es obligatorio")
    @Pattern(regexp = "^\\d{4}-[12]$", message = "El periodo debe cumplir el patrón AAAA-1 o AAAA-2 (ej. 2026-2)")
    private String periodo;

    @NotEmpty(message = "Debe matricular al menos un curso")
    @Valid
    private List<DetalleMatriculaRequestDTO> detalles;
}