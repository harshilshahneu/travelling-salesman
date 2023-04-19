package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service created for test functions
 * */
public interface TestService {

	/**
	 * Will asychronously publish events in web socket
	 * to plot points and draw lines
	 * */
	public void testAsync(List<Point> points);
	
}
