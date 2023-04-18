package edu.northeastern.info6205.tspsolver.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.util.EdgeUtil;

public class KolmogorovWeightedPerfectMatchingImplTest {
	
    @Test
    public void instanceNotNullTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        assertNotNull(perfectMatchingSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        PerfectMatchingSolverService firstInstance = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        PerfectMatchingSolverService secondInstance = KolmogorovWeightedPerfectMatchingImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void singleNodeIllegalArgumentExceptionTest() {
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();

        Point point = new Point(Constant.BLANK_STRING, 0, 0);

        List<Point> nodes = new ArrayList<>();
        nodes.add(point);

        assertThrows(IllegalArgumentException.class, () -> {
            perfectMatchingSolverService.getMinimumWeightPerfectMatching(nodes);
        });
    }
    
    @Test
    public void twoNodesTest() {
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
    public void emptyDataTest() {
    	dataTest(Constant.TEST_DATA_FILE_EMPTY, 0);
    }
    
    @Test
    public void smallDataTest() {
    	dataTest(Constant.TEST_DATA_FILE_SMALL, 21248.089360342838);
    }
    
    @Test
    public void bigDataTest() {
    	dataTest(Constant.TEST_DATA_FILE_BIG, 146168.36186616763);
    }
    
    private void dataTest(String filePath, double expectedCost) {
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
		List<Point> points = csvParserService.parsePoints(filePath);
		
		if (points.size() % 2 == 1) {
			points.remove(0);
		}
		
        PerfectMatchingSolverService perfectMatchingSolverService = KolmogorovWeightedPerfectMatchingImpl.getInstance();
		List<Edge> result = perfectMatchingSolverService.getMinimumWeightPerfectMatching(points);
		
		assertEquals(points.size(), result.size() * 2);
		
		double actualCost = EdgeUtil.getTotalCost(result);
		assertEquals(actualCost, expectedCost, 0);
	}

}
