package ar.edu.davinci.excusas.model.empleados.encargados;

import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.complejas.ExcusaCompleja;
import ar.edu.davinci.excusas.model.excusas.inverosimiles.ExcusaInverosimil;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaModerada;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaPerdidaSuministro;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaCuidadoFamiliar;
import ar.edu.davinci.excusas.model.excusas.triviales.ExcusaTrivial;

public interface ManejadorExcusas {
    void manejarExcusa(Excusa excusa);

    boolean manejar(ExcusaTrivial excusa);
    boolean manejar(ExcusaModerada excusa);
    boolean manejar(ExcusaPerdidaSuministro excusa);
    boolean manejar(ExcusaCuidadoFamiliar excusa);
    boolean manejar(ExcusaCompleja excusa);
    boolean manejar(ExcusaInverosimil excusa);
}
