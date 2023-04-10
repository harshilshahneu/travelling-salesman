package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedMatching;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.alg.matching.blossom.v5.ObjectiveSense;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.util.SupplierUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.harshil.HaversineDistance;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;

@Service
public class PerfectMatchingSolverServiceImpl implements PerfectMatchingSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerfectMatchingSolverServiceImpl.class);

	@Autowired
	private MapService mapService;
	
	@Override
	public List<Edge> edmondAlgorithm(List<Point> points) {
		LOGGER.trace("solving edmond algorithms for points size: {}", points.size());
	    int n = points.size();
	    double[][] matrix = new double[n][n];
	    for (int i = 0; i < points.size(); i++) {
	    	Point source = points.get(i);
	    	for (int j = 0; j < points.size(); j++) {
	    		Point destination = points.get(j);
	    		matrix[i][j] = HaversineDistance.haversine(source, destination);	
	    	}
	    }

	    MinimumWeightPerfectMatching matching = new MinimumWeightPerfectMatching(matrix);
	    matching.solve();
	    int[] path = matching.getMinWeightCostMatching();

	    List<Edge> result = new ArrayList<>();

	    for (int i = 0; i < path.length / 2; i++) {
			int sourceIndex = path[2 * i];
			int destinationIndex = path[2 * i + 1];
			Point source = points.get(sourceIndex);
			Point destination = points.get(destinationIndex);
			
//			LOGGER.trace("{} <-> {}", source.getId(), destination.getId());
			
			Edge edge = new Edge(source, destination);
			result.add(edge);
		}
	    
	    return result;
	}

	@Override
	public List<Edge> kolmogorovMatching(List<Point> points) {
		LOGGER.trace("solving kolmogorovMatching algorithms for points size: {}", points.size());
		
		Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(
				SupplierUtil.createStringSupplier(), 
				SupplierUtil.createDefaultWeightedEdgeSupplier());

		Map<String, Point> map = new HashMap<>();
		
		for (Point point : points) {
			graph.addVertex(point.getId());
			map.put(point.getId(), point);
		}

		/*
		// Kept here for debuggig, should not be kept logs or else will get verbose
		Set<String> vertexSet = graph.vertexSet();
		LOGGER.trace("vertexSet size: {}", vertexSet.size());
		for (String vertex : vertexSet) {
			LOGGER.trace("vertex: {}", vertex);
		}
		*/
		
		for (int i = 0; i < points.size(); i++) {
			Point source = points.get(i);
//			LOGGER.trace("source: {}", source.getId());

			for (int j = i + 1; j < points.size(); j++) {
				Point destination = points.get(j);
//				LOGGER.trace("destination: {}", destination.getId());
				
				double distance = HaversineDistance.haversine(source, destination);
				
				DefaultWeightedEdge edge = graph.addEdge(source.getId(), destination.getId());
//				LOGGER.trace("added edge: {}", edge);

				graph.setEdgeWeight(edge, distance);
//				graph.setEdgeWeight(edge, -1 * distance);
			}
		}

		MatchingAlgorithm<String, DefaultWeightedEdge> algorithm = new KolmogorovWeightedPerfectMatching<>(graph);
		
	    MatchingAlgorithm.Matching<String, DefaultWeightedEdge> matching = algorithm.getMatching();
	    Set<DefaultWeightedEdge> edges = matching.getEdges();
	    matching.getWeight();
	    LOGGER.trace("edges size: {}", edges.size());

	    // This should never happen, logging for debugging
	    if (edges.size() * 2 != points.size()) {
	    	LOGGER.error(
	    			"Looks like some issue in kolmogorovMatching, edges size: {}, points size: {}",
	    			edges.size(),
	    			points.size());
	    	
	    	Set<String> pointIds = new HashSet<>();
	    	for (Point point : points) {
	    		pointIds.add(point.getId());
	    	}
	    	
	    	for (DefaultWeightedEdge edge : edges) {
	    		String source = graph.getEdgeSource(edge);
				String destination = graph.getEdgeTarget(edge);
				
				pointIds.remove(source);
				pointIds.remove(destination);
	    	}
	    	
	    	List<Point> discardedPoints = new ArrayList<>();
	    	for (String id : pointIds) {
	    		Point point = map.get(id);
	    		discardedPoints.add(point);
	    	}
	    	
    		LOGGER.error("discarded points size: {}", discardedPoints.size());
	    	for (Point point : discardedPoints) {
	    		LOGGER.error("point discarded: {}", point);
	    		mapService.publishChangePointColorRed(point.getId());
	    	}
	    }
	    
	    Set<Point> publishedPoints = new HashSet<>();
	    
	    List<Edge> result = new ArrayList<>();
		for (DefaultWeightedEdge edge : edges) {
//			LOGGER.trace(edge.toString());
			
			String source = graph.getEdgeSource(edge);
			String destination = graph.getEdgeTarget(edge);
			
			Point sourcePoint = map.get(source);
			if (publishedPoints.add(sourcePoint)) {
				mapService.publishChangePointColorGreen(sourcePoint.getId());
			}
			
			Point destinationPoint = map.get(destination);
			if (publishedPoints.add(destinationPoint)) {
				mapService.publishChangePointColorGreen(destinationPoint.getId());
			}

			Edge path = new Edge(sourcePoint, destinationPoint);
			result.add(path);
			mapService.publishAddGreenLine(path);
		}
        
		return result;
	}
}
