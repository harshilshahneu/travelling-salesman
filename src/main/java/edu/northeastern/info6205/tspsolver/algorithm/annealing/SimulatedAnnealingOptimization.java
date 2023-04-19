package edu.northeastern.info6205.tspsolver.algorithm.annealing;

import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulatedAnnealingOptimization {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedAnnealingOptimization.class);

	private final int[] christofidesTour; 
	
	private final double[][] distanceMatrix;
	private final int n;
	private final int maxIteration;
	private final double startingTemperature;
	private final double finalTemperature;
	private final double coolingRate;

	public SimulatedAnnealingOptimization(
			int[] christofidesTour,
			double[][] distanceMatrix, 
			int maxIteration, 
			double startingTemperature, 
			double finalTemperature, 
			double coolingRate) {
		this.christofidesTour = christofidesTour;
        this.distanceMatrix = distanceMatrix;
        this.n = distanceMatrix.length;
        this.maxIteration = maxIteration;
        this.startingTemperature = startingTemperature;
        this.finalTemperature = finalTemperature;
        this.coolingRate = coolingRate;
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("initialised with parameters, distanceMatrix lenght: ");
        stringBuilder.append(distanceMatrix.length);
        stringBuilder.append(", maxIteration: ");
        stringBuilder.append(maxIteration);
        stringBuilder.append(", startingTemperature: ");
        stringBuilder.append(startingTemperature);
        stringBuilder.append(", finalTemperature: ");
        stringBuilder.append(finalTemperature);
        stringBuilder.append(", coolingRate: ");
        stringBuilder.append(coolingRate);
        
        LOGGER.info(stringBuilder.toString());
    }
	
	public int[] solve() {
		Random random = new Random();
		double temperature = startingTemperature;
		int[] current = Arrays.copyOf(christofidesTour, n);
		int[] best = Arrays.copyOf(christofidesTour, n);
		double bestCost = calculateCost(best);

		int iteration = 0;
		while (temperature >= finalTemperature || iteration < maxIteration) {
			LOGGER.trace("temperature: {}, iteration: {}", temperature, iteration);
			
			int[] next = Arrays.copyOf(current, n);

			// randomly select two cities to swap
			int i = random.nextInt(n - 1) + 1;
			int j;
			do {
				j = random.nextInt(n - 1) + 1;
			} while (i == j);

			// swap two cities
			int temp = next[i];
			next[i] = next[j];
			next[j] = temp;

			double nextCost = calculateCost(next);
			double currentCost = calculateCost(current);

			if (nextCost < bestCost) {
				best = Arrays.copyOf(next, n);
				bestCost = nextCost;
			}

			if (nextCost < currentCost) {
				current = Arrays.copyOf(next, n);
			} else if (Math.random() < probability(currentCost, nextCost, temperature)) {
				current = Arrays.copyOf(next, n);
			}

			if (temperature >= finalTemperature) {
				temperature *= coolingRate;
			}

			iteration++;
		}

		return best;
	}
	
	private double calculateCost(int[] tour) {
		double cost = 0;
		for (int i = 0; i < n - 1; i++) {
			cost += distanceMatrix[tour[i]][tour[i + 1]];
		}
		cost += distanceMatrix[tour[n - 1]][tour[0]]; // return to start city
		return cost;
	}

	private double probability(double currentCost, double nextCost, double temperature) {
		if (nextCost < currentCost) {
			return 1;
		}
		return Math.exp((currentCost - nextCost) / temperature);
	}
}
