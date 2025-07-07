package ar.edu.davinci.excusas.model.prontuario;

import ar.edu.davinci.excusas.model.excepciones.ErrorProcesamiento;
import ar.edu.davinci.excusas.model.excusas.Excusa;

import java.util.ArrayList;
import java.util.List;


public class AdministradorProntuarios extends Observable {
    private static AdministradorProntuarios instancia;
    private final List<Prontuario> prontuarios;

    private AdministradorProntuarios() {
        this.prontuarios = new ArrayList<>();
    }

    public static synchronized AdministradorProntuarios getInstance() {
        if (instancia == null) {
            instancia = new AdministradorProntuarios();
        }
        return instancia;
    }

    public void guardarProntuario(Excusa excusa) {
        if (excusa == null) {
            throw new ErrorProcesamiento("La excusa no puede ser nula para guardar un prontuario");
        }
        if (excusa.getEmpleado() == null) {
            throw new ErrorProcesamiento("La excusa debe tener un empleado asociado para guardar un prontuario");
        }

        try {
            Prontuario prontuario = new Prontuario(
                    excusa.getEmpleado(),
                    excusa,
                    excusa.getEmpleado().getLegajo()
            );

            this.prontuarios.add(prontuario);
            notificarObservers(prontuario);
        } catch (Exception e) {
            throw new ErrorProcesamiento("Error al guardar el prontuario: " + e.getMessage(), e);
        }
    }



    public List<Prontuario> getProntuarios() {
        return new ArrayList<>(prontuarios);
    }

    public static void reset() {
        if (instancia != null) {
            instancia.prontuarios.clear();
        }
    }
}
