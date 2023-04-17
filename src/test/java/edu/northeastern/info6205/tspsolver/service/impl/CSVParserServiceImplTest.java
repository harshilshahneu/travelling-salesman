package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CSVParserServiceImplTest {
    @Test
    public void testParsePoints_ValidCSV_ReturnsCorrectNumberOfPoints() {
        // Arrange
        String csvData = "id,latitude,longitude\n1,42.3601,-71.0589\n2,37.7749,-122.4194\n";
        MultipartFile multipartFile = new MockMultipartFile("test.csv", csvData.getBytes());
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();

        // Act
        List<Point> points = csvParserService.parsePoints(multipartFile);

        // Assert
        assertEquals(2, points.size());
    }

    @Test
    public void testParsePoints_EmptyCSV_ReturnsEmptyList() {
        // Arrange
        String csvData = "id,latitude,longitude\n";
        MultipartFile multipartFile = new MockMultipartFile("test.csv", csvData.getBytes());
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();

        // Act
        List<Point> points = csvParserService.parsePoints(multipartFile);

        // Assert
        assertTrue(points.isEmpty());
    }

    @Test
    public void testParsePoints_CSVWithNoLocationData_SkipsPointsWithNoLocation() {
        // Arrange
        String csvData = "id,latitude,longitude\n0,42.3601,-71.0589\n1,60.0,74.48\n2,37.7749,-122.4194\n";
        MultipartFile multipartFile = new MockMultipartFile("test.csv", csvData.getBytes());
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();

        // Act
        List<Point> points = csvParserService.parsePoints(multipartFile);

        // Assert
        assertEquals(3, points.size());
    }

    @Test
    public void testParsePoints_ValidCSV_SetsCorrectIDForPoints() {
        // Arrange
        String csvData = "id,latitude,longitude\n0,42.3601,-71.0589\n1,37.7749,-122.4194\n";
        MultipartFile multipartFile = new MockMultipartFile("test.csv", csvData.getBytes());
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();

        // Act
        List<Point> points = csvParserService.parsePoints(multipartFile);

        // Assert
        assertEquals("0", points.get(0).getId());
        assertEquals("1", points.get(1).getId());
    }

    @Test
    public void testParsePoints_ValidCSV_SetsCorrectLatitudeAndLongitudeForPoints() {
        // Arrange
        String csvData = "id,latitude,longitude\n0,42.3601,-71.0589\n1,37.7749,-122.4194\n";
        MultipartFile multipartFile = new MockMultipartFile("test.csv", csvData.getBytes());
        CSVParserService csvParserService = CSVParserServiceImpl.getInstance();

        // Act
        List<Point> points = csvParserService.parsePoints(multipartFile);

        // Assert
        assertEquals(42.3601, points.get(0).getLatitude(), 0.001);
        assertEquals(-71.0589, points.get(0).getLongitude(), 0.001);
    }
}
