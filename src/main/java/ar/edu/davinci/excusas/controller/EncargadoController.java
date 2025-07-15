package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.service.EncargadoService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/encargados")
public class EncargadoController {

    private final EncargadoService encargadoService;

    @Autowired
    public EncargadoController(EncargadoService encargadoService) {
        this.encargadoService = encargadoService;
    }

    @GetMapping
    public ResponseEntity<List<EncargadoDTO>> obtenerTodosLosEncargados() {
        List<EncargadoDTO> encargados = encargadoService.obtenerTodosLosEncargados();
        return ResponseEntity.ok(encargados);
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EncargadoDTO> obtenerEncargadoPorLegajo(@PathVariable int legajo) {
        Optional<EncargadoDTO> encargado = encargadoService.obtenerEncargadoPorLegajo(legajo);
        return encargado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{legajo}/modo")
    public ResponseEntity<String> cambiarModoEncargado(
            @PathVariable int legajo,
            @RequestBody CambiarModoRequest request) {

        try {
            encargadoService.cambiarModoEncargado(legajo, request.getModo());
            return ResponseEntity.ok("Modo cambiado exitosamente a: " + request.getModo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<EncargadoDTO> crearEncargado(@Valid @RequestBody CrearEncargadoRequest request) {
        try {
            EncargadoDTO nuevoEncargado = encargadoService.crearEncargado(
                    request.getNombre(),
                    request.getEmail(),
                    request.getLegajo(),
                    request.getTipoEncargado(),
                    request.getModo()
            );

            return ResponseEntity.ok(nuevoEncargado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class CambiarModoRequest {
        private String modo;

        public CambiarModoRequest() {}

        public String getModo() { return modo; }
        public void setModo(String modo) { this.modo = modo; }
    }

    public static class CrearEncargadoRequest {
        @NotBlank(message = "El nombre no puede estar vacío")
        private String nombre;

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe tener un formato válido")
        private String email;

        @Positive(message = "El legajo debe ser un número positivo")
        private Integer legajo;

        @NotBlank(message = "El tipo de encargado no puede estar vacío")
        private String tipoEncargado;

        @NotBlank(message = "El modo no puede estar vacío")
        private String modo;

        public CrearEncargadoRequest() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Integer getLegajo() { return legajo; }
        public void setLegajo(Integer legajo) { this.legajo = legajo; }

        public String getTipoEncargado() { return tipoEncargado; }
        public void setTipoEncargado(String tipoEncargado) { this.tipoEncargado = tipoEncargado; }

        public String getModo() { return modo; }
        public void setModo(String modo) { this.modo = modo; }
    }
}
