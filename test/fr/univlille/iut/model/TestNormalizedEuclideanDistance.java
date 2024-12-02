package fr.univlille.iut.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNormalizedEuclideanDistance {

    private Model<Iris> modelIris;
    private Model<Pokemon> modelPokemon;
    private final String pathIris = "res/iris.csv";
    private final String pathPokemon = "res/pokemon_train.csv";
    private final String falsePath = "src/main/resources/test.csv";
    private Iris iris1;
    private Iris iris2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;

    @BeforeEach
    void setUp() {
        iris1 = new Iris(5.1, 3.5, 1.5, 0.2, Variety.SETOSA);
        iris2 = new Iris(4.9, 3.0, 1.4, 0.2, Variety.SETOSA);
        pokemon1 = new Pokemon("Bulbizarre", 49, 5120, 45.0, 49, 1059860, 49, 49, 45, "Grass","Poison",Type.GRASS, Type.POISON, 49, false);
        pokemon2 = new Pokemon("Salamèche", 39, 5120, 39.0, 52, 1059860, 52, 43, 65, "Fire", null, Type.FIRE, null, 39, false);
        modelIris = new Model<>(Iris.class);
        modelIris.load(pathIris);
        modelPokemon = new Model<>(Pokemon.class);
        modelPokemon.load(pathPokemon);
    }

    @Test
    void testDistanceIris() {
        NormalizedEuclideanDistance<Iris> distance = new NormalizedEuclideanDistance<>();
        double[] diff = modelIris.getDifference(iris1,iris2);
        double sumOfSquares = 0;

        for (double value : diff) {
            sumOfSquares += Math.pow(value, 2);
        }

        double expectedDistance = Math.sqrt(sumOfSquares / diff.length);
        double actualDistance = distance.distance(iris1, iris2,modelIris);

        assertEquals(expectedDistance, actualDistance,0.01);
    }

    @Test
  void testDistancePokemon() {
        NormalizedEuclideanDistance<Pokemon> distance = new NormalizedEuclideanDistance<>();
        double[] diff = modelPokemon.getDifference(pokemon1, pokemon2);
        double sumOfSquares = 0;

        for (double value : diff) {
            sumOfSquares += Math.pow(value, 2);
        }

        double expectedDistance = Math.sqrt(sumOfSquares / diff.length);
        double actualDistance = distance.distance(pokemon1, pokemon2,modelPokemon);
       assertEquals(expectedDistance, actualDistance,0.01);
  }

}
