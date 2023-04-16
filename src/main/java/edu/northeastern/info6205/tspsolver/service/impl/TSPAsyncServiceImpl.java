package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.service.TSPAsyncService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverFactoryService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

public class TSPAsyncServiceImpl implements TSPAsyncService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPAsyncServiceImpl.class);

	private static TSPAsyncService instance;
	
	public static TSPAsyncService getInstance() {
		if (instance == null) {
			instance = new TSPAsyncServiceImpl();
		}
		
		return instance;
	}
	
	private TSPAsyncServiceImpl() {
		LOGGER.info("Initializing the instance");
	}
	
	@Override
	public void solveAsync(
			String keyIdentifier, 
			List<Point> points, 
			int startingPointIndex, 
			TSPPayload payload) {
		LOGGER.trace(
				"solve tsp in async manner for keyIdentifier: {}, points size: {}, startingPointIndex: {}, payload: {}",
				keyIdentifier,
				points.size(),
				startingPointIndex,
				payload);
		
		Runnable runnable = () -> {
			TSPSolverFactoryService factoryService = TSPSolverFactoryServiceImpl.getInstane();
			TSPSolverService solverService = factoryService.getService(keyIdentifier);
			
			long startTimestamp = System.currentTimeMillis();
			
			List<Point> tspTour = solverService.solve(
					points, 
					startingPointIndex, 
					payload);
			
			long endTimestamp = System.currentTimeMillis();
			long millsecondsTaken = (endTimestamp - startTimestamp);
			
			LOGGER.info(
					"[PERFORMANCE METRIC] keyIdentifier: {}, points size: {}, startingPointIndex: {}, payload: {}, tspTour size: {}, millsecondsTaken: {}",
					keyIdentifier,
					points.size(),
					startingPointIndex,
					payload,
					tspTour.size(),
					millsecondsTaken);
		};
		
		new Thread(runnable).start();
	}

}
