package ar.edu.davinci.excusas.model.excusas.triviales;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.ManejadorExcusas;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public class ExcusaTrivial extends Excusa {

    public ExcusaTrivial(Empleado empleado, MotivoExcusa motivo) {
        super(empleado, motivo);
        if (motivo != MotivoExcusa.QUEDARSE_DORMIDO && motivo != MotivoExcusa.PERDI_TRANSPORTE) {
            throw new IllegalArgumentException("Motivo no válido para excusa trivial");
        }
    }

    @Override
    public boolean serManejadaPor(ManejadorExcusas manejador) {
        return manejador.manejar(this);
    }
}
