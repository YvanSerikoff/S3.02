package fr.univlille.iut.model;

import java.util.List;

public interface DistanceComparable<T> {

    List<Enum<?>> getVariety();
    void setBaseVariety();
    void initializeFromValues(double[] values);
    boolean isNew();
    Enum getVar();
    String getCategory();
    void setVariety(String str);

    String determineCategory(List<T> kNeighbors);
    String getCategoryString();

    double getX();
    double getY();
    void setX(String x);
    void setY(String y);

    int getNbAttributesComparable();

    int getAttribute(int i);
    String getAttributeName(int i);
}
