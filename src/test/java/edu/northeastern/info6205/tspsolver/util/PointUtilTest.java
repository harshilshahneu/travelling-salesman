package edu.northeastern.info6205.tspsolver.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;

public class PointUtilTest {

	@Test
	public void nullTest() {
		assertThrows(NullPointerException.class, () -> {
			PointUtil.getTotalCost(null);
		});
	}
	
	@Test
	public void emptyListZeroCostTest() {
		double cost = PointUtil.getTotalCost(Collections.emptyList());
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
		
		double cost = PointUtil.getTotalCost(points);
		assertEquals(cost, expectedCost);
	}
	
}
