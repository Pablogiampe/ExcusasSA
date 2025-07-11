package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.LineaEncargados;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final List<Empleado> empleados = new ArrayList<>();
    private final LineaEncargados lineaEncargados;

    public EmpleadoController() {
        this.lineaEncargados = new LineaEncargados(new EmailSenderImpl());
        empleados.add(new Empleado("Juan Pérez", "juan.perez@empresa.com", 2001));
        empleados.add(new Empleado("María García", "maria.garcia@empresa.com", 2002));
        empleados.add(new Empleado("Carlos López", "carlos.lopez@empresa.com", 2003));
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerTodosLosEmpleados() {
        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorLegajo(@PathVariable int legajo) {
        Optional<Empleado> empleado = empleados.stream()
                .filter(e -> e.getLegajo() == legajo)
                .findFirst();

        return empleado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empleado> crearEmpleado(@Valid @RequestBody EmpleadoRequest request) {
        boolean existeLegajo = empleados.stream()
                .anyMatch(e -> e.getLegajo() == request.getLegajo());

        if (existeLegajo) {
            return ResponseEntity.badRequest().build();
        }

        Empleado nuevoEmpleado = new Empleado(
                request.getNombre(),
                request.getEmail(),
                request.getLegajo()
        );

        empleados.add(nuevoEmpleado);
        return ResponseEntity.ok(nuevoEmpleado);
    }

    @PostMapping("/{legajo}/excusas/{motivo}")
    public ResponseEntity<Excusa> generarExcusa(
            @PathVariable int legajo,
            @PathVariable String motivo) {

        Optional<Empleado> empleadoOpt = empleados.stream()
                .filter(e -> e.getLegajo() == legajo)
                .findFirst();

        if (empleadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (motivo == null || motivo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Empleado empleado = empleadoOpt.get();
            MotivoExcusa motivoEnum = MotivoExcusa.valueOf(motivo.toUpperCase());

            Excusa excusa = empleado.generarYEnviarExcusa(motivoEnum, lineaEncargados);

            return ResponseEntity.ok(excusa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{legajo}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable int legajo) {
        boolean eliminado = empleados.removeIf(e -> e.getLegajo() == legajo);

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
