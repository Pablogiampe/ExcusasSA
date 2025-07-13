package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.model.empleados.encargados.*;
import ar.edu.davinci.excusas.model.empleados.encargados.modos.*;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EncargadoService {

    private final List<EncargadoBase> encargados = new ArrayList<>();

    public EncargadoService() {
        inicializarEncargados();
    }

    private void inicializarEncargados() {
        EmailSenderImpl emailSender = new EmailSenderImpl();

        encargados.add(new Recepcionista("Ana García", "ana@excusas.com", 1001, new ModoNormal(), emailSender));
        encargados.add(new SupervisorArea("Carlos López", "carlos@excusas.com", 1002, new ModoProductivo(), emailSender));
        encargados.add(new GerenteRRHH("María Rodríguez", "maria@excusas.com", 1003, new ModoNormal(), emailSender));
        encargados.add(new CEO("Roberto Silva", "roberto@excusas.com", 1004, new ModoNormal(), emailSender));
    }

    public List<EncargadoBase> obtenerTodosLosEncargados() {
        return new ArrayList<>(encargados);
    }

    public Optional<EncargadoBase> obtenerEncargadoPorLegajo(int legajo) {
        return encargados.stream()
                .filter(e -> e.getLegajo() == legajo)
                .findFirst();
    }

    public EncargadoBase cambiarModoEncargado(int legajo, String tipoModo) {
        Optional<EncargadoBase> encargadoOpt = obtenerEncargadoPorLegajo(legajo);

        if (encargadoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró encargado con legajo: " + legajo);
        }

        EncargadoBase encargado = encargadoOpt.get();
        IModo nuevoModo = crearModo(tipoModo);
        EncargadoBase encargadoActualizado = crearEncargadoConNuevoModo(encargado, nuevoModo);

        int index = encargados.indexOf(encargado);
        encargados.set(index, encargadoActualizado);

        return encargadoActualizado;
    }

    public EncargadoBase crearEncargado(String nombre, String email, int legajo,
                                        String tipoEncargado, String tipoModo) {
        if (existeEncargadoConLegajo(legajo)) {
            throw new IllegalArgumentException("Ya existe un encargado con el legajo: " + legajo);
        }

        EmailSenderImpl emailSender = new EmailSenderImpl();
        IModo modo = crearModo(tipoModo);

        EncargadoBase nuevoEncargado = crearEncargadoPorTipo(
                tipoEncargado, nombre, email, legajo, modo, emailSender
        );

        encargados.add(nuevoEncargado);
        return nuevoEncargado;
    }

    private boolean existeEncargadoConLegajo(int legajo) {
        return encargados.stream()
                .anyMatch(e -> e.getLegajo() == legajo);
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
}