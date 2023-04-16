package edu.northeastern.info6205.tspsolver.service;

/**
 * Will return the instance of {@link TSPSolverService}
 * depending on certain criteria as it can
 * have multiple implementations like
 * TwoOpt, Simulated Annealing, etc
 * */
public interface TSPSolverFactoryService {

	TSPSolverService getService(String identifier);
	
}
