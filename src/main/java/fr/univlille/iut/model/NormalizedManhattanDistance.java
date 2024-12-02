package fr.univlille.iut.model;

public class NormalizedManhattanDistance <T extends DistanceComparable<T>> implements Distance<T> {

    /**
     * Calcule la distance manhattan normalisé
     *
     * @param o1 le nom de l'attribut 1
     * @param o2 le nom de l'attribut 2
     * @param model le model pour appeller getDifference
     */
    public double distance(T o1, T o2, Model<T> model) {
        double[] diff = model.getDifference(o1, o2);
        double sumOfAbs = 0;

        for (double value : diff) {
            sumOfAbs += Math.abs(value);
        }

        return sumOfAbs/diff.length;
    }
}
