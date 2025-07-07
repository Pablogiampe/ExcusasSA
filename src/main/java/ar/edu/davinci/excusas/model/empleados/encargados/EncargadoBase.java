package ar.edu.davinci.excusas.model.empleados.encargados;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.modos.IModo;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.complejas.ExcusaCompleja;
import ar.edu.davinci.excusas.model.excusas.inverosimiles.ExcusaInverosimil;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaModerada;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaPerdidaSuministro;
import ar.edu.davinci.excusas.model.excusas.moderadas.ExcusaCuidadoFamiliar;
import ar.edu.davinci.excusas.model.excusas.triviales.ExcusaTrivial;
import ar.edu.davinci.excusas.model.mail.EmailSender;

public abstract class EncargadoBase extends Empleado implements ManejadorExcusas {
    protected ManejadorExcusas siguiente;
    protected IModo modo;
    protected EmailSender emailSender;

    public EncargadoBase(String nombre, String email, int legajo,
                         IModo modo, EmailSender emailSender) {
        super(nombre, email, legajo);
        this.modo = modo;
        this.emailSender = emailSender;
    }

    public void setSiguiente(ManejadorExcusas siguiente) {
        this.siguiente = siguiente;
    }
    
    public ManejadorExcusas getSiguiente() {
        return siguiente;
    }

    @Override
    public final void manejarExcusa(Excusa excusa) {
        modo.manejar(this, excusa);
    }

    public final void procesar(Excusa excusa) {
        boolean procesada = excusa.serManejadaPor(this);
        
        if (!procesada) {
            siguiente.manejarExcusa(excusa);
        }
    }

    @Override
    public boolean manejar(ExcusaTrivial excusa) {
        return false;
    }

    @Override
    public boolean manejar(ExcusaModerada excusa) {
        return false;
    }

    @Override
    public boolean manejar(ExcusaPerdidaSuministro excusa) {
        return false;
    }

    @Override
    public boolean manejar(ExcusaCuidadoFamiliar excusa) {
        return false;
    }

    @Override
    public boolean manejar(ExcusaCompleja excusa) {
        return false;
    }

    @Override
    public boolean manejar(ExcusaInverosimil excusa) {
        return false;
    }

    protected void enviarNotificacion(Excusa excusa, String destinatario) {
        emailSender.enviarEmail(
                destinatario,
                this.getEmail(),
                this.asunto(excusa),
                this.cuerpo(excusa)
        );
    }

    public EmailSender getEmailSender() {
        return emailSender;
    }

    protected abstract String asunto(Excusa excusa);
    protected abstract String cuerpo(Excusa excusa);
}
