package ar.edu.davinci.excusas.model.empleados.encargados.modos;

import ar.edu.davinci.excusas.model.empleados.encargados.EncargadoBase;
import ar.edu.davinci.excusas.model.excusas.Excusa;
public interface IModo {
    void manejar(EncargadoBase encargado, Excusa excusa);
}
