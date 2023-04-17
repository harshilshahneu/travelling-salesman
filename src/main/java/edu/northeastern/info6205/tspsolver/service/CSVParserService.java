package edu.northeastern.info6205.tspsolver.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service to parse the CSV Data and save
 * 
 * */
public interface CSVParserService {
	
	/**
	 * Parses the list of {@link Point} at the
	 * given path and converts it into {@link MultipartFile}
	 * and then internally calls {{@link #parsePoints(InputStream)}}
	 * */
	List<Point> parsePoints(String path);
	
	/**
	 * Parses the list of {@link Point} for the given {@link MultipartFile}
	 * and then internally calls {{@link #parsePoints(InputStream)}}
	 *
	 * */
	List<Point> parsePoints(MultipartFile multipartFile);

	/**
	 * Parese the list of {@link Point} for the given {@link InputStream}
	 * */
	List<Point> parsePoints(InputStream inputStream);

}
