package fr.univlille.iut.model;

import com.opencsv.bean.CsvBindByName;

import java.util.*;

@SuppressWarnings("ALL")
/**
 * Classe représentant une Iris avec ses attributs et ses fonctionnalités.
 */
public class Iris implements DistanceComparable<Iris> {
    @CsvBindByName(column = "sepal.length")
    private double lengthSepal;

    @CsvBindByName(column = "sepal.width")
    private double widthSepal;

    @CsvBindByName(column = "petal.length")
    private double lengthPetal;

    @CsvBindByName(column = "petal.width")
    private double widthPetal;

    @CsvBindByName(column = "variety")
    private String varietyString;

    private Variety variety;

    private double x;
    private double y;

    private boolean isNewIris;

    public Iris() {}

    public Iris(Iris iris) {
        this.lengthSepal = iris.getLengthSepal();
        this.widthSepal = iris.getWidthSepal();
        this.lengthPetal = iris.getLengthPetal();
        this.widthPetal = iris.getWidthPetal();
        this.variety = (Variety) iris.getVariety().get(0);
        this.varietyString = iris.varietyString;
    }

    public Iris(double lengthSepal, double widthSepal, double lengthPetal, double widthPetal, Variety variety) {
        this.lengthSepal = lengthSepal;
        this.widthSepal = widthSepal;
        this.lengthPetal = lengthPetal;
        this.widthPetal = widthPetal;
        this.variety = variety;
        this.varietyString = (variety == null) ? null : variety.toString();
        this.isNewIris = isNewIris;
    }

    public Iris(double lengthSepal, double widthSepal, double lengthPetal, double widthPetal) {
        this(lengthSepal,widthSepal,lengthPetal,widthPetal,null);

    }


    /**
     * Initialise une Iris à partir d'un tableau de valeur
     * @param values tableau de valeurs donnant les valeurs de l'iris
     */
    public void initializeFromValues(double[] values) {
        this.lengthSepal = values[0];
        this.widthSepal = values[1];
        this.lengthPetal = values[2];
        this.widthPetal = values[3];
        this.isNewIris = true;
    }

    public double getLengthSepal() {return lengthSepal;}

    public double getWidthSepal() {return widthSepal;}

    public double getWidthPetal() {return widthPetal;}

    public double getLengthPetal() {return lengthPetal;}

    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }

    public boolean isNew(){
        return this.isNewIris;
    }

    /**
     * Met en forme utilisable une colonne du csv de Iris
     * @param x la colonne non mis en forme
     */
    @Override
    public void setX(String x) {
        switch (x) {
            case "sepal.length" -> this.x = lengthSepal;
            case "sepal.width" -> this.x = widthSepal;
            case "petal.length" -> this.x = lengthPetal;
            case "petal.width" -> this.x = widthPetal;
        }
    }

    /**
     * Met en forme utilisable une colonne du csv de Iris
     * @param y la colonne non mis en forme
     */
    @Override
    public void setY(String y) {
        switch (y) {
            case "sepal.length" -> this.y = lengthSepal;
            case "sepal.width" -> this.y = widthSepal;
            case "petal.length" -> this.y = lengthPetal;
            case "petal.width" -> this.y = widthPetal;
        }
    }

    @Override
    public int getNbAttributesComparable() {
        return 4;
    }

    @Override
    public int getAttribute(int i) {
        return switch (i) {
            case 0 -> (int) lengthSepal;
            case 1 -> (int) widthSepal;
            case 2 -> (int) lengthPetal;
            case 3 -> (int) widthPetal;
            default -> 0;
        };
    }

    @Override
    public String getAttributeName(int i) {
        return switch (i) {
            case 0 -> "sepal.length";
            case 1 -> "sepal.width";
            case 2 -> "petal.length";
            case 3 -> "petal.width";
            default -> null;
        };
    }

    public String getCategoryString() {return varietyString;}
    /**
     * Set la varieté avec un string
     * @param str le string de la variété
     */
    public void setVariety(String str) {
        this.variety=Variety.fromString(str);
        this.varietyString = str;
    }

    public String toString() {
        return "longueurSepale:" + lengthSepal + ", largeurSepale:" + widthSepal +
                ", longueurPetale:" + lengthPetal + ", largeurPetale:" + widthPetal +
                ", categorie:" + variety;
    }

    /**
     * Setup la variété de base de l'Iris
     */
    public void setBaseVariety() {
        if (variety == null && varietyString != null) {
            this.variety = Variety.fromString(varietyString);
        }
    }


    /**
     * Donne sous forme de liste les valeurs de Iris
     */
    public List<Double> getAllValues() {
        return List.of(lengthSepal, widthSepal, lengthPetal, widthPetal);
    }

    public Enum getVar(){
        return this.variety;
    }

    /**
     * Donne sous forme de liste les variétés possible de Iris
     */
    public List<Enum<?>> getVariety() {
        return Arrays.asList(Variety.values());
    }

    public String getCategory() {
        return varietyString;
    }

    //Comparable Distance

    /**
     * Determine la catégorie grâce au voisin
     * @param kNeighbors listes des kPlusProcheVoisins
     */
    @Override
    public String determineCategory(List<Iris> kNeighbors) {
        Map<String, Integer> varietyCount = new HashMap<>();
        for (Iris iris : kNeighbors) {
            String variety = iris.getCategoryString();
            if(variety != null) {
                varietyCount.put(variety, varietyCount.getOrDefault(variety, 0) + 1);
            }
        }
        return varietyCount.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iris iris = (Iris) o;
        return Double.compare(iris.lengthSepal, lengthSepal) == 0 &&
                Double.compare(iris.widthSepal, widthSepal) == 0 &&
                Double.compare(iris.lengthPetal, lengthPetal) == 0 &&
                Double.compare(iris.widthPetal, widthPetal) == 0 &&
                Objects.equals(varietyString, iris.varietyString);
    }
}
