package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.service.TSPSolverFactoryService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

public class TSPSolverFactoryServiceImpl implements TSPSolverFactoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPSolverFactoryServiceImpl.class);

	private static TSPSolverFactoryService instance;
	
	private Map<String, TSPSolverService> serviceMap;
	
	private TSPSolverFactoryServiceImpl() {
		LOGGER.info("initialising instance");
		initializeServiceMap();
	}
	
	public static TSPSolverFactoryService getInstance() {
		if (instance == null) {
			instance = new TSPSolverFactoryServiceImpl();
		}
		
		return instance;
	}
	
	private void initializeServiceMap() {
		LOGGER.info("initializing the service map");
		
		serviceMap = new HashMap<>();
		
		addService(TSPJspritSolverServiceImpl.getInstance());
		addService(TSPChristofidesSolverServiceImpl.getInstance());
		addService(TSPRandomTwoOptSolverServiceImpl.getInstance());
		addService(TSPRandomThreeOptSolverServiceImpl.getInstance());
		addService(TSPSimulatedAnnealingSolverServiceImpl.getInstance());
		addService(TSPAntColonyOptimzationSolverServiceImpl.getInstance());
		addService(TSPACOThreeOptSolverServiceImpl.getInstance());
	}
	
	private void addService(TSPSolverService service) {
		LOGGER.info("adding service: {}", service);
		serviceMap.put(service.getKeyIdentifier(), service);
	}
	
	@Override
	public TSPSolverService getService(String identifier) {
		LOGGER.trace("will get tsp service for identifier: {}", identifier);
		TSPSolverService service = serviceMap.get(identifier);
		
		// This condition should never happen but still keeping for safety
		if (service == null) {
			LOGGER.error("no suitable tsp service found for identifier: {}", identifier);
		}
		
		return service;
	}

}
