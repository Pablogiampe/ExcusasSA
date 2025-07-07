package ar.edu.davinci.excusas.model.empleados.encargados;

import ar.edu.davinci.excusas.model.empleados.encargados.modos.ModoNormal;
import ar.edu.davinci.excusas.model.empleados.encargados.modos.ModoProductivo;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.mail.EmailSender;

public class LineaEncargados {

    private final ManejadorExcusas primerEncargado;

    public LineaEncargados(EmailSender emailSender) {
        Recepcionista recepcionista = new Recepcionista("Ana García", "ana@excusas.com", 1001, new ModoNormal(), emailSender);
        SupervisorArea supervisor = new SupervisorArea("Carlos López", "carlos@excusas.com", 1002, new ModoProductivo(), emailSender);
        GerenteRRHH gerente = new GerenteRRHH("María Rodríguez", "maria@excusas.com", 1003, new ModoNormal(), emailSender);
        CEO ceo = new CEO("Roberto Silva", "roberto@excusas.com", 1004, new ModoNormal(), emailSender);
        Rechazador rechazador = new Rechazador();

        recepcionista.setSiguiente(supervisor);
        supervisor.setSiguiente(gerente);
        gerente.setSiguiente(ceo);
        ceo.setSiguiente(rechazador);

        this.primerEncargado = recepcionista;
    }

    public void manejarExcusa(Excusa excusa) {
        primerEncargado.manejarExcusa(excusa);
    }
}
