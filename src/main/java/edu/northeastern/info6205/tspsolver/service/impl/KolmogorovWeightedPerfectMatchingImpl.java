package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.jgrapht.Graph;
import edu.northeastern.info6205.tspsolver.jgrapht.alg.interfaces.MatchingAlgorithm;
import edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.DefaultWeightedEdge;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.SimpleWeightedGraph;
import edu.northeastern.info6205.tspsolver.jgrapht.util.SupplierUtil;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;

public class KolmogorovWeightedPerfectMatchingImpl implements PerfectMatchingSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(KolmogorovWeightedPerfectMatchingImpl.class);

	private static PerfectMatchingSolverService instance;
	
	private KolmogorovWeightedPerfectMatchingImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static PerfectMatchingSolverService getInstance() {
		if (instance == null) {
			instance = new KolmogorovWeightedPerfectMatchingImpl();
		}
		
		return instance;
	}
	
	@Override
	public List<Edge> getMinimumWeightPerfectMatching(List<Point> points) {
		LOGGER.trace("solving kolmogorovMatching algorithms for points size: {}", points.size());
		
		MapService mapService = MapServiceImpl.getInstance();
		
		Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(
				SupplierUtil.createStringSupplier(),
				SupplierUtil.createDefaultWeightedEdgeSupplier());

		Map<String, Point> map = new HashMap<>();
		
		for (Point point : points) {
			graph.addVertex(point.getId());
			map.put(point.getId(), point);
		}

		/*
		// Kept here for debugging, should not be kept logs or else will get verbose
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
				
				double distance = HaversineDistanceUtil.haversine(source, destination);
				
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
//				mapService.publishChangePointColorGreen(sourcePoint.getId());
			}
			
			Point destinationPoint = map.get(destination);
			if (publishedPoints.add(destinationPoint)) {
//				mapService.publishChangePointColorGreen(destinationPoint.getId());
			}

			Edge path = new Edge(sourcePoint, destinationPoint);
			result.add(path);
//			mapService.publishAddGreenLine(path);
		}
        
		return result;
	}
}
