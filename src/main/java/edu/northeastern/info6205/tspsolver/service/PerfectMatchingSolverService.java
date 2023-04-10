package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service to solve the Perfect matching
 * problem
 * */
public interface PerfectMatchingSolverService {

	/**
	 * Will apply Edmond Karpds algorithm
	 * to solve the minimized weight matching
	 * for the given list of points
	 * */
	List<Edge> edmondAlgorithm(List<Point> points);
	
	/**
	 * Will use the KolmogorovWeightedMatching
	 * in jgrapht-core library
	 * */
	List<Edge> kolmogorovMatching(List<Point> points);
	
}
