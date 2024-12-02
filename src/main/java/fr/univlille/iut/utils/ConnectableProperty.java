package fr.univlille.iut.utils;

import java.util.HashSet;
import java.util.Set;

public class ConnectableProperty extends ObservableProperty implements Observer {

    private Set<ConnectableProperty> connections = new HashSet<>();

    @Override
    public void update(Observable subj) {
        if (subj instanceof ConnectableProperty) {
            ConnectableProperty property = (ConnectableProperty) subj;
            if (!property.getValue().equals(this.getValue())) {
                this.setValue(property.getValue());
            }
        }
    }

    @Override
    public void update(Observable subj, Object data) {
        if (!data.equals(this.getValue())) {
            this.setValue(data);
        }
    }

    public void connectTo(ConnectableProperty other) {
        other.attach(this);
        this.setValue(other.getValue());
    }

    public void biconnectTo(ConnectableProperty other) {
        if (!connections.contains(other)) {
            this.connectTo(other);
            other.connectTo(this);
            connections.add(other);
        }
    }

    public void unconnectFrom(ConnectableProperty other) {
        other.detach(this);
        other.connections.remove(this);
        this.connections.remove(other);
    }

    @Override
    public void setValue(Object val) {
        super.setValue(val);
        notifyObservers(val);
        propagateValue(val);
    }

    private void propagateValue(Object val) {
        for (ConnectableProperty connected : connections) {
            try{
                if (!connected.getValue().equals(val)) {
                    connected.setValue(val);
                }
            } catch (NullPointerException e) {System.out.println(e.getMessage());}
        }
    }
}
