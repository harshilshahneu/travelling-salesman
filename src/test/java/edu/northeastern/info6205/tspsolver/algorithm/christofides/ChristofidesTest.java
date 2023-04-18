package edu.northeastern.info6205.tspsolver.algorithm.christofides;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.util.PointUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

public class ChristofidesTest {
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
        assertEquals(3, result.size());
    }

    @Test
    public void runSmallOptimizationTest() {
        final String FILE_PATH = "src/test/resources/data/tsp-test-small.csv";
        test(FILE_PATH);
    }

    @Test
    public void runBigOptimizationTest() {
        final String FILE_PATH = "src/test/resources/data/tsp-test-big.csv";
        test(FILE_PATH);
    }

    private void test(String fileName) {
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
        List<Point> points = csvParserService.parsePoints(fileName);

        Christofides christofides = new Christofides(points);
        List<Point> christofidesTour = christofides.solve();
        double christofidesTourCost = PointUtil.getTotalCost(christofidesTour);

        PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();

        double christofidesCostDiff = (christofidesTourCost - mstCost);
        double percentage = (christofidesCostDiff/mstCost) * 100;

        assertTrue(percentage < 100);
    }
}



