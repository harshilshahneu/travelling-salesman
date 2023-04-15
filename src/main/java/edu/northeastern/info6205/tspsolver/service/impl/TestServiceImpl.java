package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.TestService;

public class TestServiceImpl implements TestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

	private static TestService instance;
	
	private TestServiceImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static TestService getInstance() {
		if (instance == null) {
			instance = new TestServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public void testAsync(List<Point> points) {
		LOGGER.trace("testAsync for points size: {}", points.size());
		
		MapService mapService = MapServiceImpl.getInstance();
		
		// Printing to check that only one object service instance is created in the application
		LOGGER.trace("mapService: {}", mapService);
		
		mapService.publishAddPointsAndFitBound(points);
		
		LOGGER.trace("testAsync has finished processing for points size: {}", points.size());
	}

}
