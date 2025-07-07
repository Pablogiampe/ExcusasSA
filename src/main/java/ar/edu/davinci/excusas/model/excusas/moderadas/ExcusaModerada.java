package ar.edu.davinci.excusas.model.excusas.moderadas;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.ManejadorExcusas;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public class ExcusaModerada extends Excusa {

    public ExcusaModerada(Empleado empleado, MotivoExcusa motivo) {
        super(empleado, motivo);
        if (motivo != MotivoExcusa.PERDIDA_SUMINISTRO && motivo != MotivoExcusa.CUIDADO_FAMILIAR) {
            throw new IllegalArgumentException("Motivo no válido para excusa moderada");
        }
    }

    @Override
    public boolean serManejadaPor(ManejadorExcusas manejador) {
        return manejador.manejar(this);
    }
}
