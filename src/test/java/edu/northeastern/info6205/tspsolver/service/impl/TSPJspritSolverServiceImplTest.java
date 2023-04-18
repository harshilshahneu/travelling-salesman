package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TSPJspritSolverServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspJspritSolverService = TSPJspritSolverServiceImpl.getInstance();
        assertNotNull(tspJspritSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPJspritSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPJspritSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
}
