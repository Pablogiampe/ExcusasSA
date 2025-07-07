package ar.edu.davinci.excusas.model.empleados.encargados.modos;

import ar.edu.davinci.excusas.model.empleados.encargados.EncargadoBase;
import ar.edu.davinci.excusas.model.excusas.Excusa;

public class ModoVago implements IModo {
    @Override
    public void manejar(EncargadoBase encargado, Excusa excusa) {
        System.out.println("LOG [Modo Vago]: El encargado " + encargado.getNombre() + 
                " está en modo vago y pasa la excusa sin procesarla.");
        
        encargado.getSiguiente().manejarExcusa(excusa);
    }
}
