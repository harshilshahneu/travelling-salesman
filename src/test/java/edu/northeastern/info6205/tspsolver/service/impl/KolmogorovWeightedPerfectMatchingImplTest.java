package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KolmogorovWeightedPerfectMatchingImplTest {

    PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();

    @Test
    public void testGetInstance() {
        assertNotNull(perfectMatchingSolverService);
    }

    @Test
    public void testGetMinimumWeightPerfectMatchingWithLoop() {
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
        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 0));
        nodes.add(new Point("1", 1, 1));

        List<Edge> result = new ArrayList<>();
        result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetMinimumWeightPerfectMatchingWithOddNodes() {
        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 0));
        nodes.add(new Point("1", 1, 1));
        nodes.add(new Point("2", 1, 1));

        assertThrows(IllegalArgumentException.class, () -> {
            List<Edge> result = new ArrayList<>();
            result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        });
    }

    @Test
    public void testGetMinimumWeightPerfectMatching() {
        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 0));
        nodes.add(new Point("1", 1, 1));
        nodes.add(new Point("2", 1, 1));



//        assertThrows(IllegalArgumentException.class, () -> {
//            List<Edge> result = new ArrayList<>();
//            result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
//        });
    }

}
