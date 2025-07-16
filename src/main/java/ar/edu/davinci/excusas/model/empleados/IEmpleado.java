package ar.edu.davinci.excusas.model.empleados;

import ar.edu.davinci.excusas.model.empleados.encargados.LineaEncargados;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;

public interface IEmpleado {
    String getNombre();
    String getEmail();
    int getLegajo();
    Excusa generarYEnviarExcusa(MotivoExcusa motivo, LineaEncargados linea);
}
