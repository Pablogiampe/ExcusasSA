package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.ProntuarioDTO;
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
    public ResponseEntity<List<ProntuarioDTO>> obtenerTodosLosProntuarios() {
        List<ProntuarioDTO> prontuarios = prontuarioService.obtenerTodosLosProntuarios();
        return ResponseEntity.ok(prontuarios);
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<List<ProntuarioDTO>> obtenerProntuariosPorLegajo(@PathVariable int legajo) {
        List<ProntuarioDTO> prontuarios = prontuarioService.obtenerProntuariosPorLegajo(legajo);
        return ResponseEntity.ok(prontuarios);
    }

    @DeleteMapping("/limpiar")
    public ResponseEntity<String> limpiarProntuarios() {
        prontuarioService.limpiarProntuarios();
        return ResponseEntity.ok("Prontuarios limpiados exitosamente");
    }
}
