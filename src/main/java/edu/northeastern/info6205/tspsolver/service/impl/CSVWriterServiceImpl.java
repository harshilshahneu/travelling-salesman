package edu.northeastern.info6205.tspsolver.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPOutput;
import edu.northeastern.info6205.tspsolver.service.CSVWriterService;

public class CSVWriterServiceImpl implements CSVWriterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVWriterServiceImpl.class);

	private static final String TMP_DIRECTORY = "tmp";
	
	private static final int CRIME_ID_TRIM_SIZE = 5;
	private static final String DECIMAL_FORMATTER = "%.3f";
	
	private static CSVWriterService instance;
	
	private CSVWriterServiceImpl() {
		LOGGER.info("Initialising the instance");
		createTmpDirectoryIfNotExists();
	}
	
	private void createTmpDirectoryIfNotExists() {
		LOGGER.info("will create the tmp directory if it doesn't exist");
		
		File directory = new File(TMP_DIRECTORY);
		if (directory.exists()) {
			LOGGER.info("directory already exists no need to do anything");
			return;
		}

		boolean created = directory.mkdir();
		if (created) {
			LOGGER.info("successfully created the TMP directory");
		} else {
			// This should never happen but keeping it here to check unwanted behaviour
			LOGGER.error("failed to create the TMP directory!! This should not have happened");
		}
	}

	public static CSVWriterService getInstance() {
		if (instance == null) {
			instance = new CSVWriterServiceImpl();
		}
		
		return instance;
	}
	
	@Override
	public TSPOutput write(
			List<Point> points,
			String algorithmName,
			double percentage,
			double tourDistance) {
		LOGGER.trace(
				"writing file for points size: {}, algorithmName: {}, percentage: {}, tourDistance: {}", 
				points.size(),
				algorithmName,
				percentage,
				tourDistance);
		String fileName = generateFileName(
				points,
				algorithmName,
				percentage,
				tourDistance);
		return write(points, fileName);
	}
	
	@Override
	public TSPOutput write(List<Point> points, String fileName) {
		LOGGER.trace("writing file for points size: {}, fileName: {}", points.size(), fileName);
		
		String completeFilePath = getCompleteFilePath(fileName);
		LOGGER.info("writing file at location: {}", completeFilePath);
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(completeFilePath))) {
			// Header row
//			writer.write(Constant.ID);
//			writer.write(Constant.COMMA);
			
			writer.write(Constant.CSV_COLUMN_CRIME_ID);
			writer.write(Constant.COMMA);
			writer.write(Constant.CSV_COLUMN_LONGITUDE);
			writer.write(Constant.COMMA);
			writer.write(Constant.CSV_COLUMN_LATITUDE);
			
			writer.newLine();
			
			for (Point point : points) {
				LOGGER.trace("writing data for point: {}", point);

				String trimmedCrimeID = trimCrimeID(point.getPlaceId(), CRIME_ID_TRIM_SIZE);

//				writer.write(point.getId());
//				writer.write(Constant.COMMA);
				
				writer.write(Constant.SINGLE_QUOTE);
				writer.write(trimmedCrimeID);
				writer.write(Constant.SINGLE_QUOTE);
				writer.write(Constant.COMMA);
				writer.write(String.valueOf(point.getLongitude()));
				writer.write(Constant.COMMA);
				writer.write(String.valueOf(point.getLatitude()));
				
				writer.newLine();
			}
		} catch (Exception e) {
			LOGGER.error("Exception in write(): {}", e.getMessage(), e);
		}
		
		TSPOutput output = new TSPOutput();
		output.setCompleteFilePath(completeFilePath);
		output.setFileName(fileName);
		LOGGER.info("finished writing file at output: {}", output);
		return output;
	}
	
	private String trimCrimeID(String placeId, int trimSize) {
		if (placeId == null || placeId.length() <= trimSize) {
			return placeId;
		}

		return placeId.substring(placeId.length() - trimSize);
	}

	private String generateFileName(
			List<Point> points,
			String algorithmName,
			double percentage,
			double tourDistance) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(algorithmName);
		stringBuilder.append(Constant.DASH);
		stringBuilder.append(String.format(DECIMAL_FORMATTER, percentage));
		stringBuilder.append(Constant.DASH);
		stringBuilder.append(String.format(DECIMAL_FORMATTER, tourDistance));
		stringBuilder.append(Constant.DASH);
		stringBuilder.append(System.currentTimeMillis());
		stringBuilder.append(Constant.CSV_EXTENSION);

		return stringBuilder.toString();
	}

	private String getCompleteFilePath(String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(TMP_DIRECTORY);
		stringBuilder.append(Constant.SLASH);
		stringBuilder.append(fileName);
		return stringBuilder.toString();
	}

}
