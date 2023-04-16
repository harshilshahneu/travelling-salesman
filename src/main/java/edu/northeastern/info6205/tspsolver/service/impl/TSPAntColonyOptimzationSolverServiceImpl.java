package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

public class TSPAntColonyOptimzationSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPAntColonyOptimzationSolverServiceImpl.class);
	
	private static TSPSolverService instance;
	
	private TSPAntColonyOptimzationSolverServiceImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static TSPSolverService getInstance() {
		if (instance == null) {
			instance = new TSPAntColonyOptimzationSolverServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public String getKeyIdentifier() {
		return Constant.KEY_IDENTIFIER_ANT_COLONY;
	}

	@Override
	public String getName() {
		return Constant.NAME_ANT_COLONY;
	}

	@Override
	public List<Point> solve(
			List<Point> points, 
			int startingPointIndex, 
			TSPPayload payload) {
		LOGGER.info(
				"Ant Colony Optimzation will solve for points size: {}, startingPointIndex: {}, payload: {}", 
				points.size(),
				startingPointIndex,
				payload);
		
		// TODO Use Ant Colony Optimization algorithm to return the tour
		return null;
	}
	
}
