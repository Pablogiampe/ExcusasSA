package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> obtenerTodosLosEmpleados() {
        try {
            List<EmpleadoDTO> empleados = empleadoService.obtenerTodosLosEmpleados();
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EmpleadoDTO> obtenerEmpleadoPorLegajo(@PathVariable Integer legajo) {
        try {
            Optional<EmpleadoDTO> empleado = empleadoService.obtenerEmpleadoPorLegajo(legajo);
            return empleado.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> crearEmpleado(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        try {
            EmpleadoDTO nuevoEmpleado = empleadoService.crearEmpleado(empleadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{legajo}")
    public ResponseEntity<EmpleadoDTO> actualizarEmpleado(@PathVariable Integer legajo,
                                                         @Valid @RequestBody EmpleadoDTO empleadoDTO) {
        try {
            EmpleadoDTO empleadoActualizado = empleadoService.actualizarEmpleado(legajo, empleadoDTO);
            return ResponseEntity.ok(empleadoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{legajo}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer legajo) {
        try {
            empleadoService.eliminarEmpleado(legajo);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EmpleadoDTO>> buscarEmpleadosPorNombre(@RequestParam String nombre) {
        try {
            List<EmpleadoDTO> empleados = empleadoService.buscarEmpleadosPorNombre(nombre);
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public static class EmpleadoRequest {
        private Integer legajo;
        private String nombre;
        private String email;

        public Integer getLegajo() { return legajo; }
        public void setLegajo(Integer legajo) { this.legajo = legajo; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
