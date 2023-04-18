package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NearestNeighbourPerfectMatchingImplTest {
    @Test
    public void testGetInstance() {
        PerfectMatchingSolverService perfectMatchingSolverService = NearestNeighbourPerfectMatchingImpl.getInstance();
        assertNotNull(perfectMatchingSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        PerfectMatchingSolverService firstInstance = NearestNeighbourPerfectMatchingImpl.getInstance();
        PerfectMatchingSolverService secondInstance = NearestNeighbourPerfectMatchingImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testGetMinimumWeightPerfectMatchingTwoNodes() {
        PerfectMatchingSolverService perfectMatchingSolverService = NearestNeighbourPerfectMatchingImpl.getInstance();

        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("0", 0, 0));
        nodes.add(new Point("1", 1, 1));

        List<Edge> result = new ArrayList<>();
        result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        assertEquals(1, result.size());
    }

}
