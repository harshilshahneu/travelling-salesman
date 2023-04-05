package edu.northeastern.info6205.tspsolver.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.info6205.tspsolver.model.Point;

/**
 * Service to parse the CSV Data and save
 * 
 * */
public interface CSVParserService {
	
	/**
	 * Parses the given {@link MultipartFile} (which is expected in CSV Format)
	 * and returns list of {@link Point}
	 * */
	List<Point> parsePoints(MultipartFile multipartFile);
	
}
