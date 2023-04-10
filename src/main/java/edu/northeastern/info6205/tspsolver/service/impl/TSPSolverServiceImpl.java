package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.harshil.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.JspritTSPSolverService;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

@Service
public class TSPSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPSolverServiceImpl.class);

	@Autowired
	private JspritTSPSolverService jspritTSPSolverService;
	
	@Autowired
	private PerfectMatchingSolverService perfectMatchingSolverService;
	
	@Override
	public void solveAsync(List<Point> points, int startingPointIndex) {
		LOGGER.info(
				"TSPSolverService will solve asynchronosly for points size: {}, startingPointIndex: {}", 
				points.size(),
				startingPointIndex);
		
		Runnable runnable = () -> {
			PrimsMST primsMST = new PrimsMST(points);
			primsMST.solve();
			Edge[] edges = primsMST.getMst();
			
			/*
			 * 
			 * TOO verbose
			for(Edge edge : edges) {
				if(edge == null) {
					// TODO THis should not even print!!
					// Keeping it here for now, so that can debug later
					LOGGER.trace("NULL EDGE");
				} else {
					LOGGER.trace(
							"{}, to {} Distance: {}",
							edge.from != null ? edge.from.getId() : "NULL",
							edge.to != null ? edge.to.getId() : "NULL",
							edge.distance);	
				}
			}
			*/
			
			LOGGER.info("MST Cost: {}", primsMST.getMstCost());
			
			List<Edge> edgeList = Arrays.asList(edges);
			List<Point> oddDegreePoints = findOddDegreeVertices(points, edgeList);
			
			List<Edge> newEdges = perfectMatchingSolverService.kolmogorovMatching(oddDegreePoints);
			LOGGER.trace("newEdges size: {}", newEdges.size());
			
			double totalCost = newEdges.stream()
                    .mapToDouble(Edge::getDistance)
                    .sum();
			LOGGER.trace("totalCost: {}", totalCost);
		};
		
		
		/*
		Runnable runnable = () -> {
			List<Point> tspPoints = jspritTSPSolverService.getTSPTour(points, startingPointIndex);
			// TODO Need to publish in web socket for visualisation
		};
		**/
		

		new Thread(runnable).start();
	}
	
	private List<Point> findOddDegreeVertices(List<Point> points, List<Edge> edges) {
	    Map<Point, Integer> vertexDegrees = new HashMap<>();
	    for (Point point : points) {
	        vertexDegrees.put(point, 0);
	    }

	    for (Edge edge : edges) {
	        vertexDegrees.put(edge.from, vertexDegrees.get(edge.from) + 1);
	        vertexDegrees.put(edge.to, vertexDegrees.get(edge.to) + 1);
	    }

	    // Find all odd degree vertices
	    List<Point> oddDegreeVertices = new ArrayList<>();
	    for (Map.Entry<Point, Integer> entry : vertexDegrees.entrySet()) {
	        if (entry.getValue() % 2 == 1) {
	            oddDegreeVertices.add(entry.getKey());
	        }
	    }

	    return oddDegreeVertices;
	}

}
