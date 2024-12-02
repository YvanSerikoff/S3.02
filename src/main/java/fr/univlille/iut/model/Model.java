package fr.univlille.iut.model;

import com.opencsv.bean.CsvToBeanBuilder;
import fr.univlille.iut.utils.Observable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("ALL")
public class Model<T extends DistanceComparable<T>> extends Observable implements KNN<T> {
    private List<T> rawData;
    private List<T> data;
    private List<T> newData;
    private final Class<T> type;
    private double x;
    private double y;

    /**
     * Constructeur de la classe Model
     * @param type le type de données
     */

    public Model(Class<T> type) {
        this.type = type;
        rawData = new ArrayList<>();
        data = new ArrayList<>();
        newData = new ArrayList<>();
    }

    /**
     * Ajoute un élément à la liste des données
     * @param item l'élément à ajouter
     */
    public void addData(T item, String axeX, String axeY) {
        data.add(item);
        item.setX(axeX);
        item.setY(axeY);
        categorizeData();
        notifyObservers();
    }

    /**
     * Charge les données brutes dans la liste principale après traitement (si nécessaire)
     * @param filePath chemin du fichier CSV
     */
    public void load(String filePath) {
        loadRawData(filePath);
        data.clear();
        data.addAll(rawData);
        initializeCategories();
        notifyObservers();
    }

    /**
     * Set les variety par défaut de T
     */
    private void initializeCategories() {
        for (T item : data) {
            item.setBaseVariety();
        }
    }

    /**
     * Categorize tout les newData avec knn euclidien
     */
    public void categorizeData() {
        NormalizedEuclideanDistance distance = new NormalizedEuclideanDistance();
        for (T item : newData) {
            this.knn(item, data, 3, distance);
        }
    }

    /**
     * Charge les données brutes depuis un fichier CSV
     * @param filePath chemin du fichier CSV
     */
    public void loadRawData(String filePath) {
        rawData.clear();

        try {
            System.out.println("Loading data from file: " + filePath);
            rawData = new CsvToBeanBuilder<T>(Files.newBufferedReader(Paths.get(filePath)))
                    .withSeparator(',')
                    .withType(type)
                    .build()
                    .parse();
            System.out.println("Data loaded successfully. Number of records: " + rawData.size());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing CSV: " + e.getMessage());
        }
    }


    /**
     * KNN
     * @param item l'item de base sur le quel l'algorithme doit déduire la catégorie
     * @param listItem l'observable
     * @param k nombres de voisins
     * @param distance le type de distance utilisé
     */
    public String knn(T item, List<T> listItem, int k, Distance<T> distance) {
        List<T> nearestNeighbors = this.getNearestNeighbors(item, listItem, distance, k);
        return  item.determineCategory(nearestNeighbors);
    }

    /**
     * Retourne les k voisins les plus proches d'un élément
     * @param item l'élément
     * @param listItem la liste des éléments
     * @param algoDist l'algorithme de distance
     * @param k le nombre de voisins
     * @return les k voisins les plus proches
     */
    public List<T> getNearestNeighbors(T item, List<T> listItem, Distance<T> algoDist, int k) {
        List<T> nearbyNeighbors = new ArrayList<>();
        List<Double> distances = new ArrayList<>();

        for (T knownItem : listItem) {
            double distance = algoDist.distance(item, knownItem, this);
            distances.add(distance);
            nearbyNeighbors.add(knownItem);
        }

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < distances.size(); i++) {
            indices.add(i);
        }

        Collections.sort(indices, Comparator.comparingDouble(distances::get));

        List<T> sortedNeighbors = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            sortedNeighbors.add(nearbyNeighbors.get(indices.get(i)));
        }

        return sortedNeighbors;
    }

    /**
     * Calcule le taux de succes de notre algorithme KNN
     * @param listItem l'observable
     * @param k nombres de voisins
     * @param distance le type de distance utilisé
     */
    public double calculateSuccessRate(List<T> listItems, int k, Distance<T> distance) {
        int correctPredictions = 0;

        for (T testPlayer : listItems) {
            String predictedPoste = this.knn(testPlayer, listItems, k, distance);
            if (predictedPoste.equals(testPlayer.getCategory())) {
                correctPredictions++;
            }
        }

        return (correctPredictions / (double) listItems.size()) * 100;
    }

    /**
     * Fait la différence entre deux item
     * @param T l'item n1
     * @param T l'itme n2
     */
    public double[] getDifference(T item, T other) {
        int nbAttributes = other.getNbAttributesComparable();
        double[] diff = new double[nbAttributes];
        for (int i = 0; i < nbAttributes; i++) {
            String attributeName = other.getAttributeName(i);
            double normalizedValue = normalized(item.getAttribute(i), other.getAttribute(i), attributeName);
            diff[i] = Math.abs(normalizedValue);
        }
        return diff;
    }

    public double normalized(int value1, int value2, String attribute) {
    /**
     * Normalise une valeur
     * @param int la valeur
     * @param attribute nom de la colonne
     */
        double max = getMaxValues().get(attribute);
        double min = getMinValues().get(attribute);
        double normalizedValue1 = (value1 - min) / (max - min);
        double normalizedValue2 = (value2 - min) / (max - min);

        return normalizedValue1 - normalizedValue2;
    }

    //Normalisation

    public Map<String, Double> getMaxValues() {
        Map<String, Double> maxValues = new HashMap<>();
        for (T item : data) {
            for (int i = 0; i < item.getNbAttributesComparable(); i++) {
                String attributeName = item.getAttributeName(i);
                double value = item.getAttribute(i);
                maxValues.put(attributeName, Math.max(maxValues.getOrDefault(attributeName, Double.MIN_VALUE), value));
            }
        }
        return maxValues;
    }

    public Map<String, Double> getMinValues() {
        Map<String, Double> minValues = new HashMap<>();
        for (T item : data) {
            for (int i = 0; i < item.getNbAttributesComparable(); i++) {
                String attributeName = item.getAttributeName(i);
                double value = item.getAttribute(i);
                minValues.put(attributeName, Math.min(minValues.getOrDefault(attributeName, Double.MAX_VALUE), value));
            }
        }
        return minValues;
    }

    /**
     * Retourne la liste des données brutes
     * @return la liste des données brutes
     */
    public List<T> getRawData() {
        return rawData;
    }

    /**
     * Retourne la liste des données
     * @return la liste des données
     */
    public List<T> getData() {
        ArrayList<T> list=new ArrayList<>();
        list.addAll(data);
        list.addAll(newData);
        return list;
    }

    public List<T> getDatas(){
        return data;
    }

    public double getX(T item) {
        return item.getX();
    }

    public double getY(T item) {
        return item.getY();
    }

    public T createItem() {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setData(List<T> data) {
        this.data=data;
    }
}
