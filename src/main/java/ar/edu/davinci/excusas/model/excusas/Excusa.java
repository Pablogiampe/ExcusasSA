package ar.edu.davinci.excusas.model.excusas;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.ManejadorExcusas;

public abstract class Excusa {
    protected Empleado empleado;
    protected MotivoExcusa motivo;

    public Excusa(Empleado empleado, MotivoExcusa motivo) {
        this.empleado = empleado;
        this.motivo = motivo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public MotivoExcusa getMotivo() {
        return motivo;
    }

    public abstract boolean serManejadaPor(ManejadorExcusas manejador);
}
