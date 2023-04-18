package edu.northeastern.info6205.tspsolver.service;

import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.impl.KolmogorovWeightedPerfectMatchingImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PerfectMatchingSolverServiceTest {
	
    @Test
    public void instanceNotNullTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        assertNotNull(perfectMatchingSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        PerfectMatchingSolverService firstInstance = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        PerfectMatchingSolverService secondInstance = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testGetMinimumWeightPerfectMatchingWithLoop() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();

        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 0));
        nodes.add(new Point("1", 0, 1));
        nodes.add(new Point("2", 1, 1));
        nodes.add(new Point("3", 1, 0));
        nodes.add(new Point("4", 0, 0));

        assertThrows(IllegalArgumentException.class, () -> {
            List<Edge> result = new ArrayList<>();
            result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        });
    }

    @Test
    public void testGetMinimumWeightPerfectMatchingTwoNodes() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();

        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 0));
        nodes.add(new Point("1", 1, 1));

        List<Edge> result = new ArrayList<>();
        result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetMinimumWeightPerfectMatchingWithOddNodes() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();

        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 0));
        nodes.add(new Point("1", 1, 1));
        nodes.add(new Point("2", 1, 1));

        assertThrows(IllegalArgumentException.class, () -> {
            List<Edge> result = new ArrayList<>();
            result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        });
    }

    //TODO: change this test case to check unique node
    @Test
    public void testGetMinimumWeightPerfectMatching() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 1));
        nodes.add(new Point("1", 1, 0));
        nodes.add(new Point("2", 3, 0));
        nodes.add(new Point("3", 0, 3));

        List<Edge> result;
        result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);

        List<Edge> expectedResult = new ArrayList<>();
        expectedResult.add(new Edge(new Point("0", 0, 1), new Point("3", 0, 3)));
        expectedResult.add(new Edge(new Point("1", 1, 0), new Point("2", 3, 0)));

        assertTrue(isEqualArrayByValues(result, expectedResult));

    }

    private static boolean isEqualArrayByValues(List<Edge> result, List<Edge> expectedTour) {
        boolean equal = true;
        if (expectedTour.size() != result.size()) {
            equal = false;
        } else {
            for (int i = 0; i < expectedTour.size(); i++) {
                if (!expectedTour.get(i).equals(result.get(i))) {
                    equal = false;
                    break;
                }
            }
        }
        return equal;
    }

}
