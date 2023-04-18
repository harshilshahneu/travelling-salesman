package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
