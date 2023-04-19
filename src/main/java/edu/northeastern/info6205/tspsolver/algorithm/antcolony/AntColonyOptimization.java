package edu.northeastern.info6205.tspsolver.algorithm.antcolony;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntColonyOptimization {
	private static final Logger LOGGER = LoggerFactory.getLogger(AntColonyOptimization.class);

	// Parameters of ACO algorithm
//	private static final int NUM_ANTS = 10; // Number of ants
//	private static final double ALPHA = 1.0; // Pheromone exponent
//	private static final double BETA = 2.0; // Heuristic exponent
//	private static final double RHO = 0.1; // Pheromone evaporation rate
//	private static final double Q = 1.0; // Pheromone deposit factor
//	private static final int NUM_ITERATIONS = 20; // Number of iterations

	// Parameters of ACO algorithm
	private final int numberOfAnts;
	private final double phermoneExponent; //ALPHA
	private final double heuristicExponent; //BETA
	private final double phermoneEvaporationRate; // RHO
	private final double phermoneDepositFactor; // Q
	private final int numberOfIterations;
	private final int maxImprovementIterations;
	
	// Graph represented as adjacency matrix
	private final double[][] graph;
	private final int[] christofidesTour;
	private final int numVertices;

	// Ants' current tours and distances
	private int[][] antTours;
	private double[] antDistances;

	// Pheromone levels on edges
	private double[][] pheromones;

	// Heuristic information on edges
	private double[][] heuristicInfo;
	
	public AntColonyOptimization(
			double[][] graph,
			int[] christofidesTour,
			int numberOfAnts,
			double phermoneExponent,
			double heuristicExponent,
			double phermoneEvaporationRate,
			double phermoneDepositFactor,
			int numberOfIterations,
			int maxImprovementIterations) {
        this.graph = graph;
        this.christofidesTour = christofidesTour;
        
        this.numberOfAnts = numberOfAnts;
        this.phermoneExponent = phermoneExponent;
        this.heuristicExponent = heuristicExponent;
        this.phermoneEvaporationRate = phermoneEvaporationRate;
        this.phermoneDepositFactor = phermoneDepositFactor;
        this.numberOfIterations = numberOfIterations;
        this.maxImprovementIterations = maxImprovementIterations;

        this.numVertices = graph.length;
        this.antTours = new int[numberOfAnts][numVertices];
        this.antDistances = new double[numberOfAnts];
        this.pheromones = new double[numVertices][numVertices];
        this.heuristicInfo = new double[numVertices][numVertices];
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("initialised with parameters, graph length: ");
        stringBuilder.append(graph.length);
        stringBuilder.append(", christofidesTour length: ");
        stringBuilder.append(christofidesTour.length);
        stringBuilder.append(", numberOfAnts: ");
        stringBuilder.append(numberOfAnts);
        stringBuilder.append(", phermoneExponent: ");
        stringBuilder.append(phermoneExponent);
        stringBuilder.append(", heuristicExponent: ");
        stringBuilder.append(heuristicExponent);
        stringBuilder.append(", phermoneEvaporationRate: ");
        stringBuilder.append(phermoneEvaporationRate);
        stringBuilder.append(", phermoneDepositFactor: ");
        stringBuilder.append(phermoneDepositFactor);
        stringBuilder.append(", numberOfIterations: ");
        stringBuilder.append(numberOfIterations);
        stringBuilder.append(", maxImprovementIterations: ");
        stringBuilder.append(maxImprovementIterations);
        
        LOGGER.info(stringBuilder.toString());
        
        initialize();
    }
	
	public void initialize() {
		LOGGER.info("initializing all matrix");
		
		// Compute heuristic information as inverse of distance
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				if (i != j) {
					heuristicInfo[i][j] = 1.0 / graph[i][j];
				}
			}
		}
		
		// Initialize pheromones on edges of approximate tour
		double initialPheromone = 1.0 / (numVertices * christofidesTour.length);
		for (int i = 0; i < numVertices - 1; i++) {
			pheromones[christofidesTour[i]][christofidesTour[i+1]] = initialPheromone;
			pheromones[christofidesTour[i+1]][christofidesTour[i]] = initialPheromone;
		}
		
		pheromones[christofidesTour[numVertices-1]][christofidesTour[0]] = initialPheromone;
		pheromones[christofidesTour[0]][christofidesTour[numVertices-1]] = initialPheromone;

		for (int i = 0; i < numberOfAnts; i++) {
			antTours[i] = christofidesTour.clone();
			antDistances[i] = computeTourDistance(christofidesTour);
		}
	}
	
	// ACO algorithm
	public int[] runACO() {
		LOGGER.info("Running the optimization");

		int[] bestTour = null;
		double bestDistance = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < numberOfIterations; i++) {
			LOGGER.trace("iteration counter: {}", i);
			
			// Move ants to construct tours
			for (int j = 0; j < numberOfAnts; j++) {
				moveAnt(j);
			}
			
			// Update pheromone trail
			updatePheromones();
			
			// Evaluate ants' tours
			for (int j = 0; j < numberOfAnts; j++) {
				LOGGER.trace("evaluate ant tour: {}", j);
				double distance = computeTourDistance(antTours[j]);
				if (distance < bestDistance) {
					bestTour = antTours[j].clone();
					bestDistance = distance;
				}
			}
		}
		
		return bestTour;
	}
	
	// Ant movement
	private void moveAnt(int k) {
		LOGGER.trace("moving ant: {}", k);
		
		int[] tour = antTours[k];
		tour[0] = (int) (Math.random() * numVertices);
		boolean[] visited = new boolean[numVertices];
		visited[tour[0]] = true;
		// Construct tour by probabilistically choosing next city
		for (int i = 1; i < numVertices; i++) {
			int currentCity = tour[i-1];
			int nextCity = chooseNextCity(currentCity, visited);
			tour[i] = nextCity;
			visited[nextCity] = true;
		}
		// Shortcut tour by removing repeated vertices and edges
		tour = shortcutTour(tour);
		// Update ant's tour and distance
		antTours[k] = tour;
		antDistances[k] = computeTourDistance(tour);
	}
	
	// Ant decision rule
	private int chooseNextCity(int i, boolean[] visited) {
		LOGGER.trace("chooseNextCity() for i: {}", i);
		
		double[] probabilities = new double[numVertices];
		double sum = 0.0;
		// Compute probability of choosing each unvisited city
		for (int j = 0; j < numVertices; j++) {
			if (!visited[j]) {
				probabilities[j] = Math.pow(pheromones[i][j], phermoneExponent) * Math.pow(heuristicInfo[i][j], heuristicExponent);
				sum += probabilities[j];
			}
		}
		// Choose next city probabilistically
		double random = Math.random() * sum;
		sum = 0.0;
		for (int j = 0; j < numVertices; j++) {
			if (!visited[j]) {
				sum += probabilities[j];
				if (random <= sum) {
					return j;
				}
			}
		}
		// If all cities are visited, choose a random unvisited city
		for (int j = 0; j < numVertices; j++) {
			if (!visited[j]) {
				return j;
			}
		}
		
		LOGGER.error("should not reach here, error condition! returning {}", i);
		return i; // Should not reach here
	}
	
	// Local optimization of a tour using edge shortcuts
	private int[] shortcutTour(int[] tour) {
		LOGGER.trace("shortcutTour start");

		boolean improved = true;
	    int iterCount = 0;
		while (improved) {
			improved = false;
			for (int i = 0; i < numVertices; i++) {
				int city1 = tour[i];
				int city2 = tour[(i+1)%numVertices];
				double dist1 = graph[city1][city2];
				for (int j = i+2; j < numVertices; j++) {
					int city3 = tour[j];
					int city4 = tour[(j+1)%numVertices];
					double dist2 = graph[city3][city4];
					double dist3 = graph[city1][city3];
					double dist4 = graph[city2][city4];
					if (dist3 + dist4 < dist1 + dist2) {
						// Replace edge (city1, city2) with (city1, city3) and (city2, city4)
						int[] newTour = new int[numVertices];
						int idx = 0;
						for (int k = 0; k <= i; k++) {
							newTour[idx++] = tour[k];
						}
						for (int k = j; k > i; k--) {
							newTour[idx++] = tour[k];
						}
						for (int k = j+1; k < numVertices; k++) {
							newTour[idx++] = tour[k];
						}
						tour = newTour;
						improved = true;
					}
				}
			}
			
			iterCount++;
	        if (iterCount >= maxImprovementIterations) {
	            LOGGER.trace("shortcutTour: Maximum number of iterations without improvement reached: {}", maxImprovementIterations);
	            break;
	        }
		}
		
		LOGGER.trace("shortcutTour end");
		return tour;
	}
	
	private void updatePheromones() {
        // Decay pheromones
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                pheromones[i][j] *= (1.0 - phermoneEvaporationRate);
            }
        }
        
        // Update pheromones based on ant tours
        for (int k = 0; k < numberOfAnts; k++) {
            double tourLength = antDistances[k];
            for (int i = 0; i < numVertices; i++) {
                int city1 = antTours[k][i];
                int city2 = antTours[k][(i+1)%numVertices];
                pheromones[city1][city2] += phermoneDepositFactor / tourLength;
                pheromones[city2][city1] += phermoneDepositFactor / tourLength;
            }
        }
    }
    
    // Tour distance computation
    private double computeTourDistance(int[] tour) {
        double distance = 0.0;
        for (int i = 0; i < numVertices; i++) {
            int city1 = tour[i];
            int city2 = tour[(i+1)%numVertices];
            distance += graph[city1][city2];
        }
        return distance;
    }
	
}
