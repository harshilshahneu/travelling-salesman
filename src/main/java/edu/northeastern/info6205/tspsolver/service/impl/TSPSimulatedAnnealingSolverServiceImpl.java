package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

public class TSPSimulatedAnnealingSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPSimulatedAnnealingSolverServiceImpl.class);
	
	private static TSPSolverService instance;
	
	private TSPSimulatedAnnealingSolverServiceImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static TSPSolverService getInstance() {
		if (instance == null) {
			instance = new TSPSimulatedAnnealingSolverServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public String getKeyIdentifier() {
		return Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING;
	}

	@Override
	public String getName() {
		return Constant.NAME_SIMULATED_ANNEALING;
	}

	@Override
	public List<Point> solve(
			List<Point> points, 
			int startingPointIndex, 
			TSPPayload payload) {
		LOGGER.info(
				"Simulated Annealing will solve for points size: {}, startingPointIndex: {}, payload: {}", 
				points.size(),
				startingPointIndex,
				payload);
		
		// TODO Use Simulated Annealing algorithm to return the tour
		return null;
	}
	
}
