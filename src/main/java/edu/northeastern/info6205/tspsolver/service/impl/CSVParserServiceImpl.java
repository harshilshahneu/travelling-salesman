package edu.northeastern.info6205.tspsolver.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;

@Service
public class CSVParserServiceImpl implements CSVParserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVParserServiceImpl.class);
	
	@Override
	public List<Point> parsePoints(File file) {
		List<Point> points = new ArrayList<>();
		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line = bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split(Constant.COMMA);
				if (parts[6].equals(Constant.NO_LOCATION)) {
					continue;
				}

				String id = parts[0];
				double longitude = Double.parseDouble(parts[4]);
				double latitude = Double.parseDouble(parts[5]);
				
				Point node = new Point(id, latitude, longitude);
				points.add(node);
			}
			
			
		} catch (Exception  e) {
			LOGGER.error("Error in parseNodes(): {}", e.getMessage(), e);
		}
		
		LOGGER.trace("parsed points size: {} from file: {}", points.size(), file);
		return points;
	}
	
}
