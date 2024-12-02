package fr.univlille.iut.utils;

import java.util.Collection;
import java.util.LinkedList;

public class ObservableProperty extends Observable {

    private Object value;
    private Collection<Observer> observers = new LinkedList<>();

    public Object getValue() {
        return value;
    }

    public void setValue(Object val) {
        if (this.value != val) {
            this.value = val;
            notifyObservers();
        }
    }
    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this, value);
        }
    }
}
