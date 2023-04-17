package edu.northeastern.info6205.tspsolver.algorithm.christofides;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.util.PointUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChristofidesTest {
    @Test
    public void solvePercentageTest() {
        CSVParserService csvService = CSVParserServiceImpl.getInstance();
        List<Point> graph = csvService.parsePoints("src/test/resources/data/tsp-test-mumbai.csv");

        // create Christofides object and solve TSP
        Christofides christofides = new Christofides(graph);
        List<Point> christofidesTour = christofides.solve();
        double christofidesTourCost = PointUtil.getTotalCost(christofidesTour);

        PrimsMST primsMst = new PrimsMST(graph);
        double mstCost = primsMst.getMstCost();

        double christofidesCostDiff = (christofidesTourCost - mstCost);
        double percentage = (christofidesCostDiff/mstCost) * 100;

        assertTrue(percentage < 100);
    }

    @Test
    public void solveNullTest() {
        assertThrows(NullPointerException.class, () -> {
            Christofides christofides = new Christofides(null);
            christofides.solve();
        });
    }

    @Test
    public void solveEmptyGraphTest() {
        assertThrows(NegativeArraySizeException.class, () -> {
            List<Point> graph = new ArrayList<>();
            Christofides christofides = new Christofides(graph);
            christofides.solve();
        });
    }

    @Test
    public void solveSingleNodeGraphTest() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            List<Point> graph = new ArrayList<>();
            graph.add(new Point(String.valueOf(0), 35.65, 45.45));
            Christofides christofides = new Christofides(graph);
            christofides.solve();
        });
    }

    @Test
    public void solveDoubleNodeGraphTest() {

        List<Point> graph = new ArrayList<>();
        graph.add(new Point(String.valueOf(0), 35.65, 45.45));
        graph.add(new Point(String.valueOf(1), 35.65, 45.46));
        Christofides christofides = new Christofides(graph);
        List<Point> result = christofides.solve();
        assertEquals(1, result.size());
    }
}



