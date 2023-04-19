package edu.northeastern.info6205.tspsolver.algorithm.antcolony;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.algorithm.christofides.Christofides;
import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.AntColonyOptimazationPayload;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

public class AntColonyOptimizationTest {

	@Test
    public void graphNullTest() {
    	assertThrows(NullPointerException.class, () -> {
    		new AntColonyOptimization(
        			null, 
        			new int[] {1}, 
        			0, 
        			0, 
        			0, 
        			0, 
        			0, 
        			0,
        			0);
    	});
    }
    
    @Test
    public void christofidesTourNullTest() {
    	assertThrows(NullPointerException.class, () -> {
    		new AntColonyOptimization(
        			new double[][] {{ 1 }}, 
        			null, 
        			0, 
        			0, 
        			0, 
        			0, 
        			0, 
        			0,
        			0);
    	});
    }
    
    @Test
    public void graphAndChristofidesTourNullTest() {
    	assertThrows(NullPointerException.class, () -> {
    		new AntColonyOptimization(
        			null, 
        			null, 
        			0, 
        			0, 
        			0, 
        			0, 
        			0, 
        			0,
        			0);
    	});
    }
    
    @Test
    public void runSmallOptimizationTest() {
    	test(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void runBigOptimizationTest() {
    	test(Constant.TEST_DATA_FILE_BIG);
    }
    
    private void test(String fileName) {
    	CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(fileName);
    	
    	Map<Integer, Point> pointMap = new HashMap<>();
		for (Point point : points) {
			pointMap.put(Integer.parseInt(point.getId()), point);
		}
    	
    	Christofides christofides = new Christofides(points);
		List<Point> tour = christofides.solve();
		
		// Last point and first point are same in Christofides tour
		tour.remove(tour.size() - 1);
		
		int[] christofidesTour = tour.stream()
				.mapToInt(p -> Integer.parseInt(p.getId()))
				.toArray();
		
		int n = points.size();
		double[][] graph = new double[n][n];
		for (int i = 0; i < n; i++) {
			Point source = points.get(i);
			for (int j = i + 1; j < n; j++) {
				Point destination = points.get(j);
				double distance = HaversineDistanceUtil.haversine(destination, source);
				graph[i][j] = distance;
				graph[j][i] = distance;
			}
		}
		
		AntColonyOptimazationPayload antColonyOptimazationPayload = new AntColonyOptimazationPayload();
		antColonyOptimazationPayload.setNumberOfAnts(10);
		antColonyOptimazationPayload.setPhermoneExponent(1);
		antColonyOptimazationPayload.setHeuristicExponent(2);
		antColonyOptimazationPayload.setPhermoneEvaporationRate(0.1);
		antColonyOptimazationPayload.setPhermoneDepositFactor(1);
		antColonyOptimazationPayload.setNumberOfIterations(20);
		antColonyOptimazationPayload.setMaxImprovementIterations(1000);
		
		AntColonyOptimization optimization = new AntColonyOptimization(
				graph, 
				christofidesTour, 
				antColonyOptimazationPayload.getNumberOfAnts(), 
				antColonyOptimazationPayload.getPhermoneExponent(), 
				antColonyOptimazationPayload.getHeuristicExponent(), 
				antColonyOptimazationPayload.getPhermoneEvaporationRate(), 
				antColonyOptimazationPayload.getPhermoneDepositFactor(), 
				antColonyOptimazationPayload.getNumberOfIterations(), 
				antColonyOptimazationPayload.getMaxImprovementIterations());
		
		int[] path = optimization.runACO();
		
		List<Point> tspTour = new ArrayList<>();
		for (int node : path) {
			tspTour.add(pointMap.get(node));
		}
		
		int startingPointIndex = 0;
		Point firstPoint = pointMap.get(startingPointIndex);
		int firstPointIndex = tspTour.indexOf(firstPoint);
		
		if (firstPointIndex != -1) {
			int rotations = firstPointIndex;
			Collections.rotate(tspTour, -rotations);
		}

		tspTour.add(tspTour.get(0));
		
		double tspTourCost = PointUtil.getTotalCost(tspTour);
		
		PrimsMST primsMst = new PrimsMST(points);
        double mstCost = primsMst.getMstCost();
        
        double percentage = ((tspTourCost - mstCost)/mstCost) * 100;
        assertTrue(percentage < 100);
    }
}
