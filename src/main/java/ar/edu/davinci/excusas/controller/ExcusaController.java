package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.service.ExcusaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/excusas")
@CrossOrigin(origins = "*")
public class ExcusaController {
    
    @Autowired
    private ExcusaService excusaService;
    
    @GetMapping
    public ResponseEntity<List<ExcusaDTO>> obtenerTodasLasExcusas() {
        try {
            List<ExcusaDTO> excusas = excusaService.obtenerTodasLasExcusas();
            return ResponseEntity.ok(excusas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{legajo}")
    public ResponseEntity<List<ExcusaDTO>> obtenerExcusasPorEmpleado(@PathVariable Integer legajo) {
        try {
            List<ExcusaDTO> excusas = excusaService.obtenerExcusasPorEmpleado(legajo);
            return ResponseEntity.ok(excusas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/rechazadas")
    public ResponseEntity<List<ExcusaDTO>> obtenerExcusasRechazadas() {
        try {
            List<ExcusaDTO> excusas = excusaService.obtenerExcusasRechazadas();
            return ResponseEntity.ok(excusas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/busqueda")
    public ResponseEntity<List<ExcusaDTO>> buscarExcusas(
            @RequestParam(required = false) Integer legajo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta) {
        try {
            List<ExcusaDTO> excusas = excusaService.buscarExcusas(legajo, fechaDesde, fechaHasta);
            return ResponseEntity.ok(excusas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ExcusaDTO> registrarExcusa(@Valid @RequestBody RegistrarExcusaRequest request) {
        try {
            ExcusaDTO excusa = excusaService.registrarExcusa(request.getLegajoEmpleado(), request.getMotivo());
            return ResponseEntity.status(HttpStatus.CREATED).body(excusa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/eliminar")
    public ResponseEntity<EliminarExcusasResponse> eliminarExcusasAnterioresA(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaLimite) {
        try {
            if (fechaLimite == null) {
                return ResponseEntity.badRequest().build();
            }
            
            int excusasEliminadas = excusaService.eliminarExcusasAnterioresA(fechaLimite);
            EliminarExcusasResponse response = new EliminarExcusasResponse(excusasEliminadas);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    public static class RegistrarExcusaRequest {
        private Integer legajoEmpleado;
        private String motivo;
        
        // Getters y Setters
        public Integer getLegajoEmpleado() { return legajoEmpleado; }
        public void setLegajoEmpleado(Integer legajoEmpleado) { this.legajoEmpleado = legajoEmpleado; }
        
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
    }
    
    public static class EliminarExcusasResponse {
        private int excusasEliminadas;
        
        public EliminarExcusasResponse(int excusasEliminadas) {
            this.excusasEliminadas = excusasEliminadas;
        }
        
        public int getExcusasEliminadas() { return excusasEliminadas; }
        public void setExcusasEliminadas(int excusasEliminadas) { this.excusasEliminadas = excusasEliminadas; }
    }
}
