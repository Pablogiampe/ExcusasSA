package ar.edu.davinci.excusas.model.prontuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Observable {
    private final List<Observer> observers = new ArrayList<>();

    public void agregarObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notificarObservers(Prontuario prontuario) {
        for (Observer observer : new ArrayList<>(observers)) {
            observer.actualizar(prontuario); // Llamamos al método 'actualizar' que corregimos antes
        }
    }

    public List<Observer> getObservers() {
        return Collections.unmodifiableList(observers);
    }
}
