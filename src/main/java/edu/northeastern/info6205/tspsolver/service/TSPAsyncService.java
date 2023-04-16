package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;

/**
 * Service to solve the TSP
 * in a separate thread so that it executes
 * asynchronously and doesn't block the main
 * thread.
 * 
 * <br><br>
 * 
 * Will use the {@link TSPSolverFactoryService}
 * to determine which {@link TSPSolverService}
 * implementation to use
 * */
public interface TSPAsyncService {

	void solveAsync(
			String keyIdentifier,
			List<Point> points, 
			int startingPointIndex,
			TSPPayload payload);
	
}
