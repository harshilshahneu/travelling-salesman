package edu.northeastern.info6205.tspsolver.service;

import edu.northeastern.info6205.tspsolver.service.impl.TSPServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TSPServiceTest {
	
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
