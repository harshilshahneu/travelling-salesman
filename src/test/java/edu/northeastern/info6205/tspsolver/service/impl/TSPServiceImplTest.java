package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.service.TSPService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

public class TSPServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPService TSPService = TSPServiceImpl.getInstance();
        assertNotNull(TSPService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPService firstInstance = TSPServiceImpl.getInstance();
        TSPService secondInstance = TSPServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
}
