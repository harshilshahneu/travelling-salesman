package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPOutput;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;

/**
 * Service to solve the TSP
 * 
 * <br><br>
 * 
 * Will use the {@link TSPSolverFactoryService}
 * to determine which {@link TSPSolverService}
 * implementation to use
 * */
public interface TSPService {

	/**
	 * @return The file name which contains the
	 * output CSV of the solved TSP problem
	 * */
	TSPOutput solve(
			String keyIdentifier,
			List<Point> points, 
			int startingPointIndex,
			TSPPayload payload);
	
}
