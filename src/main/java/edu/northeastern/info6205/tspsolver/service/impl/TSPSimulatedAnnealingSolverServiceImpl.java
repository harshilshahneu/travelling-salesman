package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.algorithm.annealing.SimulatedAnnealingOptimization;
import edu.northeastern.info6205.tspsolver.algorithm.christofides.Christofides;
import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPPayload;
import edu.northeastern.info6205.tspsolver.model.TSPPayload.SimulatedAnnealingPayload;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;

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
		
		Christofides christofides = new Christofides(points);
		christofides.solve();
		List<Point> tour = christofides.solve();
		
		// Last point and first point are same in Christofides tour
		tour.remove(tour.size() - 1);
		
		SimulatedAnnealingPayload annealingPayload = payload.getSimulatedAnnealingPayload();
		
		int[] christofidesTour = tour.stream()
				.mapToInt(p -> Integer.parseInt(p.getId()))
				.toArray();
		
		int n = points.size();
		double[][] graph = new double[n][n];
		for (int i = 0; i < n; i++) {
			Point source = points.get(i);
			for (int j = i + 1; j < n; j++) {
				Point destination = points.get(j);
				double distance = HaversineDistanceUtil.haversine(destination, source);
				graph[i][j] = distance;
				graph[j][i] = distance;
			}
		}
		
		SimulatedAnnealingOptimization optimization = new SimulatedAnnealingOptimization(
				christofidesTour, 
				graph, 
				annealingPayload.getMaxIteration(), 
				annealingPayload.getStartingTemperature(), 
				annealingPayload.getFinalTemperature(), 
				annealingPayload.getCoolingRate());
		
		int[] path = optimization.solve();
		
		Map<Integer, Point> pointMap = new HashMap<>();
		for (Point point : points) {
			pointMap.put(Integer.parseInt(point.getId()), point);
		}
		
		List<Point> result = new ArrayList<>();
		for (int node : path) {
			result.add(pointMap.get(node));
		}
		
		Point firstPoint = pointMap.get(startingPointIndex);
		int firstPointIndex = result.indexOf(firstPoint);
		LOGGER.trace("firstPointIndex: {}", firstPointIndex);
		
		if (firstPointIndex != -1) {
			int rotations = firstPointIndex;
			LOGGER.info("will rotate the tour by rotations: {}", rotations);
			Collections.rotate(result, -rotations);
		} else {
			LOGGER.info("ACO gave correct tours, no need to rotate array");
		}

		result.add(result.get(0));
		return result;
		
		/*
		SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(
				tour, 
				annealingPayload.getMaxIteration(), 
				annealingPayload.getStartingTemperature(), 
				annealingPayload.getFinalTemperature(), 
				annealingPayload.getCoolingRate());
		
		List<Point> result = simulatedAnnealing.solve();
		return result;
		*/
	}
	
}
