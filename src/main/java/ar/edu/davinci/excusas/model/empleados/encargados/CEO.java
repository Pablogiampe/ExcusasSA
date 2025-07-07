package ar.edu.davinci.excusas.model.empleados.encargados;

import ar.edu.davinci.excusas.model.empleados.encargados.modos.IModo;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.inverosimiles.ExcusaInverosimil;
import ar.edu.davinci.excusas.model.mail.EmailSender;
import ar.edu.davinci.excusas.model.prontuario.AdministradorProntuarios;
import ar.edu.davinci.excusas.model.prontuario.Observer;
import ar.edu.davinci.excusas.model.prontuario.Prontuario;

public class CEO extends EncargadoBase implements Observer {

    public CEO(String nombre, String email, int legajo,
               IModo modo, EmailSender emailSender) {
        super(nombre, email, legajo, modo, emailSender);
        AdministradorProntuarios.getInstance().agregarObserver(this);
    }

    @Override
    public boolean manejar(ExcusaInverosimil excusa) {
        System.out.println("CEO " + this.getNombre() + " procesando excusa INVEROSIMIL.");
        enviarNotificacion(excusa, excusa.getEmpleado().getEmail());
        AdministradorProntuarios.getInstance().guardarProntuario(excusa);
        return true;
    }

    @Override
    protected String asunto(Excusa excusa) {
        return "Sobre su reciente y creativa excusa";
    }

    @Override
    protected String cuerpo(Excusa excusa) {
        return "Estimado/a " + excusa.getEmpleado().getNombre() + ",\n\nSu excusa ha sido aprobada por su notable creatividad. Buen trabajo.\n\nAtte. El CEO.";
    }

    @Override
    public void actualizar(Prontuario prontuario) {
        System.out.println("CEO " + this.getNombre() + " notificado sobre nuevo prontuario: " +
                prontuario.getExcusa().getClass().getSimpleName());
    }
}
