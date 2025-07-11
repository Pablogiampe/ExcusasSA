package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.empleados.encargados.*;
import ar.edu.davinci.excusas.model.empleados.encargados.modos.*;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/encargados")
public class EncargadoController {

    private final List<EncargadoBase> encargados = new ArrayList<>();

    public EncargadoController() {
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

            EncargadoBase encargadoActualizado = crearEncargadoConNuevoModo(encargado, nuevoModo);

            int index = encargados.indexOf(encargado);
            encargados.set(index, encargadoActualizado);

            return ResponseEntity.ok("Modo cambiado exitosamente a: " + request.getModo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Modo no válido: " + request.getModo());
        }
    }

    @PostMapping
    public ResponseEntity<EncargadoResponse> crearEncargado(@Valid @RequestBody CrearEncargadoRequest request) {
        boolean existeLegajo = encargados.stream()
                .anyMatch(e -> e.getLegajo() == request.getLegajo());

        if (existeLegajo) {
            return ResponseEntity.badRequest().build();
        }

        try {
            EmailSenderImpl emailSender = new EmailSenderImpl();
            IModo modo = crearModo(request.getModo());

            EncargadoBase nuevoEncargado = crearEncargadoPorTipo(
                    request.getTipoEncargado(),
                    request.getNombre(),
                    request.getEmail(),
                    request.getLegajo(),
                    modo,
                    emailSender
            );

            encargados.add(nuevoEncargado);

            EncargadoResponse response = new EncargadoResponse(
                    nuevoEncargado.getLegajo(),
                    nuevoEncargado.getNombre(),
                    nuevoEncargado.getEmail(),
                    nuevoEncargado.getClass().getSimpleName()
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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

    private EncargadoBase crearEncargadoPorTipo(String tipo, String nombre, String email,
                                                int legajo, IModo modo, EmailSenderImpl emailSender) {
        return switch (tipo.toUpperCase()) {
            case "RECEPCIONISTA" -> new Recepcionista(nombre, email, legajo, modo, emailSender);
            case "SUPERVISORAREA" -> new SupervisorArea(nombre, email, legajo, modo, emailSender);
            case "GERENTERRHH" -> new GerenteRRHH(nombre, email, legajo, modo, emailSender);
            case "CEO" -> new CEO(nombre, email, legajo, modo, emailSender);
            default -> throw new IllegalArgumentException("Tipo de encargado no válido: " + tipo);
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
    public static class CrearEncargadoRequest {
        @NotBlank(message = "El nombre no puede estar vacío")
        private String nombre;

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe tener un formato válido")
        private String email;

        @Positive(message = "El legajo debe ser un número positivo")
        private int legajo;

        @NotBlank(message = "El tipo de encargado no puede estar vacío")
        private String tipoEncargado;

        @NotBlank(message = "El modo no puede estar vacío")
        private String modo;

        public CrearEncargadoRequest() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public int getLegajo() { return legajo; }
        public void setLegajo(int legajo) { this.legajo = legajo; }

        public String getTipoEncargado() { return tipoEncargado; }
        public void setTipoEncargado(String tipoEncargado) { this.tipoEncargado = tipoEncargado; }

        public String getModo() { return modo; }
        public void setModo(String modo) { this.modo = modo; }
    }
}
