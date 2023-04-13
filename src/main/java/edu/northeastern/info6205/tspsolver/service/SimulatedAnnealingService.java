package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service to apply Simulated Annealing
 * */
public interface SimulatedAnnealingService {
		
	/**
	 * Will apply Simulated Annealing
	 * to improve the total travel
	 * cost for the given list of edges
	 * and parameters
	 * */
	List<Point> simulatedAnnealing(
			List<Point> tour, 
			int maxIteration,
			double startingTemperature,
			double coolingRate);
}
