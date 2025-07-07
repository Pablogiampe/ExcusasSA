package ar.edu.davinci.excusas.model.excusas.inverosimiles;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.ManejadorExcusas;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public class ExcusaInverosimil extends Excusa {

    public ExcusaInverosimil(Empleado empleado, MotivoExcusa motivo) {
        super(empleado, motivo);
        if (motivo != MotivoExcusa.INCREIBLE_INVEROSIMIL) {
            throw new IllegalArgumentException("Motivo no válido para excusa inverosímil");
        }
    }

    @Override
    public boolean serManejadaPor(ManejadorExcusas manejador) {
        return manejador.manejar(this);
    }
}
