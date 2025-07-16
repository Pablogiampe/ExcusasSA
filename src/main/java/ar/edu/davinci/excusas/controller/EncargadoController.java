package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.service.EncargadoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/encargados")
@CrossOrigin(origins = "*")
public class EncargadoController {

    @Autowired
    private EncargadoService encargadoService;

    @GetMapping
    public ResponseEntity<List<EncargadoDTO>> obtenerTodosLosEncargados() {
        try {
            List<EncargadoDTO> encargados = encargadoService.obtenerTodosLosEncargados();
            return ResponseEntity.ok(encargados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EncargadoDTO> obtenerEncargadoPorLegajo(@PathVariable Integer legajo) {
        try {
            Optional<EncargadoDTO> encargado = encargadoService.obtenerEncargadoPorLegajo(legajo);
            return encargado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<EncargadoDTO> crearEncargado(@Valid @RequestBody EncargadoDTO encargadoDTO) {
        try {
            EncargadoDTO nuevoEncargado = encargadoService.crearEncargado(encargadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEncargado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{legajo}/modo")
    public ResponseEntity<EncargadoDTO> cambiarModoEncargado(
            @PathVariable Integer legajo,
            @Valid @RequestBody CambiarModoRequest request) {
        try {
            EncargadoDTO encargadoActualizado = encargadoService.cambiarModoEncargado(
                    legajo,
                    request.getNuevoModo()
            );
            return ResponseEntity.ok(encargadoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{legajo}")
    public ResponseEntity<Void> eliminarEncargado(@PathVariable Integer legajo) {
        try {
            encargadoService.eliminarEncargado(legajo);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public static class CambiarModoRequest {
        @NotBlank(message = "El nuevo modo no puede estar vacío")
        private String nuevoModo;

        public String getNuevoModo() { return nuevoModo; }
        public void setNuevoModo(String nuevoModo) { this.nuevoModo = nuevoModo; }
    }
}
