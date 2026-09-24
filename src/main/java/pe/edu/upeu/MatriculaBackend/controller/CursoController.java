package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.CursoRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CursoResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.service.CursoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Cursos")
public class CursoController {

    private final CursoService cursoService;

    @PostMapping("/cursos")
    @Operation(summary = "Registrar un nuevo curso")
    public ResponseEntity<CursoResponseDTO> create(@Valid @RequestBody CursoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.create(request));
    }

    @PutMapping("/cursos/{id}")
    @Operation(summary = "Actualizar datos de un curso")
    public ResponseEntity<CursoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CursoRequestDTO request) {
        return ResponseEntity.ok(cursoService.update(id, request));
    }

    @GetMapping("/cursos/{id}")
    @Operation(summary = "Obtener un curso por ID")
    public ResponseEntity<CursoResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.read(id));
    }

    @GetMapping("/cursos")
    @Operation(summary = "Listar todos los cursos")
    public ResponseEntity<List<CursoResponseDTO>> readAll() {
        return ResponseEntity.ok(cursoService.readAll());
    }

    @DeleteMapping("/cursos/{id}")
    @Operation(summary = "Eliminar un curso por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/carreras/{id}/cursos")
    @Operation(summary = "Listar todos los cursos que pertenecen a una carrera")
    public ResponseEntity<List<CursoResponseDTO>> findByCarrera(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.findByCarreraId(id));
    }

    @GetMapping("/cursos/buscar")
    @Operation(summary = "Buscar cursos con filtros combinables y ordenamiento seguro")
    public ResponseEntity<List<CursoResponseDTO>> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long carreraId,
            @RequestParam(required = false) Integer ciclo,
            @RequestParam(required = false) Boolean conVacantes,
            @RequestParam(required = false, defaultValue = "nombre") String orden,
            @RequestParam(required = false, defaultValue = "asc") String dir) {
        return ResponseEntity.ok(cursoService.buscar(nombre, carreraId, ciclo, conVacantes, orden, dir));
    }
}