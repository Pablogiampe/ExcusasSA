package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.prontuario.Prontuario;
import ar.edu.davinci.excusas.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    @Autowired
    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @GetMapping
    public ResponseEntity<List<ProntuarioResponse>> obtenerTodosLosProntuarios() {
        List<Prontuario> prontuarios = prontuarioService.obtenerTodosLosProntuarios();
        List<ProntuarioResponse> response = prontuarios.stream()
                .map(this::convertirAProntuarioResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<List<ProntuarioResponse>> obtenerProntuariosPorLegajo(@PathVariable int legajo) {
        List<Prontuario> prontuarios = prontuarioService.obtenerProntuariosPorLegajo(legajo);
        List<ProntuarioResponse> response = prontuarios.stream()
                .map(this::convertirAProntuarioResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/limpiar")
    public ResponseEntity<String> limpiarProntuarios() {
        prontuarioService.limpiarProntuarios();
        return ResponseEntity.ok("Prontuarios limpiados exitosamente");
    }

    private ProntuarioResponse convertirAProntuarioResponse(Prontuario prontuario) {
        return new ProntuarioResponse(
                prontuario.getEmpleado().getLegajo(),
                prontuario.getEmpleado().getNombre(),
                prontuario.getEmpleado().getEmail(),
                prontuario.getExcusa().getMotivo().toString(),
                prontuario.getExcusa().getClass().getSimpleName()
        );
    }

    public static class ProntuarioResponse {
        private int legajoEmpleado;
        private String nombreEmpleado;
        private String emailEmpleado;
        private String motivoExcusa;
        private String tipoExcusa;

        public ProntuarioResponse(int legajoEmpleado, String nombreEmpleado, String emailEmpleado,
                                  String motivoExcusa, String tipoExcusa) {
            this.legajoEmpleado = legajoEmpleado;
            this.nombreEmpleado = nombreEmpleado;
            this.emailEmpleado = emailEmpleado;
            this.motivoExcusa = motivoExcusa;
            this.tipoExcusa = tipoExcusa;
        }

        public int getLegajoEmpleado() { return legajoEmpleado; }
        public String getNombreEmpleado() { return nombreEmpleado; }
        public String getEmailEmpleado() { return emailEmpleado; }
        public String getMotivoExcusa() { return motivoExcusa; }
        public String getTipoExcusa() { return tipoExcusa; }
    }
}