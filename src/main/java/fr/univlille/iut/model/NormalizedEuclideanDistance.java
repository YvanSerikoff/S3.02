package fr.univlille.iut.model;

public class NormalizedEuclideanDistance<T extends DistanceComparable<T>> implements Distance<T> {

    /**
     * Calcule la distance euclidienne normalisé
     *
     * @param o1 le nom de l'attribut 1
     * @param o2 le nom de l'attribut 2
     * @param model le model pour appeller getDifference
     */
    public double distance(T o1, T o2, Model<T> model) {
        double[] diff = model.getDifference(o1, o2);
        double sumOfSquares = 0;

        for (double value : diff) {
            sumOfSquares += Math.pow(value, 2);
        }

        return Math.sqrt(sumOfSquares/diff.length) ;
    }
}
