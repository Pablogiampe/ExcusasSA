package ar.edu.davinci.excusas.model.excusas.moderadas;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.ManejadorExcusas;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public class ExcusaPerdidaSuministro extends ExcusaModerada {

    public ExcusaPerdidaSuministro(Empleado empleado) {
        super(empleado, MotivoExcusa.PERDIDA_SUMINISTRO);
    }

    @Override
    public boolean serManejadaPor(ManejadorExcusas manejador) {
        return manejador.manejar(this);
    }
}
