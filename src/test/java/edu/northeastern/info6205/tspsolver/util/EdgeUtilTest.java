package edu.northeastern.info6205.tspsolver.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;

public class EdgeUtilTest {

	@Test
	public void nullTest() {
		assertThrows(NullPointerException.class, () -> {
			EdgeUtil.getTotalCost(null);
		});
	}
	
	@Test
	public void emptyListZeroCostTest() {
		double cost = EdgeUtil.getTotalCost(Collections.emptyList());
		assertEquals(cost, 0);
	}
	
	@Test
	public void emptyDataTest() {
		calculateCostTest(Constant.TEST_DATA_FILE_EMPTY, 0);
	}
	
	@Test
	public void smallDataTest() {
		calculateCostTest(Constant.TEST_DATA_FILE_SMALL, 71597.66003606733);
	}
	
	@Test
	public void bigDataTest() {
		calculateCostTest(Constant.TEST_DATA_FILE_BIG, 2434177.198993038);
	}
	
	private void calculateCostTest(String fileName, double expectedCost) {
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
		List<Point> points = csvParserService.parsePoints(fileName);
		
		List<Edge> edges = new ArrayList<>();
		for (int i = 0; i < points.size() - 1; i++) {
			Point source = points.get(i);
			Point destination = points.get(i + 1);
			Edge edge = new Edge(source, destination);
			edges.add(edge);
		}
		
		double cost = EdgeUtil.getTotalCost(edges);
		assertEquals(cost, expectedCost);
	}
}
