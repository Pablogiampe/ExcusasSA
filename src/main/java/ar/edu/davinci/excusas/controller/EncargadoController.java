package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.empleados.encargados.*;
import ar.edu.davinci.excusas.model.empleados.encargados.modos.*;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/encargados")
public class EncargadoController {

    private final List<EncargadoBase> encargados = new ArrayList<>();

    public EncargadoController() {
        // Inicializar encargados de ejemplo
        EmailSenderImpl emailSender = new EmailSenderImpl();
        
        encargados.add(new Recepcionista("Ana García", "ana@excusas.com", 1001, new ModoNormal(), emailSender));
        encargados.add(new SupervisorArea("Carlos López", "carlos@excusas.com", 1002, new ModoProductivo(), emailSender));
        encargados.add(new GerenteRRHH("María Rodríguez", "maria@excusas.com", 1003, new ModoNormal(), emailSender));
        encargados.add(new CEO("Roberto Silva", "roberto@excusas.com", 1004, new ModoNormal(), emailSender));
    }

    @GetMapping
    public ResponseEntity<List<EncargadoResponse>> obtenerTodosLosEncargados() {
        List<EncargadoResponse> response = encargados.stream()
                .map(e -> new EncargadoResponse(
                        e.getLegajo(),
                        e.getNombre(),
                        e.getEmail(),
                        e.getClass().getSimpleName()
                ))
                .toList();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EncargadoResponse> obtenerEncargadoPorLegajo(@PathVariable int legajo) {
        Optional<EncargadoBase> encargado = encargados.stream()
                .filter(e -> e.getLegajo() == legajo)
                .findFirst();
        
        if (encargado.isPresent()) {
            EncargadoBase e = encargado.get();
            EncargadoResponse response = new EncargadoResponse(
                    e.getLegajo(),
                    e.getNombre(),
                    e.getEmail(),
                    e.getClass().getSimpleName()
            );
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{legajo}/modo")
    public ResponseEntity<String> cambiarModoEncargado(
            @PathVariable int legajo,
            @RequestBody CambiarModoRequest request) {
        
        Optional<EncargadoBase> encargadoOpt = encargados.stream()
                .filter(e -> e.getLegajo() == legajo)
                .findFirst();
        
        if (encargadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            EncargadoBase encargado = encargadoOpt.get();
            IModo nuevoModo = crearModo(request.getModo());
            
            // Crear nuevo encargado con el modo actualizado
            EncargadoBase encargadoActualizado = crearEncargadoConNuevoModo(encargado, nuevoModo);
            
            // Reemplazar en la lista
            int index = encargados.indexOf(encargado);
            encargados.set(index, encargadoActualizado);
            
            return ResponseEntity.ok("Modo cambiado exitosamente a: " + request.getModo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Modo no válido: " + request.getModo());
        }
    }

    private IModo crearModo(String tipoModo) {
        return switch (tipoModo.toUpperCase()) {
            case "NORMAL" -> new ModoNormal();
            case "PRODUCTIVO" -> new ModoProductivo();
            case "VAGO" -> new ModoVago();
            default -> throw new IllegalArgumentException("Modo no válido: " + tipoModo);
        };
    }

    private EncargadoBase crearEncargadoConNuevoModo(EncargadoBase encargadoOriginal, IModo nuevoModo) {
        EmailSenderImpl emailSender = new EmailSenderImpl();
        
        return switch (encargadoOriginal.getClass().getSimpleName()) {
            case "Recepcionista" -> new Recepcionista(
                    encargadoOriginal.getNombre(),
                    encargadoOriginal.getEmail(),
                    encargadoOriginal.getLegajo(),
                    nuevoModo,
                    emailSender
            );
            case "SupervisorArea" -> new SupervisorArea(
                    encargadoOriginal.getNombre(),
                    encargadoOriginal.getEmail(),
                    encargadoOriginal.getLegajo(),
                    nuevoModo,
                    emailSender
            );
            case "GerenteRRHH" -> new GerenteRRHH(
                    encargadoOriginal.getNombre(),
                    encargadoOriginal.getEmail(),
                    encargadoOriginal.getLegajo(),
                    nuevoModo,
                    emailSender
            );
            case "CEO" -> new CEO(
                    encargadoOriginal.getNombre(),
                    encargadoOriginal.getEmail(),
                    encargadoOriginal.getLegajo(),
                    nuevoModo,
                    emailSender
            );
            default -> throw new IllegalArgumentException("Tipo de encargado no reconocido");
        };
    }

    // DTOs
    public static class EncargadoResponse {
        private int legajo;
        private String nombre;
        private String email;
        private String tipoEncargado;

        public EncargadoResponse(int legajo, String nombre, String email, String tipoEncargado) {
            this.legajo = legajo;
            this.nombre = nombre;
            this.email = email;
            this.tipoEncargado = tipoEncargado;
        }

        public int getLegajo() { return legajo; }
        public String getNombre() { return nombre; }
        public String getEmail() { return email; }
        public String getTipoEncargado() { return tipoEncargado; }
    }

    public static class CambiarModoRequest {
        private String modo;

        public CambiarModoRequest() {}

        public String getModo() { return modo; }
        public void setModo(String modo) { this.modo = modo; }
    }
}
