package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.algorithm.christofides.Christofides;
import edu.northeastern.info6205.tspsolver.algorithm.opt.ThreeOpt;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.ThreeOptPayload;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

public class TSPRandomThreeOptSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPRandomThreeOptSolverServiceImpl.class);
	
	private static TSPSolverService instance;
	
	private TSPRandomThreeOptSolverServiceImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static TSPSolverService getInstance() {
		if (instance == null) {
			instance = new TSPRandomThreeOptSolverServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public String getKeyIdentifier() {
		return Constant.KEY_IDENTIFIER_RANDOM_THREE_OPT;
	}

	@Override
	public String getName() {
		return Constant.NAME_RANDOM_THREE_OPT;
	}

	@Override
	public List<Point> solve(
			List<Point> points, 
			int startingPointIndex, 
			TSPPayload payload) {
		LOGGER.info(
				"Random Three Opt will solve for points size: {}, startingPointIndex: {}, payload: {}", 
				points.size(),
				startingPointIndex,
				payload);
		
		Christofides christofides = new Christofides(points);
		List<Point> tour = christofides.solve();
		
		ThreeOptPayload threeOptPayload = payload.getThreeOptPayload();
		ThreeOpt threeOpt = new ThreeOpt(tour, threeOptPayload.getStrategy(), threeOptPayload.getBudget());
		threeOpt.improve();
		List<Point> improvedTour = threeOpt.getImprovedTour();
		return improvedTour;
	}
	
}
