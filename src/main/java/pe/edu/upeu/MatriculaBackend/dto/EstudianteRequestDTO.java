package pe.edu.upeu.MatriculaBackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteRequestDTO {

    @NotBlank(message = "El código del estudiante es obligatorio")
    @Pattern(regexp = "^\\d{9}$", message = "El código de estudiante debe tener exactamente 9 dígitos")
    private String codigo;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    private String email;

    private Boolean estado = true;

    @NotNull(message = "El carreraId es obligatorio")
    private Long carreraId;
}