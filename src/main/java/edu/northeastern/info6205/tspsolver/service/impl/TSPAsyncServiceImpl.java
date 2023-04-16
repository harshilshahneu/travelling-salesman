package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.TSPAsyncService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverFactoryService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.EdgeUtil;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

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
			MapService mapService = MapServiceImpl.getInstance();
			mapService.publishClearMap();
			
			mapService.publishAddPointsAndFitBound(points);
			
			TSPSolverFactoryService factoryService = TSPSolverFactoryServiceImpl.getInstance();
			TSPSolverService solverService = factoryService.getService(keyIdentifier);
			
			PrimsMST primsMST = new PrimsMST(points);
	        primsMST.solve();
	        List<Edge> mstTour = Arrays.asList(primsMST.getMst());
			double mstCost = EdgeUtil.getTotalCost(mstTour);
	        
			long startTimestamp = System.currentTimeMillis();
			
			List<Point> tspTour = solverService.solve(
					points, 
					startingPointIndex, 
					payload);
			
			mapService.publishAddPolylineAndFitBound(tspTour);
			
			double tspTourCost = PointUtil.getTotalCost(tspTour);
			double percentageImprovement = ( (tspTourCost - mstCost) / mstCost ) * 100;
			
			long endTimestamp = System.currentTimeMillis();
			long millsecondsTaken = (endTimestamp - startTimestamp);
			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(Constant.LINE_SEPERATOR);
			stringBuilder.append("[PERFORMANCE METRIC --- START -----]");
			
			addMetric(stringBuilder, "keyIdentifier", keyIdentifier);
			addMetric(stringBuilder, "points size", points.size());
			addMetric(stringBuilder, "startingPointIndex", startingPointIndex);
			addMetric(stringBuilder, "payload", payload);
			addMetric(stringBuilder, "TSP Tour Size", tspTour.size());
			addMetric(stringBuilder, "mstCost", mstCost);
			addMetric(stringBuilder, "tspTourCost", tspTourCost);
			addMetric(stringBuilder, "percentage (compared to TSP)", percentageImprovement);
			addMetric(stringBuilder, "Millseconds Taken", millsecondsTaken);
			
			stringBuilder.append(Constant.LINE_SEPERATOR);
			stringBuilder.append("[PERFORMANCE METRIC --- END  -----]");
			
			LOGGER.info(stringBuilder.toString());
		};
		
		new Thread(runnable).start();
	}
	
	private StringBuilder addMetric(
			StringBuilder stringBuilder,
			String metric,
			Object metricValue) {
		stringBuilder.append(Constant.LINE_SEPERATOR);
		stringBuilder.append(metric);
		stringBuilder.append(Constant.COLON_SINGLE_SPACE);
		stringBuilder.append(metricValue);
		
		return stringBuilder;
	}

}
