package edu.northeastern.info6205.tspsolver.model;

/**
 * Represents the object to help
 * pass parameters into the TSP Service
 * implementation.
 * 
 * Will contain all parameters used by all algorithms
 * and service implementations will decide
 * which parameters to pick up
 * */
public class TSPPayload {
	private TwoOptPayload twoOptPayload;
	private ThreeOptPayload threeOptPayload;
	private SimulatedAnnealingPayload simulatedAnnealingPayload;
	private AntColonyOptimazationPayload antColonyOptimazationPayload;
	
	public TwoOptPayload getTwoOptPayload() {
		return twoOptPayload;
	}

	public ThreeOptPayload getThreeOptPayload() {
		return threeOptPayload;
	}

	public SimulatedAnnealingPayload getSimulatedAnnealingPayload() {
		return simulatedAnnealingPayload;
	}

	public AntColonyOptimazationPayload getAntColonyOptimazationPayload() {
		return antColonyOptimazationPayload;
	}
	
	@Override
	public String toString() {
		return "TSPPayload [twoOptPayload=" + twoOptPayload + ", threeOptPayload=" + threeOptPayload
				+ ", simulatedAnnealingPayload=" + simulatedAnnealingPayload + ", antColonyOptimazationPayload="
				+ antColonyOptimazationPayload + "]";
	}

	public static class TwoOptPayload {
		private int strategy;
		private long budget;
		
		public int getStrategy() {
			return strategy;
		}
		public long getBudget() {
			return budget;
		}

		@Override
		public String toString() {
			return "TwoOptPayload [strategy=" + strategy + ", budget=" + budget + "]";
		}
	}
	
	public static class ThreeOptPayload {
		private int strategy;
		private long budget;
		
		public int getStrategy() {
			return strategy;
		}
		public long getBudget() {
			return budget;
		}

		@Override
		public String toString() {
			return "ThreeOptPayload [strategy=" + strategy + ", budget=" + budget + "]";
		}
	}
	
	public static class SimulatedAnnealingPayload {
		private int maxIteration;
		private double startingTemperature;
		private double finalTemperature;
		private double coolingRate;
		
		public int getMaxIteration() {
			return maxIteration;
		}
		public double getStartingTemperature() {
			return startingTemperature;
		}
		public double getFinalTemperature() {
			return finalTemperature;
		}
		public double getCoolingRate() {
			return coolingRate;
		}

		@Override
		public String toString() {
			return "SimulatedAnnealingPayload [maxIteration=" + maxIteration + ", startingTemperature="
					+ startingTemperature + ", finalTemperature=" + finalTemperature + ", coolingRate=" + coolingRate
					+ "]";
		}
	}
	
	public static class AntColonyOptimazationPayload {
		private int numberOfAnts;
		private double phermoneExponent; //ALPHA
		private double heuristicExponent; //BETA
		private double phermoneEvaporationRate; // RHO
		private double phermoneDepositFactor; // Q
		private int numberOfIterations;
		private int maxImprovementIterations;
		
		public int getNumberOfAnts() {
			return numberOfAnts;
		}
		public double getPhermoneExponent() {
			return phermoneExponent;
		}
		public double getHeuristicExponent() {
			return heuristicExponent;
		}
		public double getPhermoneEvaporationRate() {
			return phermoneEvaporationRate;
		}
		public double getPhermoneDepositFactor() {
			return phermoneDepositFactor;
		}
		public int getNumberOfIterations() {
			return numberOfIterations;
		}
		public int getMaxImprovementIterations() {
			return maxImprovementIterations;
		}

		@Override
		public String toString() {
			return "AntColonyOptimazationPayload [numberOfAnts=" + numberOfAnts + ", phermoneExponent="
					+ phermoneExponent + ", heuristicExponent=" + heuristicExponent + ", phermoneEvaporationRate="
					+ phermoneEvaporationRate + ", phermoneDepositFactor=" + phermoneDepositFactor
					+ ", numberOfIterations=" + numberOfIterations + ", maxImprovementIterations="
					+ maxImprovementIterations + "]";
		}
	}
}
