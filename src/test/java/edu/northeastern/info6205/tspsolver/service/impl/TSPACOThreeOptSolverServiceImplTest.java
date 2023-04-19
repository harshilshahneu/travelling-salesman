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
import edu.northeastern.info6205.tspsolver.model.TSPPayload.AntColonyOptimazationPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.ThreeOptPayload;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

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
    
    @Test
    public void getKeyIdentifierTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_ANT_COLONY_THREE_OPT);
    }
    
    @Test
    public void getNameTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_ANT_COLONY_THREE_OPT);
    }
    
    @Test
    public void pointsNullTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }

    @Test
    public void payloadNullTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void pointsEmptyTest() {
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
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
    	
    	ThreeOptPayload threeOptPayload = new ThreeOptPayload();
    	threeOptPayload.setStrategy(1);
    	threeOptPayload.setBudget(10000);
    	payload.setThreeOptPayload(threeOptPayload);
    	
    	AntColonyOptimazationPayload antColonyOptimazationPayload = new AntColonyOptimazationPayload();
    	antColonyOptimazationPayload.setNumberOfAnts(10);
		antColonyOptimazationPayload.setPhermoneExponent(1);
		antColonyOptimazationPayload.setHeuristicExponent(2);
		antColonyOptimazationPayload.setPhermoneEvaporationRate(0.1);
		antColonyOptimazationPayload.setPhermoneDepositFactor(1);
		antColonyOptimazationPayload.setNumberOfIterations(20);
		antColonyOptimazationPayload.setMaxImprovementIterations(1000);
    	payload.setAntColonyOptimazationPayload(antColonyOptimazationPayload);
    	
    	TSPSolverService instance = TSPACOThreeOptSolverServiceImpl.getInstance();
    	List<Point> tspTour = instance.solve(points, 0, payload);
        double tspTourCost = PointUtil.getTotalCost(tspTour);

    	PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();
        
        double percentage = ((tspTourCost - mstCost)/mstCost) * 100;
        assertTrue(percentage < 100);
	}
    
}
