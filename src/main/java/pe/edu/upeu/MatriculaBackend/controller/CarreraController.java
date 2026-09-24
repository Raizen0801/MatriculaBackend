package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.service.CarreraService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carreras")
@RequiredArgsConstructor
@Tag(name = "Carreras")
public class CarreraController {

    private final CarreraService carreraService;

    @PostMapping
    @Operation(summary = "Registrar una nueva carrera")
    public ResponseEntity<CarreraResponseDTO> create(@Valid @RequestBody CarreraRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carreraService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una carrera existente")
    public ResponseEntity<CarreraResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CarreraRequestDTO request) {
        return ResponseEntity.ok(carreraService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una carrera por su ID")
    public ResponseEntity<CarreraResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(carreraService.read(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las carreras")
    public ResponseEntity<List<CarreraResponseDTO>> readAll() {
        return ResponseEntity.ok(carreraService.readAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una carrera por su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carreraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}