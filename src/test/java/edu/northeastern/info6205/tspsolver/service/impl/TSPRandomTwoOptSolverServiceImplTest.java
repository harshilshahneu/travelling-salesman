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
import edu.northeastern.info6205.tspsolver.model.TSPPayload.TwoOptPayload;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

public class TSPRandomTwoOptSolverServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspRandomTwoOptSolverService = TSPRandomTwoOptSolverServiceImpl.getInstance();
        assertNotNull(tspRandomTwoOptSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPRandomTwoOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPRandomTwoOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void getKeyIdentifierTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_RANDOM_TWO_OPT);
    }
    
    @Test
    public void getNameTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_RANDOM_TWO_OPT);
    }
    
    @Test
    public void pointsNullTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }

    @Test
    public void payloadNullTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void pointsEmptyTest() {
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, payload);
    	});
    }
    
    @Test
    public void smallDataStrategy1Test() {
    	dataTest(Constant.TEST_DATA_FILE_SMALL, 1);
    }
    
    @Test
    public void smallDataStrategy2Test() {
    	dataTest(Constant.TEST_DATA_FILE_SMALL, 2);
    }
    
    @Test
    public void smallDataStrategy3Test() {
    	dataTest(Constant.TEST_DATA_FILE_SMALL, 3);
    }
    
    @Test
    public void bigDataTestStrategy1Test() {
    	dataTest(Constant.TEST_DATA_FILE_BIG, 1);
    }
    
    @Test
    public void bigDataTestStrategy2Test() {
    	dataTest(Constant.TEST_DATA_FILE_BIG, 2);
    }
    
    @Test
    public void bigDataTestStrategy3Test() {
    	dataTest(Constant.TEST_DATA_FILE_BIG, 3);
    }
    
    private void dataTest(String dataFileName, int strategy) {
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(dataFileName);
		
    	TSPPayload payload = new TSPPayload();
    	
    	TwoOptPayload twoOptPayload = new TwoOptPayload();
    	twoOptPayload.setStrategy(strategy);
    	twoOptPayload.setBudget(10000);
    	payload.setTwoOptPayload(twoOptPayload);
    	
    	TSPSolverService instance = TSPRandomTwoOptSolverServiceImpl.getInstance();
    	List<Point> tspTour = instance.solve(points, 0, payload);
        double tspTourCost = PointUtil.getTotalCost(tspTour);

    	PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();
        
        double percentage = ((tspTourCost - mstCost)/mstCost) * 100;
        assertTrue(percentage < 100);
	}
}
