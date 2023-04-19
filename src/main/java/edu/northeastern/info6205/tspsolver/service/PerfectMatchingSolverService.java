package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service to solve the Perfect matching
 * problem
 * */
public interface PerfectMatchingSolverService {

	/**
	 * Will get the perfect matching
	 * with minimum weight
	 * */
	List<Edge> getMinimumWeightPerfectMatching(List<Point> points);
	
}
