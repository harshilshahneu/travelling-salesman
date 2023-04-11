package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.SimulatedAnnealingService;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

@Service
public class SimulatedAnnealingServiceImpl implements SimulatedAnnealingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedAnnealingServiceImpl.class);

	@Override
	public List<Point> simulatedAnnealing(
			List<Point> tour, 
			double startingTemperature,
			double coolingRate) {
		LOGGER.trace(
				"will apply simulated annealing for tour size: {}, startingTemperature: {}, coolingRate: {}", 
				tour.size(),
				startingTemperature,
				coolingRate);

		final double FINAL_TEMPERATURE = 1;
		
		List<Point> best = new ArrayList<>(tour);
		Random random = new Random();

		for (double temperature = startingTemperature; temperature >= FINAL_TEMPERATURE; temperature *= coolingRate) {
			LOGGER.trace("temperature: {}", temperature);
			List<Point> neighbour = new ArrayList<>(tour);
			
			// This code will generate random numbers of distinct values
			List<Integer> randomNumbers;
			do {
				randomNumbers = random.ints(2, 1, tour.size() - 1)
	                     .boxed()
	                     .collect(Collectors.toList());
			} while (randomNumbers.get(0) == randomNumbers.get(1));
			
			int firstIndex = randomNumbers.get(0);
			int secondIndex = randomNumbers.get(1);
			if (firstIndex == secondIndex) {
				// IDEALLY this should not even print but keeping it here for debugging
				LOGGER.trace("firstIndex: {} matching secondIndex: {}", firstIndex, secondIndex);
			}
			
			Collections.swap(neighbour, firstIndex, secondIndex);
			
			double tourCost = PointUtil.getTotalCost(tour);
			double neighbourCost = PointUtil.getTotalCost(neighbour);
			
			if (tourCost < PointUtil.getTotalCost(best)) {
				best = new ArrayList<>(tour);
			} else if (Math.random() < probability(tourCost, neighbourCost, temperature)) {
				tour = new ArrayList<>(neighbour);
			}
		}
		
		LOGGER.trace("returning best size: {}", best.size());
		return best;
	}
	
	private double probability(double previousCost, double newCost, double temp) {
        if (newCost < previousCost) return 1;
        return Math.exp((previousCost - newCost) / temp);
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
