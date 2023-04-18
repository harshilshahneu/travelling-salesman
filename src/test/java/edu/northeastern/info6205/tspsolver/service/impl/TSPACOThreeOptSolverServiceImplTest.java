package edu.northeastern.info6205.tspsolver.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

public class TSPACOThreeOptSolverServiceImplTest {
    
	@Test
    public void instanceNotNullTest() {
        TSPSolverService tspACOThreeOptSolverService = TSPACOThreeOptSolverServiceImpl.getInstance();
        assertNotNull(tspACOThreeOptSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPACOThreeOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPACOThreeOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
	
}
