package edu.northeastern.info6205.tspsolver.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.service.impl.TSPACOThreeOptSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPAntColonyOptimzationSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPChristofidesSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPRandomThreeOptSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPRandomTwoOptSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPSimulatedAnnealingSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPSolverFactoryServiceImpl;

public class TSPSolverFactoryServiceTest {
	
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
    
    @Test
    public void nullIdentifierTest() {
    	TSPSolverFactoryService service = TSPSolverFactoryServiceImpl.getInstance();
    	assertNull(service.getService(null));
    }
    
    @Test
    public void invalidIdentifierTest() {
    	TSPSolverFactoryService service = TSPSolverFactoryServiceImpl.getInstance();
    	assertNull(service.getService(Constant.BLANK_STRING));
    }
    
    @Test
    public void christofidesTest() {
    	implementationTest(
    			Constant.KEY_IDENTIFIER_CHRISTOFIDES, 
    			Constant.NAME_CHRISTOFIDES, 
    			TSPChristofidesSolverServiceImpl.class);
    }
    
    @Test
    public void twoOptTest() {
    	implementationTest(
    			Constant.KEY_IDENTIFIER_RANDOM_TWO_OPT, 
    			Constant.NAME_RANDOM_TWO_OPT, 
    			TSPRandomTwoOptSolverServiceImpl.class);
    }
    
    @Test
    public void threeOptTest() {
    	implementationTest(
    			Constant.KEY_IDENTIFIER_RANDOM_THREE_OPT, 
    			Constant.NAME_RANDOM_THREE_OPT, 
    			TSPRandomThreeOptSolverServiceImpl.class);
    }
    
    @Test
    public void simulatedAnnealingTest() {
    	implementationTest(
    			Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING, 
    			Constant.NAME_SIMULATED_ANNEALING, 
    			TSPSimulatedAnnealingSolverServiceImpl.class);
    }
    
    @Test
    public void antColonyTest() {
    	implementationTest(
    			Constant.KEY_IDENTIFIER_ANT_COLONY, 
    			Constant.NAME_ANT_COLONY, 
    			TSPAntColonyOptimzationSolverServiceImpl.class);
    }
    
    @Test
    public void antColonyThreeOptTest() {
    	implementationTest(
    			Constant.KEY_IDENTIFIER_ANT_COLONY_THREE_OPT, 
    			Constant.NAME_ANT_COLONY_THREE_OPT, 
    			TSPACOThreeOptSolverServiceImpl.class);
    }
    
    private void implementationTest(
    		String keyIdentifier,
    		String name,
    		Class<? extends TSPSolverService> serviceClass) {
    	TSPSolverFactoryService service = TSPSolverFactoryServiceImpl.getInstance();
    	TSPSolverService solverService = service.getService(keyIdentifier);
    	
    	assertEquals(solverService.getClass(), serviceClass);
    	assertEquals(solverService.getName(), name);
    	assertEquals(solverService.getKeyIdentifier(), keyIdentifier);
    }

}
