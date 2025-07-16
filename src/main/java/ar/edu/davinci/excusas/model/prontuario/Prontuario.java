package ar.edu.davinci.excusas.model.prontuario;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.excusas.Excusa;

import java.time.LocalDateTime;

public class Prontuario {
    private final Empleado empleado;
    private final Excusa excusa;
    private final LocalDateTime fechaCreacion;

    public Prontuario(Empleado empleado, Excusa excusa) {
        this.empleado = empleado;
        this.excusa = excusa;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Excusa getExcusa() {
        return excusa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public String toString() {
        return "Prontuario{" +
                "empleado=" + empleado.getNombre() +
                ", excusa=" + excusa.getClass().getSimpleName() +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
