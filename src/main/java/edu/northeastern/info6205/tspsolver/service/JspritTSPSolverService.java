package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Will solve the Travelling Salesman problem
 * by using the vehicle routing solving
 * algorithm provided in the jsprit library
 * */
public interface JspritTSPSolverService {
	
	/**
	 * Will give the TSP tour of the given points
	 * and starting point index
	 * assuming that haversine distance is going to
	 * be calculated
	 * */
	List<Point> getTSPTour(List<Point> points, int startingPointIndex);
	
}
