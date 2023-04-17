package edu.northeastern.info6205.tspsolver.algorithm.annealing;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


//TODO: Change the nodes with mumbai dataset created by giving path
public class SimulatedAnnealingTest {
    @Test
//    @Disabled("This test case needs to be implemented")
    public void testSolveWithEmptyTour() {
//        List<Point> tour = new ArrayList<>();
//        SimulatedAnnealingOptimization annealing = new SimulatedAnnealingOptimization(tour, 1000, 100, 100, 0.1, 0.95);
//        List<Point> result = annealing.solve();
//        assertEquals(tour, result);
    }

    @Test
    @Disabled("This test case needs to be implemented")
    public void testSolveWithSinglePointTour() {

//        SimulatedAnnealing annealing = new SimulatedAnnealing();
//        List<Point> tour = Collections.singletonList(new Point("0", 0, 0));
//        List<Point> result = annealing.solve(tour, 1000, 100, 0.95);
//        assertEquals(tour, result);
    }

    @Test
    @Disabled("This test case needs to be implemented")
    public void testSolveWithMumbaiPointsTour() {
        //src/main/resources/data/tsp-test-mumbai.csv

//        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
////        csvParserService.parsePoints(filePath);
//        SimulatedAnnealingOptimization annealing = new SimulatedAnnealingOptimization();
//        List<Point> tour = Arrays.asList(new Point("0", 0, 0), new Point("1", 1, 1), new Point("2", 2, 2), new Point("3", 3, 3));
//        List<Point> result = annealing.solve(tour, 1000, 100, 0.95);
//        assertEquals(tour, result);
    }
}
