package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.harshil.HaversineDistance;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;

@Service
public class PerfectMatchingSolverServiceImpl implements PerfectMatchingSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerfectMatchingSolverServiceImpl.class);

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
}
