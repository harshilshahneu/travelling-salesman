package edu.northeastern.info6205.tspsolver.algorithm.annealing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

public class SimulatedAnnealing {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedAnnealing.class);

	public List<Point> solve(
			List<Point> tour, 
			int maxIteration,
			double startingTemperature,
			double coolingRate) {
		LOGGER.trace(
				"will apply simulated annealing for tour size: {}, startingTemperature: {}, coolingRate: {}", 
				tour.size(),
				startingTemperature,
				coolingRate);

		long startTimestamp = System.currentTimeMillis();
		
		List<Point> previous = new ArrayList<>(tour);
		
		final double FINAL_TEMPERATURE = 1;
		
		List<Point> best = new ArrayList<>(tour);
		double bestCost = PointUtil.getTotalCost(best);
		LOGGER.trace("initial bestCost: {}", bestCost);
		
		Random random = new Random();

		int iteration = 0;
		for (
				double temperature = startingTemperature; 
				temperature >= FINAL_TEMPERATURE || iteration < maxIteration; 
				iteration++) {
//			LOGGER.trace("temperature: {}, iteration: {}", temperature, iteration);
			List<Point> neighbour = new ArrayList<>(previous);

			// This code will generate random numbers of distinct values
			List<Integer> randomNumbers;
			do {
				randomNumbers = random.ints(2, 1, tour.size() - 1)
	                     .boxed()
	                     .collect(Collectors.toList());
			} while (randomNumbers.get(0).intValue() == randomNumbers.get(1).intValue());
			
			int firstIndex = randomNumbers.get(0);
			int secondIndex = randomNumbers.get(1);
			if (firstIndex == secondIndex) {
				// IDEALLY this should not even print but keeping it here for debugging
				LOGGER.error("firstIndex: {} matching secondIndex: {}", firstIndex, secondIndex);
			}
			
			Collections.swap(neighbour, firstIndex, secondIndex);
			
			double neighbourCost = PointUtil.getTotalCost(neighbour);
			double previousCost = PointUtil.getTotalCost(previous);
			
			if (neighbourCost < bestCost) {
				best = new ArrayList<>(neighbour);
				bestCost = PointUtil.getTotalCost(best);

				previous = new ArrayList<>(neighbour);
				LOGGER.trace("updated bestCost: {}", bestCost);
			} else if (Math.random() < probability(previousCost, neighbourCost, temperature)) {
				previous = new ArrayList<>(neighbour);
				
				/*
				LOGGER.trace(
						"accepting worst neigbour cost: {} over previous cost: {} at temperature: {}", 
						neighbourCost, 
						previousCost,
						temperature);
				*/
			}
			
			if (temperature >= FINAL_TEMPERATURE) {
				temperature *= coolingRate;
			}
		}
		
		long endTimestamp = System.currentTimeMillis();
		long timeTaken = (endTimestamp - startTimestamp);
		
		LOGGER.info(
				"[Simulate Annealing METRIC]: timeTaken {}, best size: {}, with bestCost: {}, iteration: {}", 
				timeTaken,
				best.size(),
				bestCost,
				iteration);
		return best;
	}
	
	private double probability(double previousCost, double newCost, double temperature) {
        if (newCost < previousCost) return 1;
        return Math.exp((previousCost - newCost) / temperature);
    }
	
	public static void main(String[] args) {
		
		Random random = new Random();

		for (int i = 1; i < 100000; i++) {
			
			List<Integer> randomNumbers;
			do {
				randomNumbers = random.ints(2, 0, 7)
	                     .boxed()
	                     .collect(Collectors.toList());
			} while (randomNumbers.get(0) == randomNumbers.get(1));
	        
	        int firstIndex = randomNumbers.get(0);
			int secondIndex = randomNumbers.get(1);
			if (firstIndex == secondIndex) {
				LOGGER.trace("firstIndex: {} matching secondIndex: {}", firstIndex, secondIndex);
			}
		}
		
		LOGGER.trace("Execution done");
	}
}
