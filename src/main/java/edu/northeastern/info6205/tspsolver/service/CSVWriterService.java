package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPOutput;

/**
 * Service to write the CSV
 * output of the travelling salesman
 * solution
 * */
public interface CSVWriterService {

	/**
	 * Will write the points in a
	 * CSV file with a filename
	 * generated according to time-stamp.
	 * 
	 * <br><br>
	 * 
	 * Internally calls {@link #write(List, String)}
	 * with the newly generated file name
	 * 
	 * @return the {@link TSPOutput} representing
	 * the TSP Output solution
	 * */
	TSPOutput write(
			List<Point> points,
			String algorithmName,
			double percentage,
			double tourDistance);
	
	/**
	 * Will write the points in a
	 * CSV file with the following file name
	 * in the temp directory.
	 * 
	 * @return the {@link TSPOutput} representing
	 * the TSP Output solution
	 * */
	TSPOutput write(
			List<Point> points, 
			String fileName);
	
}
