package edu.northeastern.info6205.tspsolver.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;

public class CSVParserServiceImpl implements CSVParserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVParserServiceImpl.class);
	
	private static CSVParserService instance;
	
	private CSVParserServiceImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static CSVParserService getInstance() {
		if (instance == null) {
			instance = new CSVParserServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public List<Point> parsePoints(MultipartFile multipartFile) {
		List<Point> points = new ArrayList<>();
		
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
			String line = bufferedReader.readLine();
			String[] headerParts = line.split(Constant.COMMA);
			List<String> headerList = Arrays
					.asList(headerParts)
					.stream()
					.map(String::toLowerCase)
					.collect(Collectors.toList());
			
			int latitudeIndex = headerList.indexOf(Constant.LATITUDE);
			int longitudeIndex = headerList.indexOf(Constant.LONGITUDE);
			int placeIdIndex = headerList.stream()
			        .filter(s -> s.contains(String.valueOf(Constant.ID)))
			        .findFirst()
			        .map(headerList::indexOf)
			        .orElse(-1);
			if (placeIdIndex == -1) {
				LOGGER.error("Header list does not contain single column matching: {}", Constant.ID);
			}
			
			LOGGER.trace(
					"latitudeIndex: {}, longitudeIndex: {}, placeIdIndex: {}",
					latitudeIndex,
					longitudeIndex,
					placeIdIndex);
			
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index++;
				
				String[] parts = line.split(Constant.COMMA);
				List<String> partsList = Arrays.asList(parts);

				if (partsList.contains(Constant.NO_LOCATION)) {
					continue;
				}
				
				/**
				 * Sometimes ID is missing from the CSV file
				 * and also using an integer type of ID allows
				 * to identify the point more easily.
				 * 
				 * <br><br>
				 * 
				 * ID will correspond to the row number of the data
				 * where first record starts from index 0
				 * */
//				String id = parts[0];
				String id = String.valueOf(index);
				
				double longitude = Double.parseDouble(partsList.get(longitudeIndex));
				double latitude = Double.parseDouble(partsList.get(latitudeIndex));

				String placeId = (placeIdIndex != -1) ? partsList.get(placeIdIndex) : Constant.DASH;
				
				Point point = new Point(id, latitude, longitude);
				point.setPlaceId(placeId);
				points.add(point);
				
				LOGGER.trace("added point: {}", point);
			}
		} catch (Exception  e) {
			LOGGER.error("Error in parseNodes(): {}", e.getMessage(), e);
		}
		
		LOGGER.trace("parsed points size: {} from multipartFile: {}", points.size(), multipartFile);
		return points;
	}
	
}
