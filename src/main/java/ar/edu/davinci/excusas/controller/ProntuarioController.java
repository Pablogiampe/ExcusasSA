package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.ProntuarioDTO;
import ar.edu.davinci.excusas.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prontuarios")
@CrossOrigin(origins = "*")
public class ProntuarioController {
    
    @Autowired
    private ProntuarioService prontuarioService;
    
    @GetMapping
    public ResponseEntity<List<ProntuarioDTO>> obtenerTodosLosProntuarios() {
        try {
            List<ProntuarioDTO> prontuarios = prontuarioService.obtenerTodosLosProntuarios();
            return ResponseEntity.ok(prontuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioDTO> obtenerProntuarioPorId(@PathVariable Long id) {
        try {
            Optional<ProntuarioDTO> prontuario = prontuarioService.obtenerProntuarioPorId(id);
            return prontuario.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/empleado/{legajo}")
    public ResponseEntity<List<ProntuarioDTO>> obtenerProntuariosPorEmpleado(@PathVariable Integer legajo) {
        try {
            List<ProntuarioDTO> prontuarios = prontuarioService.obtenerProntuariosPorEmpleado(legajo);
            return ResponseEntity.ok(prontuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/excusa/{excusaId}")
    public ResponseEntity<List<ProntuarioDTO>> obtenerProntuariosPorExcusa(@PathVariable Long excusaId) {
        try {
            List<ProntuarioDTO> prontuarios = prontuarioService.obtenerProntuariosPorExcusa(excusaId);
            return ResponseEntity.ok(prontuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<ProntuarioDTO>> buscarProntuarios(
            @RequestParam(required = false) Integer legajo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta) {
        try {
            List<ProntuarioDTO> prontuarios = prontuarioService.buscarProntuarios(legajo, fechaDesde, fechaHasta);
            return ResponseEntity.ok(prontuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
