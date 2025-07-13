package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.empleados.encargados.EncargadoBase;
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
    public ResponseEntity<List<EncargadoResponse>> obtenerTodosLosEncargados() {
        List<EncargadoBase> encargados = encargadoService.obtenerTodosLosEncargados();
        List<EncargadoResponse> response = encargados.stream()
                .map(e -> new EncargadoResponse(
                        e.getLegajo(),
                        e.getNombre(),
                        e.getEmail(),
                        e.getClass().getSimpleName()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EncargadoResponse> obtenerEncargadoPorLegajo(@PathVariable int legajo) {
        Optional<EncargadoBase> encargado = encargadoService.obtenerEncargadoPorLegajo(legajo);

        if (encargado.isPresent()) {
            EncargadoBase e = encargado.get();
            EncargadoResponse response = new EncargadoResponse(
                    e.getLegajo(),
                    e.getNombre(),
                    e.getEmail(),
                    e.getClass().getSimpleName()
            );
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
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
    public ResponseEntity<EncargadoResponse> crearEncargado(@Valid @RequestBody CrearEncargadoRequest request) {
        try {
            EncargadoBase nuevoEncargado = encargadoService.crearEncargado(
                    request.getNombre(),
                    request.getEmail(),
                    request.getLegajo(),
                    request.getTipoEncargado(),
                    request.getModo()
            );

            EncargadoResponse response = new EncargadoResponse(
                    nuevoEncargado.getLegajo(),
                    nuevoEncargado.getNombre(),
                    nuevoEncargado.getEmail(),
                    nuevoEncargado.getClass().getSimpleName()
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class EncargadoResponse {
        private int legajo;
        private String nombre;
        private String email;
        private String tipoEncargado;

        public EncargadoResponse(int legajo, String nombre, String email, String tipoEncargado) {
            this.legajo = legajo;
            this.nombre = nombre;
            this.email = email;
            this.tipoEncargado = tipoEncargado;
        }

        public int getLegajo() { return legajo; }
        public String getNombre() { return nombre; }
        public String getEmail() { return email; }
        public String getTipoEncargado() { return tipoEncargado; }
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
        private int legajo;

        @NotBlank(message = "El tipo de encargado no puede estar vacío")
        private String tipoEncargado;

        @NotBlank(message = "El modo no puede estar vacío")
        private String modo;

        public CrearEncargadoRequest() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public int getLegajo() { return legajo; }
        public void setLegajo(int legajo) { this.legajo = legajo; }

        public String getTipoEncargado() { return tipoEncargado; }
        public void setTipoEncargado(String tipoEncargado) { this.tipoEncargado = tipoEncargado; }

        public String getModo() { return modo; }
        public void setModo(String modo) { this.modo = modo; }
    }
}