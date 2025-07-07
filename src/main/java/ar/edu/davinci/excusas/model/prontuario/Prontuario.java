package ar.edu.davinci.excusas.model.prontuario;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.excusas.Excusa;

public class Prontuario {
    private Empleado empleado;
    private Excusa excusa;
    private int numeroLegajo;

    public Prontuario(Empleado empleado, Excusa excusa, int numeroLegajo) {
        this.empleado = empleado;
        this.excusa = excusa;
        this.numeroLegajo = numeroLegajo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Excusa getExcusa() {
        return excusa;
    }

    public int getNumeroLegajo() {
        return numeroLegajo;
    }

    @Override
    public String toString() {
        return "Prontuario{" +
                "empleado=" + empleado +
                ", excusa=" + excusa.getClass().getSimpleName() +
                ", numeroLegajo=" + numeroLegajo +
                '}';
    }
}
