package fr.univlille.iut.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNormalizedManhattanDistance {

    private Model<Iris> modelIris;
    private Model<Pokemon> modelPokemon;
    private  Iris iris1;
    private  Iris iris2;
    private  Pokemon pokemon1;
    private  Pokemon pokemon2;

    @BeforeEach
    void setUp() {
        iris1 = new Iris(5.1, 3.5, 1.5, 0.2, Variety.SETOSA);
        iris2 = new Iris(4.9, 3.0, 1.4, 0.2, Variety.SETOSA);
        pokemon1 = new Pokemon("Bulbizarre", 49, 5120, 45.0, 49, 1059860, 49, 49, 45, "Grass","Poison",Type.GRASS, Type.POISON, 49, false);
        pokemon2 = new Pokemon("Salamèche", 39, 5120, 39.0, 52, 1059860, 52, 43, 65, "Fire", null, Type.FIRE, null, 39, false);
        modelIris = new Model<>(Iris.class);
        modelIris.load("res/iris.csv");
        modelPokemon = new Model<>(Pokemon.class);
        modelPokemon.load("res/pokemon_train.csv");
    }

    @Test
    void testDistanceIris() {
        NormalizedManhattanDistance<Iris> distance = new NormalizedManhattanDistance<>();
        double[] diff = modelIris.getDifference(iris1,iris2);
        double sumOfAbsolutes = 0;

        for (double value : diff) {
            sumOfAbsolutes += Math.abs(value);
        }

        double expectedDistance = sumOfAbsolutes / diff.length;
        double actualDistance = distance.distance(iris1, iris2,modelIris);

        assertEquals(expectedDistance, actualDistance,0.01);
    }

    @Test
    void testDistancePokemon() {
        NormalizedManhattanDistance<Pokemon> distance = new NormalizedManhattanDistance<>();
        double[] diff = modelPokemon.getDifference(pokemon1,pokemon2);
        double sumOfAbsolutes = 0;

        for (double value : diff) {
            sumOfAbsolutes += Math.abs(value);
        }

        double expectedDistance = sumOfAbsolutes / diff.length;
        double actualDistance = distance.distance(pokemon1, pokemon2,modelPokemon);

        assertEquals(expectedDistance, actualDistance,0.01);
    }

}
