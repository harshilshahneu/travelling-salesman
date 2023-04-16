package edu.northeastern.info6205.tspsolver.algorithm.eulerian;

import org.junit.jupiter.api.Test;

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

    //TODO: Test case fails, take necessary actions
    @Test
    public void testFourVerticesWithNoEularianGraph() {
        FluerysAlgorithm algo = new FluerysAlgorithm(4);
        algo.addEdge(0, 1);
        algo.addEdge(1, 2);
        algo.addEdge(2, 3);
        algo.addEdge(3, 0);
        algo.addEdge(0, 2);
        algo.addEdge(1, 3);
        algo.printEulerTour();
        assertEquals(0, algo.getResult().size());

    }

    //TODO: Test case fails, take necessary actions
    @Test
    public void testFourVerticesWithNoEularianGraph2() {
        FluerysAlgorithm algo = new FluerysAlgorithm(4);
        algo.addEdge(0, 1);
        algo.addEdge(1, 2);
        algo.addEdge(2, 3);
        algo.addEdge(3, 0);
        algo.addEdge(1, 3);
        algo.printEulerTour();
        assertEquals(0, algo.getResult().size());
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
    public void testPrintEularTour() {
        FluerysAlgorithm graph = new FluerysAlgorithm(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);
//        List<int[]> expectedResult = new ArrayList<>();
//        expectedResult.add(new int[] {0, 1});
//        expectedResult.add(new int[] {1, 2});
//        expectedResult.add(new int[] {2, 3});
//        expectedResult.add(new int[] {3, 4});
//        expectedResult.add(new int[] {4, 0});

        int expectedResult = 5;

        graph.printEulerTour();
        assertEquals(expectedResult, graph.getResult().size());
    }
}
