package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.ThreeOptPayload;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TSPRandomThreeOptSolverServiceImplTest {
	
    @Test
    public void instanceNotNullTest() {
        TSPSolverService tspRandomThreeOptSolverService = TSPRandomThreeOptSolverServiceImpl.getInstance();
        assertNotNull(tspRandomThreeOptSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TSPSolverService firstInstance = TSPRandomThreeOptSolverServiceImpl.getInstance();
        TSPSolverService secondInstance = TSPRandomThreeOptSolverServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void getKeyIdentifierTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getKeyIdentifier(), Constant.KEY_IDENTIFIER_RANDOM_THREE_OPT);
    }
    
    @Test
    public void getNameTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	assertEquals(instance.getName(), Constant.NAME_RANDOM_THREE_OPT);
    }
    
    @Test
    public void pointsNullTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	TSPPayload payload = new TSPPayload();
    	
    	assertThrows(NullPointerException.class, () -> {
    		instance.solve(null, 0, payload);
    	});
    }

    @Test
    public void payloadNullTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	
    	assertThrows(NegativeArraySizeException.class, () -> {
    		instance.solve(Collections.emptyList(), 0, null);
    	});
    }
    
    @Test
    public void pointsEmptyTest() {
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
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
    public void smallDataStrategy4Test() {
    	dataTest(Constant.TEST_DATA_FILE_SMALL, 4);
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
    
    @Test
    public void bigDataTestStrategy4Test() {
    	dataTest(Constant.TEST_DATA_FILE_BIG, 4);
    }
    
    private void dataTest(String dataFileName, int strategy) {
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(dataFileName);
		
    	TSPPayload payload = new TSPPayload();
    	
    	ThreeOptPayload threeOptPayload = new ThreeOptPayload();
    	threeOptPayload.setStrategy(strategy);
    	threeOptPayload.setBudget(10000);
    	payload.setThreeOptPayload(threeOptPayload);
    	
    	TSPSolverService instance = TSPRandomThreeOptSolverServiceImpl.getInstance();
    	List<Point> tspTour = instance.solve(points, 0, payload);
        double tspTourCost = PointUtil.getTotalCost(tspTour);

    	PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();
        
        double percentage = ((tspTourCost - mstCost)/mstCost) * 100;
        assertTrue(percentage < 100);
	}
}
