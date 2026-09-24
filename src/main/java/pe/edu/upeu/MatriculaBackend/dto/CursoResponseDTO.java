package pe.edu.upeu.MatriculaBackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private Integer creditos;
    private Integer ciclo;
    private Integer vacantes;
    private Boolean estado;
    private Long carreraId;
    private String carreraNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}