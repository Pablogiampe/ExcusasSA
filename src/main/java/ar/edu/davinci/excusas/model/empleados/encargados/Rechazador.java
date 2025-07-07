package ar.edu.davinci.excusas.model.empleados.encargados;

import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.complejas.ExcusaCompleja;
import ar.edu.davinci.excusas.model.excusas.inverosimiles.ExcusaInverosimil;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaModerada;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaPerdidaSuministro;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaCuidadoFamiliar;
import ar.edu.davinci.excusas.model.excusas.triviales.ExcusaTrivial;

public class Rechazador implements ManejadorExcusas {

    private void rechazar() {
        System.out.println("Excusa rechazada: ningún encargado pudo procesarla.");
    }

    @Override
    public void manejarExcusa(Excusa excusa) {
        excusa.serManejadaPor(this);
    }

    @Override
    public boolean manejar(ExcusaTrivial excusa) {
        rechazar();
        return true;
    }

    @Override
    public boolean manejar(ExcusaModerada excusa) {
        rechazar();
        return true;
    }

    @Override
    public boolean manejar(ExcusaPerdidaSuministro excusa) {
        rechazar();
        return true;
    }

    @Override
    public boolean manejar(ExcusaCuidadoFamiliar excusa) {
        rechazar();
        return true;
    }

    @Override
    public boolean manejar(ExcusaCompleja excusa) {
        rechazar();
        return true;
    }

    @Override
    public boolean manejar(ExcusaInverosimil excusa) {
        rechazar();
        return true;
    }
}
