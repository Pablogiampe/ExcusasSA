package ar.edu.davinci.excusas.model.excusas.complejas;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.ManejadorExcusas;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public class ExcusaCompleja extends Excusa {

    public ExcusaCompleja(Empleado empleado, MotivoExcusa motivo) {
        super(empleado, motivo);
        if (motivo != MotivoExcusa.IRRELEVANTE) {
            throw new IllegalArgumentException("Motivo no válido para excusa compleja");
        }
    }

    @Override
    public boolean serManejadaPor(ManejadorExcusas manejador) {
        return manejador.manejar(this);
    }
}
