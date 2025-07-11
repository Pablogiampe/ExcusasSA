package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.prontuario.AdministradorProntuarios;
import ar.edu.davinci.excusas.model.prontuario.Prontuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    @GetMapping
    public ResponseEntity<List<ProntuarioResponse>> obtenerTodosLosProntuarios() {
        List<Prontuario> prontuarios = AdministradorProntuarios.getInstance().getProntuarios();
        
        List<ProntuarioResponse> response = prontuarios.stream()
                .map(p -> new ProntuarioResponse(
                        p.getNumeroLegajo(),
                        p.getEmpleado().getNombre(),
                        p.getEmpleado().getEmail(),
                        p.getExcusa().getClass().getSimpleName(),
                        p.getExcusa().getMotivo().toString()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleado/{legajo}")
    public ResponseEntity<List<ProntuarioResponse>> obtenerProntuariosPorEmpleado(@PathVariable int legajo) {
        List<Prontuario> prontuarios = AdministradorProntuarios.getInstance().getProntuarios();
        
        List<ProntuarioResponse> response = prontuarios.stream()
                .filter(p -> p.getNumeroLegajo() == legajo)
                .map(p -> new ProntuarioResponse(
                        p.getNumeroLegajo(),
                        p.getEmpleado().getNombre(),
                        p.getEmpleado().getEmail(),
                        p.getExcusa().getClass().getSimpleName(),
                        p.getExcusa().getMotivo().toString()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> limpiarProntuarios() {
        AdministradorProntuarios.reset();
        return ResponseEntity.ok().build();
    }

    public static class ProntuarioResponse {
        private int numeroLegajo;
        private String nombreEmpleado;
        private String emailEmpleado;
        private String tipoExcusa;
        private String motivoExcusa;

        public ProntuarioResponse(int numeroLegajo, String nombreEmpleado, String emailEmpleado, 
                                String tipoExcusa, String motivoExcusa) {
            this.numeroLegajo = numeroLegajo;
            this.nombreEmpleado = nombreEmpleado;
            this.emailEmpleado = emailEmpleado;
            this.tipoExcusa = tipoExcusa;
            this.motivoExcusa = motivoExcusa;
        }

        public int getNumeroLegajo() { return numeroLegajo; }
        public String getNombreEmpleado() { return nombreEmpleado; }
        public String getEmailEmpleado() { return emailEmpleado; }
        public String getTipoExcusa() { return tipoExcusa; }
        public String getMotivoExcusa() { return motivoExcusa; }
    }
}
