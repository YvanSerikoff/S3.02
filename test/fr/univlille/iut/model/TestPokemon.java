package fr.univlille.iut.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPokemon {

    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private Pokemon pokemon3;
    private Pokemon pokemon4;
    private Pokemon pokemon5;
    private Pokemon pokemon6;
    private Pokemon pokemon7;


    @BeforeEach
    void setUp() {
        pokemon1 = new Pokemon("Bulbizarre", 49, 5120, 45.0, 49, 1059860, 49, 49, 45, "Grass","Poison",Type.GRASS, Type.POISON, 49, false);
        pokemon2 = new Pokemon("Salamèche", 39, 5120, 39.0, 52, 1059860, 52, 43, 65, "Fire", null, Type.FIRE, null, 39, false);
        pokemon3 = new Pokemon("Carapuce", 44, 5120, 44.0, 48, 1059860, 48, 65, 43, "Water", null, Type.WATER, null, 44, false);
        pokemon4 = new Pokemon("Pikachu", 35, 5120, 35.0, 55, 1059860, 55, 40, 90, "Electric", null, Type.ELECTRIC, null, 35, false);
        pokemon5 = new Pokemon("Herbizarre", 45, 5120, 60.0, 49, 1059860, 49, 49, 45, "Grass","Poison",Type.GRASS, Type.POISON, 45, false);
        pokemon6 = new Pokemon("Reptincel", 58, 5120, 58.0, 64, 1059860, 64, 58, 80, "Fire", null, Type.FIRE, null, 58, false);
        pokemon7 = new Pokemon("Dracaufeu", 78, 5120, 78.0, 84, 1059860, 84, 78, 100, "Fire", "Flying", Type.FIRE, Type.FLYING, 78, false);
    }







    @Test
    void testDetermineCategory() {
        // La liste contient des Pokémon avec des types valides
        List<Pokemon> pokemonList1 = List.of(pokemon1,pokemon2, pokemon3, pokemon4,pokemon5,pokemon6,pokemon7);
        String category1 = pokemon1.determineCategory(pokemonList1);
        assertEquals(Type.FIRE.toString() +" " +Type.POISON.toString(), category1);

        // La liste contient des Pokémon avec des types GRASS
        List<Pokemon> pokemonList2 = List.of(pokemon1,pokemon5, pokemon6,pokemon3);
        String category2 = pokemon1.determineCategory(pokemonList2);
        assertEquals(Type.GRASS.toString()+" " +Type.POISON.toString(), category2);

        // La liste contient des Pokémon avec des types GRASS et FIRE
        List<Pokemon> pokemonList3 = List.of(pokemon1,pokemon5, pokemon6,pokemon7);
        String category3 = pokemon1.determineCategory(pokemonList3);
        assertEquals(Type.FIRE.toString()+" " +Type.POISON.toString(), category3);

    }

    @Test
    void testGetCategoryString() {
        assertEquals("Grass", pokemon1.getCategoryString());
    }


    @Test
    void testGetAndSetX() {
        pokemon1.setX("attack");
        assertEquals(49, pokemon1.getX());

        pokemon1.setX("defense");
        assertEquals(49, pokemon1.getX());

    }

    @Test
    void testGetAndSetY() {
        pokemon1.setY("attack");
        assertEquals(49, pokemon1.getY());

        pokemon1.setY("defense");
        assertEquals(49, pokemon1.getY());
    }


}
