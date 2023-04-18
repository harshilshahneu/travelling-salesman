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
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

public class TSPChristofidesSolverServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspChristofidesSolverService = TSPChristofidesSolverServiceImpl.getInstance();
        assertNotNull(tspChristofidesSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPChristofidesSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPChristofidesSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void getKeyIdentifierTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_CHRISTOFIDES);
    }
    
    @Test
    public void getNameTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_CHRISTOFIDES);
    }
    
    @Test
    public void pointsNullTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }

    @Test
    public void payloadNullTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void pointsEmptyTest() {
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
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
    	
    	TSPSolverService instance = TSPChristofidesSolverServiceImpl.getInstance();
    	List<Point> tspTour = instance.solve(points, 0, payload);
        double tspTourCost = PointUtil.getTotalCost(tspTour);

    	PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();
        
        double percentage = ((tspTourCost - mstCost)/mstCost) * 100;
        assertTrue(percentage < 100);
	}
}
