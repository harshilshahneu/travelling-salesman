package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service to change Map state
 * */
public interface MapService {

	/**
	 * Will publish a message in the web socket
	 * channel to tell the map to clear all underlying layers
	 * */
	void publishClearMap();
	
	/**
	 * Will publish a message in the web socket
	 * channel to tell the map to draw all the points
	 * and then fit the map bound
	 * */
	void publishAddPointsAndFitBound(List<Point> points);
	
	/**
	 * Will publish a message in the web socket
	 * channel to tell the point with the given id
	 * has been relaxed
	 * */
	void publishPointRelaxed(String id);
	
	/**
	 * Will publish a message in the web socket
	 * channel to tell that a line has been
	 * drawn between two points
	 * */
	void publishDrawEdge(Edge edge);
	
	/**
	 * Will publish a message in the web socket
	 * channel to tell that a point color
	 * has to be changed to Red for given id
	 * */
	void publishChangePointColorRed(String id);
	
	/**
	 * Will publish a message in the web socket
	 * channel to tell that a point color
	 * has to be changed to Green for given id
	 * */
	void publishChangePointColorGreen(String id);
	
	/**
	 * Will publish a message in the web socket
	 * channel to tell that a line has to be added with
	 * a color Green
	 * */
	void publishAddGreenLine(Edge edge);
}
