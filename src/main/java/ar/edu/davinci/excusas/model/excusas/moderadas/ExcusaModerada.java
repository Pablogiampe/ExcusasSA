package ar.edu.davinci.excusas.model.excusas.moderadas;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public abstract class ExcusaModerada extends Excusa {

    public ExcusaModerada(Empleado empleado, MotivoExcusa motivo) {
        super(empleado, motivo);
    }
}
