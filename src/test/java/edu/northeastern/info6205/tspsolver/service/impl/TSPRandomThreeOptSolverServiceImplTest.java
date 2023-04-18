package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TSPRandomThreeOptSolverServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspRandomThreeOptSolverService = TSPRandomThreeOptSolverServiceImpl.getInstance();
        assertNotNull(tspRandomThreeOptSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPRandomThreeOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPRandomThreeOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
}
