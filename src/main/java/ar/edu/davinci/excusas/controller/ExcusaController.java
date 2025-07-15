package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.service.ExcusaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/excusas")
public class ExcusaController {

    private final ExcusaService excusaService;

    @Autowired
    public ExcusaController(ExcusaService excusaService) {
        this.excusaService = excusaService;
    }

    @PostMapping
    public ResponseEntity<ExcusaDTO> registrarExcusa(@Valid @RequestBody RegistrarExcusaRequest request) {
        try {
            MotivoExcusa motivoEnum = MotivoExcusa.valueOf(request.getMotivo().toUpperCase());
            ExcusaDTO excusa = excusaService.registrarExcusa(request.getLegajo(), motivoEnum);
            return ResponseEntity.ok(excusa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<List<ExcusaDTO>> obtenerExcusasPorEmpleado(@PathVariable int legajo) {
        List<ExcusaDTO> excusas = excusaService.obtenerExcusasPorEmpleado(legajo);
        return ResponseEntity.ok(excusas);
    }

    @GetMapping
    public ResponseEntity<List<ExcusaDTO>> obtenerTodasLasExcusas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) String motivo) {

        List<ExcusaDTO> excusas = excusaService.obtenerTodasLasExcusas(fechaDesde, fechaHasta, motivo);
        return ResponseEntity.ok(excusas);
    }

    @GetMapping("/busqueda")
    public ResponseEntity<List<ExcusaDTO>> buscarExcusas(
            @RequestParam int legajo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        List<ExcusaDTO> excusas = excusaService.buscarExcusas(legajo, fechaDesde, fechaHasta);
        return ResponseEntity.ok(excusas);
    }

    @GetMapping("/rechazadas")
    public ResponseEntity<List<ExcusaDTO>> obtenerExcusasRechazadas() {
        List<ExcusaDTO> rechazadas = excusaService.obtenerExcusasRechazadas();
        return ResponseEntity.ok(rechazadas);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<EliminarExcusasResponse> eliminarExcusas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaLimite) {

        try {
            int cantidadEliminadas = excusaService.eliminarExcusas(fechaLimite);
            EliminarExcusasResponse response = new EliminarExcusasResponse(cantidadEliminadas);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class RegistrarExcusaRequest {
        @Positive(message = "El legajo debe ser un número positivo")
        private Integer legajo;

        @NotBlank(message = "El motivo no puede estar vacío")
        private String motivo;

        public RegistrarExcusaRequest() {}

        public Integer getLegajo() { return legajo; }
        public void setLegajo(Integer legajo) { this.legajo = legajo; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
    }

    public static class EliminarExcusasResponse {
        private int cantidadEliminadas;

        public EliminarExcusasResponse(int cantidadEliminadas) {
            this.cantidadEliminadas = cantidadEliminadas;
        }

        public int getCantidadEliminadas() { return cantidadEliminadas; }
    }
}
