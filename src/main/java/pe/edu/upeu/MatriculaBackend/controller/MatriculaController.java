package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.MatriculaRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.service.MatriculaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
@Tag(name = "Matrículas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    @Operation(summary = "Registrar una nueva matrícula académica con sus detalles")
    public ResponseEntity<MatriculaResponseDTO> matricular(@Valid @RequestBody MatriculaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.matricular(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una matrícula por ID con sus cursos asociados")
    public ResponseEntity<MatriculaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las matrículas")
    public ResponseEntity<List<MatriculaResponseDTO>> findAll() {
        return ResponseEntity.ok(matriculaService.findAll());
    }

    @PatchMapping("/{id}/anular")
    @Operation(summary = "Anular una matrícula y devolver las vacantes a sus cursos")
    public ResponseEntity<MatriculaResponseDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.anular(id));
    }
    @DeleteMapping("/{id}/cursos/{cursoId}")
    public ResponseEntity<MatriculaResponseDTO> retirarCurso(
            @PathVariable Long id,
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(matriculaService.retirarCurso(id, cursoId));
    }
}