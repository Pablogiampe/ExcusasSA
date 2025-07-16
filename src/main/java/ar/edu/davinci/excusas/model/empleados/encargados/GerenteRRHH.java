package ar.edu.davinci.excusas.model.empleados.encargados;

import ar.edu.davinci.excusas.model.empleados.encargados.modos.IModo;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.complejas.ExcusaCompleja;
import ar.edu.davinci.excusas.model.mail.EmailSender;

public class GerenteRRHH extends EncargadoBase {

    public GerenteRRHH(String nombre, String email, int legajo,
                       IModo modo, EmailSender emailSender) {
        super(nombre, email, legajo, modo, emailSender);
    }

    @Override
    public boolean manejar(ExcusaCompleja excusa) {
        System.out.println("Gerente de RRHH " + this.getNombre() + " procesando excusa COMPLEJA.");
        enviarNotificacion(excusa, excusa.getEmpleado().getEmail());
        return true;
    }

    @Override
    protected String asunto(Excusa excusa) {
        return "Análisis de excusa compleja";
    }

    @Override
    protected String cuerpo(Excusa excusa) {
        return "Estimado/a " + excusa.getEmpleado().getNombre() + ",\n\nSu excusa requiere un análisis detallado por parte de RRHH. Se le notificará la resolución.\n\nSaludos.";
    }
}
