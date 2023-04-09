package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.harshil.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Point;

public class ChristofidesAlgorithm {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVParserServiceImpl.class);

	public static List<Point> apply(
			List<Point> points,
			int startingIndex) {
		LOGGER.info("solving christofides for points size: {}", points.size());
		
		PrimsMST primsMST = new PrimsMST(points);
		primsMST.solve();
		Edge[] edges = primsMST.getMst();
		List<Edge> minimumSpanningTree = Arrays.asList(edges);
		
		Map<String, Integer> pointDegrees = new HashMap<>();
		for (Edge edge : edges) {
			String from  = edge.from.getId();
			String to = edge.to.getId();
			pointDegrees.put(from, pointDegrees.getOrDefault(from, 0) + 1);
			pointDegrees.put(to, pointDegrees.getOrDefault(to, 0) + 1);
		}
		
		Set<String> oddDegreePoints = new HashSet<>();
		for (String point : pointDegrees.keySet()) {
			if (pointDegrees.get(point) % 2 == 1) {
				oddDegreePoints.add(point);
			}
		}
		
		LOGGER.info("Number of odd degree points in the MST: {}", oddDegreePoints.size());
		
		List<Edge> minimumMatching = minimumPerfectMatching(points, oddDegreePoints);
		
		List<Edge> eulerianMultigraph = new ArrayList<>();
		eulerianMultigraph.addAll(minimumSpanningTree);
		eulerianMultigraph.addAll(minimumMatching);
		
		List<Point> circuit = eulerianCircuit(eulerianMultigraph, points.get(startingIndex));
		
		List<Point> eulerianTour = new ArrayList<>();
		for (Point point : circuit) {
			if (eulerianTour.contains(point)) {
				continue;
			}
			
			eulerianTour.add(point);
		}
		
		return eulerianTour;
	}

	private static List<Point> eulerianCircuit(List<Edge> eulerianMultigraph, Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<Edge> minimumPerfectMatching(
			List<Point> points, 
			Set<String> oddDegreePoints) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
