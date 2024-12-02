package fr.univlille.iut.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestIris {

    private Iris iris1;
    private Iris iris2;
    private Iris iris3;

    @BeforeEach
    void setUp() {

         {
            iris1 = new Iris(5.1, 3.5, 1.5, 0.2, Variety.SETOSA);
            iris2 = new Iris(4.9, 3.0, 1.4, 0.2, Variety.SETOSA);
            iris3 = new Iris(6.3, 3.3, 6.0, 2.5, Variety.VIRGINICA);

            System.out.println("iris1 variety: " + iris1.getCategoryString());
            System.out.println("iris2 variety: " + iris2.getCategoryString());
            System.out.println("iris3 variety: " + iris3.getCategoryString());
        }


    }

    @Test
    void testGetLengthSepal() {
        assertEquals(5.1, iris1.getLengthSepal());
    }

    @Test
    void testGetWidthSepal() {
        assertEquals(3.5, iris1.getWidthSepal());
    }



    @Test
    void testGetAllValues() {
        List<Double> expectedValues = List.of(5.1, 3.5, 1.5, 0.2);
        assertEquals(expectedValues, iris1.getAllValues());
    }

    @Test
    void testDetermineCategory() {
        List<Iris> kNeighbors = List.of(iris1, iris2, iris3);
        Variety expectedVariety = Variety.SETOSA;
        assertEquals(expectedVariety.toString(), iris1.determineCategory(kNeighbors));
    }

    @Test
    void testGetAndSetX(){
        iris1.setX("sepal.length");
        assertEquals(5.1, iris1.getX());
    }

    @Test
    void testGetAndSetY(){
        iris1.setY("sepal.width");
        assertEquals(3.5, iris1.getY());
    }
}
