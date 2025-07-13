package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.excusas.Excusa;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/excusas")
public class ExcusaController {

    private final ExcusaService excusaService;

    @Autowired
    public ExcusaController(ExcusaService excusaService) {
        this.excusaService = excusaService;
    }

    @PostMapping
    public ResponseEntity<ExcusaResponse> registrarExcusa(@Valid @RequestBody RegistrarExcusaRequest request) {
        try {
            MotivoExcusa motivoEnum = MotivoExcusa.valueOf(request.getMotivo().toUpperCase());
            Excusa excusa = excusaService.registrarExcusa(request.getLegajo(), motivoEnum);

            ExcusaResponse response = new ExcusaResponse(
                    excusa.getMotivo().toString(),
                    excusa.getEmpleado().getLegajo(),
                    excusa.getEmpleado().getNombre(),
                    excusa.getEmpleado().getEmail(),
                    excusa.getClass().getSimpleName(),
                    LocalDate.now()
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<List<ExcusaResponse>> obtenerExcusasPorEmpleado(@PathVariable int legajo) {
        List<Excusa> excusas = excusaService.obtenerExcusasPorEmpleado(legajo);
        List<ExcusaResponse> response = excusas.stream()
                .map(this::convertirAExcusaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ExcusaResponse>> obtenerTodasLasExcusas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) String motivo) {

        List<Excusa> excusas = excusaService.obtenerTodasLasExcusas(fechaDesde, fechaHasta, motivo);
        List<ExcusaResponse> response = excusas.stream()
                .map(this::convertirAExcusaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/busqueda")
    public ResponseEntity<List<ExcusaResponse>> buscarExcusas(
            @RequestParam int legajo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        List<Excusa> excusas = excusaService.buscarExcusas(legajo, fechaDesde, fechaHasta);
        List<ExcusaResponse> response = excusas.stream()
                .map(this::convertirAExcusaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rechazadas")
    public ResponseEntity<List<ExcusaResponse>> obtenerExcusasRechazadas() {
        List<Excusa> rechazadas = excusaService.obtenerExcusasRechazadas();
        List<ExcusaResponse> response = rechazadas.stream()
                .map(this::convertirAExcusaResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
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

    private ExcusaResponse convertirAExcusaResponse(Excusa excusa) {
        return new ExcusaResponse(
                excusa.getMotivo().toString(),
                excusa.getEmpleado().getLegajo(),
                excusa.getEmpleado().getNombre(),
                excusa.getEmpleado().getEmail(),
                excusa.getClass().getSimpleName(),
                LocalDate.now()
        );
    }

    public static class RegistrarExcusaRequest {
        @Positive(message = "El legajo debe ser un número positivo")
        private int legajo;

        @NotBlank(message = "El motivo no puede estar vacío")
        private String motivo;

        public RegistrarExcusaRequest() {}

        public int getLegajo() { return legajo; }
        public void setLegajo(int legajo) { this.legajo = legajo; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
    }

    public static class ExcusaResponse {
        private String motivo;
        private int legajoEmpleado;
        private String nombreEmpleado;
        private String emailEmpleado;
        private String tipoExcusa;
        private LocalDate fecha;

        public ExcusaResponse(String motivo, int legajoEmpleado, String nombreEmpleado,
                              String emailEmpleado, String tipoExcusa, LocalDate fecha) {
            this.motivo = motivo;
            this.legajoEmpleado = legajoEmpleado;
            this.nombreEmpleado = nombreEmpleado;
            this.emailEmpleado = emailEmpleado;
            this.tipoExcusa = tipoExcusa;
            this.fecha = fecha;
        }

        public String getMotivo() { return motivo; }
        public int getLegajoEmpleado() { return legajoEmpleado; }
        public String getNombreEmpleado() { return nombreEmpleado; }
        public String getEmailEmpleado() { return emailEmpleado; }
        public String getTipoExcusa() { return tipoExcusa; }
        public LocalDate getFecha() { return fecha; }
    }

    public static class EliminarExcusasResponse {
        private int cantidadEliminadas;

        public EliminarExcusasResponse(int cantidadEliminadas) {
            this.cantidadEliminadas = cantidadEliminadas;
        }

        public int getCantidadEliminadas() { return cantidadEliminadas; }
    }
}