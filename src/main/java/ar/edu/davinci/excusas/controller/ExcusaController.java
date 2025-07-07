package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/excusas")
public class ExcusaController {

    @GetMapping("/motivos")
    public ResponseEntity<List<MotivoExcusaResponse>> obtenerMotivosDisponibles() {
        List<MotivoExcusaResponse> motivos = Arrays.stream(MotivoExcusa.values())
                .map(m -> new MotivoExcusaResponse(
                        m.name(),
                        obtenerDescripcionMotivo(m),
                        obtenerTipoExcusa(m)
                ))
                .toList();
        
        return ResponseEntity.ok(motivos);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> obtenerTiposDeExcusa() {
        List<String> tipos = List.of(
                "ExcusaTrivial",
                "ExcusaModerada", 
                "ExcusaPerdidaSuministro",
                "ExcusaCuidadoFamiliar",
                "ExcusaCompleja",
                "ExcusaInverosimil"
        );
        
        return ResponseEntity.ok(tipos);
    }

    private String obtenerDescripcionMotivo(MotivoExcusa motivo) {
        return switch (motivo) {
            case QUEDARSE_DORMIDO -> "El empleado se quedó dormido";
            case PERDI_TRANSPORTE -> "El empleado perdió el transporte";
            case PERDIDA_SUMINISTRO -> "Hubo pérdida de suministro eléctrico";
            case CUIDADO_FAMILIAR -> "Necesita cuidar a un familiar";
            case IRRELEVANTE -> "Motivo irrelevante o complejo";
            case INCREIBLE_INVEROSIMIL -> "Motivo increíble e inverosímil";
        };
    }

    private String obtenerTipoExcusa(MotivoExcusa motivo) {
        return switch (motivo) {
            case QUEDARSE_DORMIDO, PERDI_TRANSPORTE -> "Trivial";
            case PERDIDA_SUMINISTRO -> "Moderada - Pérdida Suministro";
            case CUIDADO_FAMILIAR -> "Moderada - Cuidado Familiar";
            case IRRELEVANTE -> "Compleja";
            case INCREIBLE_INVEROSIMIL -> "Inverosímil";
        };
    }

    // DTO
    public static class MotivoExcusaResponse {
        private String codigo;
        private String descripcion;
        private String tipoExcusa;

        public MotivoExcusaResponse(String codigo, String descripcion, String tipoExcusa) {
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.tipoExcusa = tipoExcusa;
        }

        public String getCodigo() { return codigo; }
        public String getDescripcion() { return descripcion; }
        public String getTipoExcusa() { return tipoExcusa; }
    }
}
