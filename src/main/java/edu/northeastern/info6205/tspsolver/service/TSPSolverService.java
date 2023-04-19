package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;

/**
 * Service to solve the TSP Problem
 * */
public interface TSPSolverService {

	/**
	 * Will return the key to identify the
	 * Service
	 * */
	String getKeyIdentifier();
	
	/**
	 * Will return the Name of the service
	 * */
	String getName();
	
	/**
	 * Will solve the TSP for the given
	 * list of {@link Point} and then 
	 * return the list of points in the order in
	 * which they are visited for completing the
	 * TSP tour
	 * */
	List<Point> solve(
			List<Point> points, 
			int startingPointIndex,
			TSPPayload payload);
	
}
