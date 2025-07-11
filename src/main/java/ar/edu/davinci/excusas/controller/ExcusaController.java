package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.LineaEncargados;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/excusas")
public class ExcusaController {

    private final List<Empleado> empleados = new ArrayList<>();
    private final List<Excusa> excusas = new ArrayList<>();
    private final LineaEncargados lineaEncargados;

    public ExcusaController() {
        this.lineaEncargados = new LineaEncargados(new EmailSenderImpl());
        empleados.add(new Empleado("Juan Pérez", "juan.perez@empresa.com", 2001));
        empleados.add(new Empleado("María García", "maria.garcia@empresa.com", 2002));
        empleados.add(new Empleado("Carlos López", "carlos.lopez@empresa.com", 2003));
    }

    @PostMapping
    public ResponseEntity<ExcusaResponse> registrarExcusa(@Valid @RequestBody RegistrarExcusaRequest request) {
        Optional<Empleado> empleadoOpt = empleados.stream()
                .filter(e -> e.getLegajo() == request.getLegajo())
                .findFirst();

        if (empleadoOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Empleado empleado = empleadoOpt.get();
            MotivoExcusa motivoEnum = MotivoExcusa.valueOf(request.getMotivo().toUpperCase());

            Excusa excusa = empleado.generarYEnviarExcusa(motivoEnum, lineaEncargados);
            excusas.add(excusa);

            ExcusaResponse response = new ExcusaResponse(
                    excusa.getMotivo().toString(),
                    empleado.getLegajo(),
                    empleado.getNombre(),
                    empleado.getEmail(),
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
        List<ExcusaResponse> excusasEmpleado = excusas.stream()
                .filter(e -> e.getEmpleado().getLegajo() == legajo)
                .map(this::convertirAExcusaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(excusasEmpleado);
    }

    @GetMapping
    public ResponseEntity<List<ExcusaResponse>> obtenerTodasLasExcusas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) String motivo) {

        List<ExcusaResponse> resultado = excusas.stream()
                .filter(e -> fechaDesde == null || !LocalDate.now().isBefore(fechaDesde))
                .filter(e -> fechaHasta == null || !LocalDate.now().isAfter(fechaHasta))
                .filter(e -> motivo == null || e.getMotivo().toString().equalsIgnoreCase(motivo))
                .map(this::convertirAExcusaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/busqueda")
    public ResponseEntity<List<ExcusaResponse>> buscarExcusas(
            @RequestParam int legajo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        List<ExcusaResponse> resultado = excusas.stream()
                .filter(e -> e.getEmpleado().getLegajo() == legajo)
                .filter(e -> fechaDesde == null || !LocalDate.now().isBefore(fechaDesde))
                .filter(e -> fechaHasta == null || !LocalDate.now().isAfter(fechaHasta))
                .map(this::convertirAExcusaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/rechazadas")
    public ResponseEntity<List<ExcusaResponse>> obtenerExcusasRechazadas() {
        List<ExcusaResponse> rechazadas = new ArrayList<>();
        return ResponseEntity.ok(rechazadas);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<EliminarExcusasResponse> eliminarExcusas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaLimite) {

        if (fechaLimite == null) {
            return ResponseEntity.badRequest().build();
        }

        int cantidadEliminadas = (int) excusas.stream()
                .filter(e -> LocalDate.now().isBefore(fechaLimite))
                .count();

        excusas.removeIf(e -> LocalDate.now().isBefore(fechaLimite));

        EliminarExcusasResponse response = new EliminarExcusasResponse(cantidadEliminadas);
        return ResponseEntity.ok(response);
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