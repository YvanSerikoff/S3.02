package fr.univlille.iut.model;

public interface Distance<T extends DistanceComparable<T>> {
    double distance(T o1, T o2, Model<T> model);
}
