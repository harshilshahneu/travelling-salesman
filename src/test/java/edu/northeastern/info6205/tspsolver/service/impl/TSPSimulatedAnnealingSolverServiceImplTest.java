package edu.northeastern.info6205.tspsolver.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.SimulatedAnnealingPayload;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

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
    
    @Test
    public void getKeyIdentifierTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING);
    }
    
    @Test
    public void getNameTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_SIMULATED_ANNEALING);
    }
    
    @Test
    public void pointsNullTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }

    @Test
    public void payloadNullTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void pointsEmptyTest() {
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
	
    @Test
    public void smallDataTest() {
    	dataTest(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void bigDataTest() {
    	dataTest(Constant.TEST_DATA_FILE_BIG);
    }

	private void dataTest(String dataFileName) {
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(dataFileName);
		
    	TSPPayload payload = new TSPPayload();
    	
    	SimulatedAnnealingPayload annealingPayload = new SimulatedAnnealingPayload();
    	annealingPayload.setMaxIteration(1000000);
    	annealingPayload.setStartingTemperature(1000);
    	annealingPayload.setFinalTemperature(1);
    	annealingPayload.setCoolingRate(0.9995);
    	payload.setSimulatedAnnealingPayload(annealingPayload);
    	
    	TSPSolverService instance = TSPSimulatedAnnealingSolverServiceImpl.getInstance();
    	List<Point> tspTour = instance.solve(points, 0, payload);
        double tspTourCost = PointUtil.getTotalCost(tspTour);

    	PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();
        
        double percentage = ((tspTourCost - mstCost)/mstCost) * 100;
        assertTrue(percentage < 100);
	}
}
