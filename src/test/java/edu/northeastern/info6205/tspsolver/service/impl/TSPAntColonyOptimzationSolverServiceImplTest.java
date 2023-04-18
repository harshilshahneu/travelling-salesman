package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TSPAntColonyOptimzationSolverServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspAntColonyOptimzationSolverService = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
        assertNotNull(tspAntColonyOptimzationSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPAntColonyOptimzationSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
}
