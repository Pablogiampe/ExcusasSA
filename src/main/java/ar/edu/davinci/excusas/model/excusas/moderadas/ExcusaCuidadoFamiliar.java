package ar.edu.davinci.excusas.model.excusas.moderadas;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.ManejadorExcusas;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public class ExcusaCuidadoFamiliar extends ExcusaModerada {

    public ExcusaCuidadoFamiliar(Empleado empleado) {
        super(empleado, MotivoExcusa.CUIDADO_FAMILIAR);
    }

    @Override
    public boolean serManejadaPor(ManejadorExcusas manejador) {
        return manejador.manejar(this);
    }
}
