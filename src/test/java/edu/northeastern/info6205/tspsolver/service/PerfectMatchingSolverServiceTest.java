package edu.northeastern.info6205.tspsolver.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.service.impl.KolmogorovWeightedPerfectMatchingImpl;
import edu.northeastern.info6205.tspsolver.service.impl.NearestNeighbourPerfectMatchingImpl;
import edu.northeastern.info6205.tspsolver.util.EdgeUtil;

public class PerfectMatchingSolverServiceTest {
	
	@Test
    public void nearestNeighbourInstanceNotNullTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = NearestNeighbourPerfectMatchingImpl.getInstance();
        assertNotNull(perfectMatchingSolverService);
    }
	
	@Test
    public void kolmogorovInstanceNotNullTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        assertNotNull(perfectMatchingSolverService);
    }

    @Test
    public void nearestNeighbourSingletonInstanceTest() {
        PerfectMatchingSolverService firstInstance = NearestNeighbourPerfectMatchingImpl.getInstance();
        PerfectMatchingSolverService secondInstance = NearestNeighbourPerfectMatchingImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void kolmogorovSingletonInstanceTest() {
        PerfectMatchingSolverService firstInstance = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        PerfectMatchingSolverService secondInstance = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void nearestNeighbourEmptyListTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = NearestNeighbourPerfectMatchingImpl.getInstance();
    	List<Point> nodes = new ArrayList<>();
    	List<Edge> result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
    	assertTrue(result.isEmpty());
    }
    
    @Test
    public void nearestNeighbourSingleNodeNullPointerExceptionTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = NearestNeighbourPerfectMatchingImpl.getInstance();

        Point point = new Point(Constant.BLANK_STRING, 0, 0);

        List<Point> nodes = new ArrayList<>();
        nodes.add(point);

        assertThrows(NullPointerException.class, () -> {
            perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        });
    }
    
    @Test
    public void kolmogorovSingleNodeIllegalArgumentExceptionTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();

        Point point = new Point(Constant.BLANK_STRING, 0, 0);

        List<Point> nodes = new ArrayList<>();
        nodes.add(point);

        assertThrows(IllegalArgumentException.class, () -> {
            perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        });
    }
    
    @Test
    public void nearestNeighbourTwoNodesTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = NearestNeighbourPerfectMatchingImpl.getInstance();

        Point source = new Point(String.valueOf(1), 0, 0);
        Point destination = new Point(String.valueOf(2), 1, 1);
        
        Edge expectedEdge = new Edge(source, destination);
        List<Edge> expectedList = new ArrayList<>();
        expectedList.add(expectedEdge);
        
        List<Point> nodes = new ArrayList<>();
        nodes.add(source);
        nodes.add(destination);

        List<Edge> result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        assertEquals(expectedList, result);
    }
    
    @Test
    public void kolmogorovTwoNodesTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();

        Point source = new Point(String.valueOf(1), 0, 0);
        Point destination = new Point(String.valueOf(2), 1, 1);
        
        Edge expectedEdge = new Edge(source, destination);
        List<Edge> expectedList = new ArrayList<>();
        expectedList.add(expectedEdge);
        
        List<Point> nodes = new ArrayList<>();
        nodes.add(source);
        nodes.add(destination);

        List<Edge> result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        assertEquals(expectedList, result);
    }
    
    @Test
    public void nearestNeighbourEmptyDataTest() {
    	nearestNeighbourDataTest(Constant.TEST_DATA_FILE_EMPTY, 0);
    }
    
    @Test
    public void kolmogorovEmptyDataTest() {
    	kolmogorovDataTest(Constant.TEST_DATA_FILE_EMPTY, 0);
    }
    
    @Test
    public void nearestNeighbourSmallDataTest() {
    	nearestNeighbourDataTest(Constant.TEST_DATA_FILE_SMALL, 37817.4426166655);
    }
    
    @Test
    public void kolmogorovSmallDataTest() {
    	kolmogorovDataTest(Constant.TEST_DATA_FILE_SMALL, 21248.089360342838);
    }
    
    @Test
    public void nearestNeighbourBigDataTest() {
    	nearestNeighbourDataTest(Constant.TEST_DATA_FILE_BIG, 229734.53644178226);
    }

    @Test
    public void kolmogorovBigDataTest() {
    	kolmogorovDataTest(Constant.TEST_DATA_FILE_BIG, 146168.36186616763);
    }
    
	private void nearestNeighbourDataTest(String filePath, double expectedCost) {
		dataTest(
				filePath, 
				expectedCost, 
				NearestNeighbourPerfectMatchingImpl.getInstance());
	}
	
    private void kolmogorovDataTest(String filePath, double expectedCost) {
		dataTest(
				filePath, 
				expectedCost, 
				KolmogorovWeightedPerfectMatchingImpl.getInstance());
	}

    private void dataTest(
    		String filePath, 
    		double expectedCost,
    		PerfectMatchingSolverService perfectMatchingSolverService) {
    	CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
		List<Point> points = csvParserService.parsePoints(filePath);
		
		if (points.size() % 2 == 1) {
			points.remove(0);
		}
		
		List<Edge> result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(points);
		
		assertEquals(points.size(), result.size() * 2);
		
		double actualCost = EdgeUtil.getTotalCost(result);
		assertEquals(actualCost, expectedCost, 0);
    }
}
