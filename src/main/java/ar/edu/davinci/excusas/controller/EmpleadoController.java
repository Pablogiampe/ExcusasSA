package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.LineaEncargados;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final List<Empleado> empleados = new ArrayList<>();
    private final LineaEncargados lineaEncargados;

    public EmpleadoController() {
        this.lineaEncargados = new LineaEncargados(new EmailSenderImpl());
        // Agregar algunos empleados de ejemplo
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
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody EmpleadoRequest request) {
        // Verificar que no exista un empleado con el mismo legajo
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

    @PostMapping("/{legajo}/excusas")
    public ResponseEntity<ExcusaResponse> generarExcusa(
            @PathVariable int legajo,
            @RequestBody ExcusaRequest request) {
        
        Optional<Empleado> empleadoOpt = empleados.stream()
                .filter(e -> e.getLegajo() == legajo)
                .findFirst();
        
        if (empleadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Empleado empleado = empleadoOpt.get();
            MotivoExcusa motivo = MotivoExcusa.valueOf(request.getMotivo().toUpperCase());
            
            Excusa excusa = empleado.generarYEnviarExcusa(motivo, lineaEncargados);
            
            ExcusaResponse response = new ExcusaResponse(
                    excusa.getClass().getSimpleName(),
                    excusa.getMotivo().toString(),
                    empleado.getNombre(),
                    empleado.getLegajo()
            );
            
            return ResponseEntity.ok(response);
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

    // DTOs
    public static class EmpleadoRequest {
        private String nombre;
        private String email;
        private int legajo;

        public EmpleadoRequest() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public int getLegajo() { return legajo; }
        public void setLegajo(int legajo) { this.legajo = legajo; }
    }

    public static class ExcusaRequest {
        private String motivo;

        public ExcusaRequest() {}

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
    }

    public static class ExcusaResponse {
        private String tipoExcusa;
        private String motivo;
        private String nombreEmpleado;
        private int legajoEmpleado;

        public ExcusaResponse(String tipoExcusa, String motivo, String nombreEmpleado, int legajoEmpleado) {
            this.tipoExcusa = tipoExcusa;
            this.motivo = motivo;
            this.nombreEmpleado = nombreEmpleado;
            this.legajoEmpleado = legajoEmpleado;
        }

        public String getTipoExcusa() { return tipoExcusa; }
        public String getMotivo() { return motivo; }
        public String getNombreEmpleado() { return nombreEmpleado; }
        public int getLegajoEmpleado() { return legajoEmpleado; }
    }
}
