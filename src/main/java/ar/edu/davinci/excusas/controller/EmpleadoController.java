package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerTodosLosEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerTodosLosEmpleados();
        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorLegajo(@PathVariable int legajo) {
        Optional<Empleado> empleado = empleadoService.obtenerEmpleadoPorLegajo(legajo);
        return empleado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empleado> crearEmpleado(@Valid @RequestBody EmpleadoRequest request) {
        try {
            Empleado nuevoEmpleado = empleadoService.crearEmpleado(
                    request.getNombre(),
                    request.getEmail(),
                    request.getLegajo()
            );
            return ResponseEntity.ok(nuevoEmpleado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{legajo}/excusas/{motivo}")
    public ResponseEntity<Excusa> generarExcusa(
            @PathVariable int legajo,
            @PathVariable String motivo) {

        if (motivo == null || motivo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            MotivoExcusa motivoEnum = MotivoExcusa.valueOf(motivo.toUpperCase());
            Excusa excusa = empleadoService.generarExcusa(legajo, motivoEnum);
            return ResponseEntity.ok(excusa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{legajo}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable int legajo) {
        boolean eliminado = empleadoService.eliminarEmpleado(legajo);

        if (eliminado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public static class EmpleadoRequest {
        @NotBlank(message = "El nombre no puede estar vacío")
        private String nombre;

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe tener un formato válido")
        private String email;

        @Positive(message = "El legajo debe ser un número positivo")
        private int legajo;

        public EmpleadoRequest() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public int getLegajo() { return legajo; }
        public void setLegajo(int legajo) { this.legajo = legajo; }
    }
}