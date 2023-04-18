package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPSolverFactoryService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TSPSolverFactoryServiceImplTest {
    @Test
    public void instanceNotNullTest() {
        TSPSolverFactoryService tspSolverFactoryService = TSPSolverFactoryServiceImpl.getInstance();
        assertNotNull(tspSolverFactoryService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverFactoryService firstInstance = TSPSolverFactoryServiceImpl.getInstance();
        TSPSolverFactoryService secondInstance = TSPSolverFactoryServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

}
