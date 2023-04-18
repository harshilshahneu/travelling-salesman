package edu.northeastern.info6205.tspsolver.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.model.TSPOutput;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.CSVWriterService;

public class CSVWriterServiceImplTest {
    
	@Test
    public void instanceNotNullTest() {
        CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
        assertNotNull(csvService);
    }

    @Test
    public void singletonInstanceTest() {
    	CSVWriterService firstInstance = CSVWriterServiceImpl.getInstance();
    	CSVWriterService secondInstance = CSVWriterServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }
    
    @Test
    public void generateOutputFilePointsNullTest() {
    	assertThrows(NullPointerException.class, () -> {
    		CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
            csvService.generateOutputFile(null, Constant.BLANK_STRING, 0, 0);
    	});
    }
    
    @Test
    public void writeFilePointsNullTest() {
    	assertThrows(NullPointerException.class, () -> {
    		CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
            csvService.writeFile(null, Constant.KEY_IDENTIFIER_ANT_COLONY);
    	});
    }
    
    // This doesn't crash and crated a fill with name 'null'
    @Test
    public void writeFileFileNameNullTest() {
		CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
    	csvService.writeFile(Collections.emptyList(), null);
    }
    
    // Testing that no exception is thrown when empty points list is passed
    @Test
    public void generateOutputFilePointsEmptyTest() {
    	CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
        csvService.generateOutputFile(Collections.emptyList(), Constant.KEY_IDENTIFIER_ANT_COLONY, 0, 0);
    }
    
    // Testing that no exception is thrown when empty points list is passed
    @Test
    public void writeFilePointsEmptyTest() {
    	CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
    	csvService.writeFile(Collections.emptyList(), Constant.KEY_IDENTIFIER_ANT_COLONY);
    }
    
    @Test
    public void generateOutputFilePathAndFileNameCheck() {
    	List<Point> points = Collections.emptyList();
		String algorithmName = Constant.KEY_IDENTIFIER_ANT_COLONY;
		double percentage  = 31;
		double tourDistance = 25;
		
		CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
    	TSPOutput tspOutput = csvService.generateOutputFile(points, algorithmName, percentage, tourDistance);
		
    	String timestamp = computeTimestamp(tspOutput);
    	
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(algorithmName);
		stringBuilder.append(Constant.DASH);
		stringBuilder.append(String.format(Constant.DECIMAL_THREE_PLACES_FORMATTER, percentage));
		stringBuilder.append(Constant.DASH);
		stringBuilder.append(String.format(Constant.DECIMAL_THREE_PLACES_FORMATTER, tourDistance));
		stringBuilder.append(Constant.DASH);
		stringBuilder.append(timestamp);
		stringBuilder.append(Constant.CSV_EXTENSION);
		
		String expectedFileName = stringBuilder.toString();
		
		stringBuilder = new StringBuilder();
		stringBuilder.append(Constant.TMP_DIRECTORY);
		stringBuilder.append(Constant.SLASH);
		stringBuilder.append(expectedFileName);
		
		String expectedCompleteFilePath = stringBuilder.toString();
    	
    	String fileName = tspOutput.getFileName();
    	String completeFilePath = tspOutput.getCompleteFilePath();
    	
    	assertEquals(expectedFileName, fileName);
    	assertEquals(expectedCompleteFilePath, completeFilePath);
    }
    
    @Test
    public void writeFilePathAndFileNameCheck() {
    	String expectedFileName = Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING;
		
    	StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Constant.TMP_DIRECTORY);
		stringBuilder.append(Constant.SLASH);
		stringBuilder.append(expectedFileName);
		
		String expectedCompleteFilePath = stringBuilder.toString();
    	
		CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
		TSPOutput tspOutput = csvService.writeFile(Collections.emptyList(), expectedFileName);
		
    	String fileName = tspOutput.getFileName();
    	String completeFilePath = tspOutput.getCompleteFilePath();
    	
    	assertEquals(expectedFileName, fileName);
    	assertEquals(expectedCompleteFilePath, completeFilePath);
    }
    
    @Test
    public void generateOutputFileCreatedCheck() {
    	List<Point> points = Collections.emptyList();
		String algorithmName = Constant.KEY_IDENTIFIER_ANT_COLONY;
		double percentage  = 31;
		double tourDistance = 25;
		
		CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
    	TSPOutput tspOutput = csvService.generateOutputFile(points, algorithmName, percentage, tourDistance);
    	
    	String completeFilePath = tspOutput.getCompleteFilePath();
    	File file = new File(completeFilePath);
    	
    	assertTrue(file.exists());
    }
    
    @Test
    public void writeFileCreatedCheck() {
    	CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
    	TSPOutput tspOutput = csvService.writeFile(Collections.emptyList(), Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING);
    	
    	String completeFilePath = tspOutput.getCompleteFilePath();
    	File file = new File(completeFilePath);
    	
    	assertTrue(file.exists());
    }
    
    @Test
    public void generateOutputFileEmptyDataFileContentTest() {
    	fileCreatedWithProperContentTest(Constant.TEST_DATA_FILE_EMPTY);
    }
    
    @Test
    public void generateOutputFileSmallDataFileContentTest() {
    	fileCreatedWithProperContentTest(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void generateOutputFileBigDataFileContentTest() {
    	fileCreatedWithProperContentTest(Constant.TEST_DATA_FILE_BIG);
    }
    
    @Test
    public void writeFileEmptyDataFileContentTest() {
    	fileWrittenWithProperContentTest(Constant.TEST_DATA_FILE_EMPTY);
    }
    
    @Test
    public void writeFileSmallDataFileContentTest() {
    	fileWrittenWithProperContentTest(Constant.TEST_DATA_FILE_SMALL);
    }
    
    @Test
    public void writeFileBigDataFileContentTest() {
    	fileWrittenWithProperContentTest(Constant.TEST_DATA_FILE_BIG);
    }
    
	private void fileCreatedWithProperContentTest(String dataFileName) {
    	CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(dataFileName);
    	
    	String algorithmName = Constant.KEY_IDENTIFIER_ANT_COLONY;
		double percentage  = 31;
		double tourDistance = 25;
		
		CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
    	TSPOutput tspOutput = csvService.generateOutputFile(points, algorithmName, percentage, tourDistance);
    	
    	String completeFilePath = tspOutput.getCompleteFilePath();
    	
    	List<Point> fileReadedPoints = csvParserService.parsePoints(completeFilePath);
    	
    	assertEquals(fileReadedPoints.size(), points.size());
    	
    	for (int i = 0; i < fileReadedPoints.size(); i++) {
    		Point firstPoint = points.get(i);
    		Point secondPoint = fileReadedPoints.get(i);
    		
    		assertEquals(transformCrimeID(firstPoint.getPlaceId()), secondPoint.getPlaceId());
    		assertEquals(firstPoint.getLatitude(), secondPoint.getLatitude(), 0);
    		assertEquals(firstPoint.getLongitude(), secondPoint.getLongitude(), 0);
    	}
    }
	
	private void fileWrittenWithProperContentTest(String dataFileName) {
		CSVParserService csvParserService = CSVParserServiceImpl.getInstance();
    	List<Point> points = csvParserService.parsePoints(dataFileName);
		
    	// So that every test run creates a different file
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append(Constant.KEY_IDENTIFIER_SIMULATED_ANNEALING);
    	stringBuilder.append(Constant.DASH);
    	stringBuilder.append(Constant.TEST);
    	stringBuilder.append(Constant.DASH);
    	stringBuilder.append(System.currentTimeMillis());
    	
    	String fileName = stringBuilder.toString();
    	
    	CSVWriterService csvService = CSVWriterServiceImpl.getInstance();
    	TSPOutput tspOutput = csvService.writeFile(points, fileName);
    	
    	String completeFilePath = tspOutput.getCompleteFilePath();
    	
    	List<Point> fileReadedPoints = csvParserService.parsePoints(completeFilePath);
    	
    	assertEquals(fileReadedPoints.size(), points.size());
    	
    	for (int i = 0; i < fileReadedPoints.size(); i++) {
    		Point firstPoint = points.get(i);
    		Point secondPoint = fileReadedPoints.get(i);
    		
    		assertEquals(transformCrimeID(firstPoint.getPlaceId()), secondPoint.getPlaceId());
    		assertEquals(firstPoint.getLatitude(), secondPoint.getLatitude(), 0);
    		assertEquals(firstPoint.getLongitude(), secondPoint.getLongitude(), 0);
    	}
	}
    
    private String transformCrimeID(String placeId) {
		String crimeID = trimCrimeID(placeId, 5);
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Constant.SINGLE_QUOTE);
		stringBuilder.append(crimeID);
		stringBuilder.append(Constant.SINGLE_QUOTE);
	
		return stringBuilder.toString();
	}

	private String trimCrimeID(String placeId, int trimSize) {
    	if (placeId == null || placeId.length() <= trimSize) {
			return placeId;
		}

		return placeId.substring(placeId.length() - trimSize);
	}

	private String computeTimestamp(TSPOutput tspOutput) {
    	String fileName = tspOutput.getFileName();
    	int lastDashindex = fileName.lastIndexOf(Constant.DASH);
    	int lastPeriodindex = fileName.lastIndexOf(Constant.PERIOD);
    	return fileName.substring(lastDashindex + 1, lastPeriodindex);
    }
}