package ar.edu.davinci.excusas.model.prontuario;

import ar.edu.davinci.excusas.model.excusas.Excusa;

import java.util.ArrayList;
import java.util.List;

public class AdministradorProntuarios extends Observable {
    private static AdministradorProntuarios instance;
    private final List<Observer> observers;
    private final List<Prontuario> prontuarios;

    private AdministradorProntuarios() {
        this.observers = new ArrayList<>();
        this.prontuarios = new ArrayList<>();
    }

    public static AdministradorProntuarios getInstance() {
        if (instance == null) {
            instance = new AdministradorProntuarios();
        }
        return instance;
    }

    public void guardarProntuario(Excusa excusa) {
        Prontuario prontuario = new Prontuario(excusa.getEmpleado(), excusa);
        prontuarios.add(prontuario);
        System.out.println("Prontuario guardado: " + prontuario);
        notificarObservers(prontuario);
    }

    public List<Prontuario> getProntuarios() {
        return new ArrayList<>(prontuarios);
    }

    @Override
    public void agregarObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(Prontuario prontuario) {
        for (Observer observer : observers) {
            observer.actualizar(prontuario);
        }
    }
}
