package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.LineaEncargados;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final List<Empleado> empleados = new ArrayList<>();
    private final LineaEncargados lineaEncargados;

    public EmpleadoService() {
        this.lineaEncargados = new LineaEncargados(new EmailSenderImpl());
        inicializarEmpleados();
    }

    private void inicializarEmpleados() {
        empleados.add(new Empleado("Juan Pérez", "juan.perez@empresa.com", 2001));
        empleados.add(new Empleado("María García", "maria.garcia@empresa.com", 2002));
        empleados.add(new Empleado("Carlos López", "carlos.lopez@empresa.com", 2003));
    }

    public List<Empleado> obtenerTodosLosEmpleados() {
        return new ArrayList<>(empleados);
    }

    public Optional<Empleado> obtenerEmpleadoPorLegajo(int legajo) {
        return empleados.stream()
                .filter(e -> e.getLegajo() == legajo)
                .findFirst();
    }

    public Empleado crearEmpleado(String nombre, String email, int legajo) {
        if (existeEmpleadoConLegajo(legajo)) {
            throw new IllegalArgumentException("Ya existe un empleado con el legajo: " + legajo);
        }

        Empleado nuevoEmpleado = new Empleado(nombre, email, legajo);
        empleados.add(nuevoEmpleado);
        return nuevoEmpleado;
    }

    public Excusa generarExcusa(int legajo, MotivoExcusa motivo) {
        Optional<Empleado> empleadoOpt = obtenerEmpleadoPorLegajo(legajo);

        if (empleadoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró empleado con legajo: " + legajo);
        }

        Empleado empleado = empleadoOpt.get();
        return empleado.generarYEnviarExcusa(motivo, lineaEncargados);
    }

    public boolean eliminarEmpleado(int legajo) {
        return empleados.removeIf(e -> e.getLegajo() == legajo);
    }

    public boolean existeEmpleadoConLegajo(int legajo) {
        return empleados.stream()
                .anyMatch(e -> e.getLegajo() == legajo);
    }

    public LineaEncargados getLineaEncargados() {
        return lineaEncargados;
    }
}