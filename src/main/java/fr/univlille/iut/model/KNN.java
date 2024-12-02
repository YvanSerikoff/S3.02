package fr.univlille.iut.model;

import java.util.List;

public interface KNN<T extends DistanceComparable<T>> {
    List<T> getNearestNeighbors(T item, List<T> listData, Distance<T> distance, int k);
    String knn(T other, List<T> listData, int k, Distance<T> distance);
}
