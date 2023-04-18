package edu.northeastern.info6205.tspsolver.algorithm.eulerian;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluerysAlgorithmTest {

    @Test
    public void testAddEdge() {
        FluerysAlgorithm algo = new FluerysAlgorithm(1);
        algo.addEdge(0, 0);
        algo.printEulerTour();
        assertEquals(1, algo.getResult().size());
    }

    @Test
    public void testTwoVerticesAndOneEdge() {
        FluerysAlgorithm algo = new FluerysAlgorithm(2);
        algo.addEdge(0, 1);
        algo.printEulerTour();
        assertEquals(1, algo.getResult().size());
    }

    @Test
    public void testThreeVerticesWithEularianTour() {
        FluerysAlgorithm algo = new FluerysAlgorithm(3);
        algo.addEdge(0, 1);
        algo.addEdge(0, 2);
        algo.addEdge(1, 2);
        assertEquals(0, algo.getResult().size());
    }

    @Test
    public void testThreeVerticesAndEularianTour() {
        FluerysAlgorithm algo = new FluerysAlgorithm(3);
        algo.addEdge(0, 1);
        algo.addEdge(1, 2);
        algo.addEdge(2, 0);
        algo.printEulerTour();
        assertEquals(3, algo.getResult().size());
    }

    @Test
    public void testFiveVerticesWithEularianGraph() {
        FluerysAlgorithm algo = new FluerysAlgorithm(5);
        algo.addEdge(0, 1);
        algo.addEdge(0, 2);
        algo.addEdge(1, 2);
        algo.addEdge(2, 3);
        algo.addEdge(3, 4);
        algo.addEdge(4, 2);
        algo.printEulerTour();
        assertEquals(6, algo.getResult().size());

    }

    @Test
    public void eularTourLengthTest() {
        FluerysAlgorithm graph = new FluerysAlgorithm(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);

        int expectedResult = 5;

        graph.printEulerTour();
        assertEquals(expectedResult, graph.getResult().size());
    }

    @Test
    public void eularTourCycleTest() {
        FluerysAlgorithm graph = new FluerysAlgorithm(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);

        graph.printEulerTour();
        List<int[]> result = graph.getResult();
        assertTrue(graph.getResult().get(0)[0] == graph.getResult().get(result.size()-1)[1]);
    }

    @Test
    public void eularTourNonCycleNodesTest() {
        FluerysAlgorithm graph = new FluerysAlgorithm(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);

        graph.printEulerTour();
        List<int[]> result = graph.getResult();
        List<int[]> expectedTour = new ArrayList<>();

        expectedTour.add(new int[] {0,1});
        expectedTour.add(new int[] {1,2});
        expectedTour.add(new int[] {2,0});

        boolean equal = isEqualArrayByValues(result, expectedTour);
        assertTrue(equal);
    }

    @Test
    public void eularTourCycleTourTest() {
        FluerysAlgorithm graph = new FluerysAlgorithm(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 2);
        graph.addEdge(2, 0);

        graph.printEulerTour();
        List<int[]> result = graph.getResult();
        List<int[]> expectedTour = new ArrayList<>();

        expectedTour.add(new int[] {0,1});
        expectedTour.add(new int[] {1,2});
        expectedTour.add(new int[] {2,3});
        expectedTour.add(new int[] {3,4});
        expectedTour.add(new int[] {4,2});
        expectedTour.add(new int[] {2,0});

        boolean equal = isEqualArrayByValues(result, expectedTour);
        assertTrue(equal);
    }

    private static boolean isEqualArrayByValues(List<int[]> result, List<int[]> expectedTour) {
        boolean equal = true;
        if (expectedTour.size() != result.size()) {
            equal = false;
        } else {
            for (int i = 0; i < expectedTour.size(); i++) {
                if (!Arrays.equals(expectedTour.get(i), result.get(i))) {
                    equal = false;
                    break;
                }
            }
        }
        return equal;
    }
}
