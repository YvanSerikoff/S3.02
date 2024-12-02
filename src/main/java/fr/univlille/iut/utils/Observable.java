package fr.univlille.iut.utils;

import java.util.Collection;
import java.util.LinkedList;

public abstract class Observable {

    protected Collection<Observer> obs = new LinkedList<>();

    protected void notifyObservers() {
        Collection<Observer> observersCopy = new LinkedList<>(obs);
        for(Observer o : observersCopy) {
            o.update(this);
        }
    }

    protected void notifyObservers(Object data) {
        Collection<Observer> observersCopy = new LinkedList<>(obs);
        for(Observer o : observersCopy) {
            o.update(this, data);
        }
    }

    public void attach(Observer observer) {
        this.obs.add(observer);
    }

    public void detach(Observer observer) {
        this.obs.remove(observer);
    }
}
