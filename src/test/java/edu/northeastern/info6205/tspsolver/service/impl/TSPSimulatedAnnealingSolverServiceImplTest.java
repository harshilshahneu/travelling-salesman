package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TSPSimulatedAnnealingSolverServiceImplTest {
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspSimulatedAnnealingSolverService = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
        assertNotNull(tspSimulatedAnnealingSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
}
