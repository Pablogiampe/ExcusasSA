package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.service.EncargadoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<EncargadoDTO> crearEncargado(@Valid @RequestBody CrearEncargadoRequest request) {
        try {
            EncargadoDTO encargadoDTO = new EncargadoDTO(
                request.getLegajo(),
                request.getNombre(),
                request.getEmail(),
                request.getTipoEncargado(),
                request.getModo()
            );
            
            EncargadoDTO nuevoEncargado = encargadoService.crearEncargado(encargadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEncargado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/modo")
    public ResponseEntity<EncargadoDTO> cambiarModoEncargado(@Valid @RequestBody CambiarModoRequest request) {
        try {
            EncargadoDTO encargadoActualizado = encargadoService.cambiarModoEncargado(
                request.getLegajo(), 
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
    
    public static class CrearEncargadoRequest {
        private Integer legajo;
        private String nombre;
        private String email;
        private String tipoEncargado;
        private String modo;
        
        // Getters y Setters
        public Integer getLegajo() { return legajo; }
        public void setLegajo(Integer legajo) { this.legajo = legajo; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getTipoEncargado() { return tipoEncargado; }
        public void setTipoEncargado(String tipoEncargado) { this.tipoEncargado = tipoEncargado; }
        
        public String getModo() { return modo; }
        public void setModo(String modo) { this.modo = modo; }
    }
    
    public static class CambiarModoRequest {
        private Integer legajo;
        private String nuevoModo;
        
        // Getters y Setters
        public Integer getLegajo() { return legajo; }
        public void setLegajo(Integer legajo) { this.legajo = legajo; }
        
        public String getNuevoModo() { return nuevoModo; }
        public void setNuevoModo(String nuevoModo) { this.nuevoModo = nuevoModo; }
    }
}
