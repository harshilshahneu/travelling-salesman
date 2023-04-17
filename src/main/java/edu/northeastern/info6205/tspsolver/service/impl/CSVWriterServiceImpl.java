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

	private static final String OUTPUT_FILE_NAME_PREFIX = "tsp-solution-out-";
	private static final String TMP_DIRECTORY = "tmp";
	
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
	public TSPOutput write(List<Point> points) {
		LOGGER.trace("writing file for points size: {} ", points.size());
		String fileName = generateFileName();
		return write(points, fileName);
	}
	
	@Override
	public TSPOutput write(List<Point> points, String fileName) {
		LOGGER.trace("writing file for points size: {}, fileName: {}", points.size(), fileName);
		
		String completeFilePath = getCompleteFilePath(fileName);
		LOGGER.info("writing file at location: {}", completeFilePath);
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(completeFilePath))) {
			// Header row
			writer.write(Constant.ID);
			writer.write(Constant.COMMA);
			writer.write(Constant.LATITUDE);
			writer.write(Constant.COMMA);
			writer.write(Constant.LONGITUDE);
			writer.write(Constant.COMMA);
			writer.newLine();
			
			for (Point point : points) {
				writer.write(point.getId());
				writer.write(Constant.COMMA);
				writer.write(String.valueOf(point.getLatitude()));
				writer.write(Constant.COMMA);
				writer.write(String.valueOf(point.getLongitude()));
				writer.write(Constant.COMMA);
				
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
	
	private String generateFileName() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(OUTPUT_FILE_NAME_PREFIX);
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
