package edu.northeastern.info6205.tspsolver.model;

import java.io.Serializable;

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
		// TODO Need to add parameters
	}
	
	public static class AntColonyOptimazationPayload {
		// TODO Need to add parameters
	}
}
