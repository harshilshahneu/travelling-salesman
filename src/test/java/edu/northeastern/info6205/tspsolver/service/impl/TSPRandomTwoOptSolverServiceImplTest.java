package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TSPRandomTwoOptSolverServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspRandomTwoOptSolverService = TSPRandomTwoOptSolverServiceImpl.getInstance();
        assertNotNull(tspRandomTwoOptSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPRandomTwoOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPRandomTwoOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
}
