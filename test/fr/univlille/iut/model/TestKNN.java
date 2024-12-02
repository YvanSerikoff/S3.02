package fr.univlille.iut.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestKNN<T extends DistanceComparable<T>> {

    private Model<T> modelPokemon;
    private Model<T> modelIris;

    @BeforeEach
    void setUp(){
        modelPokemon = new Model<>((Class<T>) Pokemon.class);
        String pathPokemon = "res/pokemon_test.csv";
        modelPokemon.load(pathPokemon);
        modelIris = new Model<>((Class<T>) Iris.class);
        String pathIris = "res/iris.csv";
        modelIris.load(pathIris);
    }

    @Test
    void testKnnOnIrisEuclidean() {
        List<T> listItem = modelIris.getData();
        Distance<T> distance = new NormalizedEuclideanDistance<>();
        double successRateK3 = modelIris.calculateSuccessRate(listItem, 3, distance);
        System.out.println("Success rate with k=3 : " + successRateK3 + "%");
        double successRateK5 = modelIris.calculateSuccessRate(listItem, 5, distance);
        System.out.println("Success rate with k=5 : " + successRateK5 + "%");
        assertTrue(successRateK3 >=0);
        assertTrue(successRateK5 > 0);
    }

    @Test
    void testKnnOnIrisManhattan() {
        List<T> listItem = modelIris.getData();
        Distance<T> distance = new NormalizedManhattanDistance<>();
        double successRateK3 = modelIris.calculateSuccessRate(listItem, 3, distance);
        System.out.println("Success rate with k=3 : " + successRateK3 + "%");
        double successRateK5 = modelIris.calculateSuccessRate(listItem, 5, distance);
        System.out.println("Success rate with k=5 : " + successRateK5 + "%");
        assertTrue(successRateK3 > 0);
        assertTrue(successRateK5 > 0);
    }

    @Test
    void testKnnOnPokemonEuclidean() {
        List<T> listItem = modelPokemon.getData();
        Distance<T> distance = new NormalizedEuclideanDistance<>();
        double successRateK3 = modelPokemon.calculateSuccessRate(listItem, 3, distance);
        System.out.println("Success rate with k=3 : " + successRateK3 + "%");
        double successRateK5 = modelPokemon.calculateSuccessRate(listItem, 5, distance);
        System.out.println("Success rate with k=5 : " + successRateK5 + "%");
        assertTrue(successRateK3 > 0);
        assertTrue(successRateK5 > 0);
    }

    @Test
    void testKnnOnPokemonManhattan() {
        List<T> listItem = modelPokemon.getData();
        Distance<T> distance = new NormalizedManhattanDistance<>();
        double successRateK3 = modelPokemon.calculateSuccessRate(listItem, 3, distance);
        System.out.println("Success rate with k=3 : " + successRateK3 + "%");
        double successRateK5 = modelPokemon.calculateSuccessRate(listItem, 5, distance);
        System.out.println("Success rate with k=5 : " + successRateK5 + "%");
        assertTrue(successRateK3 > 0);
        assertTrue(successRateK5 > 0);
    }
}