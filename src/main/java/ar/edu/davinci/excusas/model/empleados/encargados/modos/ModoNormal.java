package ar.edu.davinci.excusas.model.empleados.encargados.modos;

import ar.edu.davinci.excusas.model.empleados.encargados.EncargadoBase;
import ar.edu.davinci.excusas.model.excusas.Excusa;
public class ModoNormal implements IModo {
    @Override
    public void manejar(EncargadoBase encargado, Excusa excusa) {
        encargado.procesar(excusa);
    }
}
