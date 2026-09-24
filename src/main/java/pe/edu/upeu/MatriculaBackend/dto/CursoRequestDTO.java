package pe.edu.upeu.MatriculaBackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoRequestDTO {

    @NotBlank(message = "El código del curso es obligatorio")
    @Pattern(regexp = "^[A-Z]{2}\\d{3}$", message = "El código debe cumplir el patrón exacto (ej. IS401)")
    private String codigo;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos mínimos son 1")
    @Max(value = 6, message = "Los créditos máximos son 6")
    private Integer creditos;

    @NotNull(message = "El ciclo es obligatorio")
    @Min(value = 1, message = "El ciclo mínimo es 1")
    @Max(value = 10, message = "El ciclo máximo es 10")
    private Integer ciclo;

    @NotNull(message = "Las vacantes son obligatorias")
    @Min(value = 0, message = "Las vacantes no pueden ser negativas")
    private Integer vacantes;

    private Boolean estado = true;

    @NotNull(message = "El carreraId es obligatorio")
    private Long carreraId;
}