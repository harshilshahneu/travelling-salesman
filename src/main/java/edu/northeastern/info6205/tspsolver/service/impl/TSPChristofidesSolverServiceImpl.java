package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.algorithm.christofides.Christofides;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

public class TSPChristofidesSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPChristofidesSolverServiceImpl.class);
	
	private static TSPSolverService instance;
	
	private TSPChristofidesSolverServiceImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static TSPSolverService getInstance() {
		if (instance == null) {
			instance = new TSPChristofidesSolverServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public String getKeyIdentifier() {
		return Constant.KEY_IDENTIFIER_CHRISTOFIDES;
	}

	@Override
	public String getName() {
		return Constant.NAME_CHRISTOFIDES;
	}

	@Override
	public List<Point> solve(
			List<Point> points, 
			int startingPointIndex, 
			TSPPayload payload) {
		LOGGER.info(
				"Christofides will solve for points size: {}, startingPointIndex: {}, payload: {}", 
				points.size(),
				startingPointIndex,
				payload);
		
		Christofides christofides = new Christofides();
		christofides.setPoints(points);
//		List<Edge> candidate = christofides.solve();
		
		// TODO complete rest of the implementation of christofides
		return null;
	}
	
}
