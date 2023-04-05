package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service to solve the TSP Problem
 * */
public interface TSPSolverService {

	/**
	 * Will solve the TSP for the given
	 * list of {@link Point} in an asychronous 
	 * manner so that the thread does NOT get blocked
	 * */
	void solveAsync(List<Point> points);
	
}
