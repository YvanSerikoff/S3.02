package fr.univlille.iut.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestModel<T extends DistanceComparable<T>> {

    private final String pathIris = "res/iris.csv";
    private final String pathPokemon = "res/pokemon_train.csv";
    private final String falsePath = "src/main/resources/test.csv";
    private Class<T> type;
    private Model<Iris> modelIris;
    private Model<T> model;
    private Model<Pokemon> modelPokemon;
    private List<Iris> newData;

    @BeforeEach
    void setUp() {
        modelIris = new Model<>(Iris.class);
        modelPokemon = new Model<>(Pokemon.class);
        model = new Model<>(type);
        newData = new ArrayList<>();
        newData.add(new Iris(5.1, 3.5, 1.4, 0.2));
        newData.add(new Iris(4.9, 3.0, 1.4, 0.2));
        newData.add(new Iris(6.7, 3.1, 4.7, 1.5));
        modelIris.setData(newData);
    }


    @Test
    void testLoadRawData() {
        assertEquals(new ArrayList<>(), model.getRawData());
        model.loadRawData(pathIris);
        assertNotNull(model.getRawData());
    }

    @Test
    void testLoadRawDateIris() {
        assertEquals(new ArrayList<>(), modelIris.getRawData());
        modelIris.loadRawData(pathIris);
        assertNotNull(modelIris.getRawData());
    }

    @Test
    void testLoadRawDataPokemon() {
        assertEquals(new ArrayList<>(), modelPokemon.getRawData());
        modelPokemon.loadRawData(pathPokemon);
        assertNotNull(modelPokemon.getRawData());
    }

    @Test
    void testLoad() {
        assertEquals(new ArrayList<>(), model.getRawData());
        model.load(pathIris);
        assertNotNull(model.getData());
    }

    @Test
    void testLoadIris() {
        assertEquals(new ArrayList<>(), modelIris.getRawData());
        modelIris.load(pathIris);
        assertNotNull(modelIris.getData());
    }

    @Test
    void testLoadPokemon() {
        assertEquals(new ArrayList<>(), modelPokemon.getRawData());
        modelPokemon.load(pathPokemon);
        assertNotNull(modelPokemon.getData());
    }

    @Test
    void testGetNearestNeighborsIris() {
        // Création d'une liste d'éléments Iris
        List<Iris> irisList = new ArrayList<>();
        irisList.add(new Iris(5.1, 3.5, 1.4, 0.2));
        irisList.add(new Iris(4.9, 3.0, 1.4, 0.2));
        irisList.add(new Iris(6.7, 3.1, 4.7, 1.5));
        irisList.add(new Iris(5.9, 3.0, 5.1, 1.8));

        Iris target = new Iris(5.0, 3.4, 1.5, 0.2);

        // Appel à la méthode à tester
        List<Iris> nearestNeighbors = modelIris.getNearestNeighbors(target, irisList, new NormalizedEuclideanDistance(), 3);

        // Vérifiez que la liste contient les 3 voisins les plus proches
        assertEquals(3, nearestNeighbors.size());
        assertEquals(nearestNeighbors.get(0), new Iris(5.1, 3.5, 1.4, 0.2));
        assertEquals(nearestNeighbors.get(1), new Iris(4.9, 3.0, 1.4, 0.2));
        assertEquals(nearestNeighbors.get(2), new Iris(6.7, 3.1, 4.7, 1.5));
    }

    @Test
    void testCreateItem() {
        Model<Pokemon> modelPokemon = new Model<>(Pokemon.class);
        Pokemon pokemon = modelPokemon.createItem();
        assertNotNull(pokemon, "L'item créé ne doit pas être null");
        assertEquals(Pokemon.class, pokemon.getClass(), "L'item créé doit être de type Pokemon");

        Model<Iris> modelIris = new Model<>(Iris.class);
        Iris iris = modelIris.createItem();
        assertNotNull(iris, "L'item créé ne doit pas être null");
        assertEquals(Iris.class, iris.getClass(), "L'item créé doit être de type Iris");
    }
}
