package edu.northeastern.info6205.tspsolver.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;

public class PointTest {
	
	@Test
    public void sameCoordinatesEqualTest() {
        Point firstPoint = new Point(Constant.APP_NAME, 1.0, 18.0);
        Point secondPoint = new Point(Constant.APP_NAME, 1.0, 18.0);
        assertEquals(firstPoint, secondPoint);
    }

    @Test
    public void sameCoordinatesDifferentIDNotEqualTest() {
        Point firstPoint = new Point(Constant.APP_NAME, 1.0, 18.0);
        Point secondPoint = new Point(Constant.DASH, 1.0, 18.0);
        assertNotEquals(firstPoint, secondPoint);
    }
	
    @Test
    public void invalidLargeLatitudeTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, 100.0, 0.0);
        });
    }
    
    @Test
    public void invalidLargeLongitudeTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, 45.0, 230.0);
        });
    }
    
    @Test
    public void invalidSmallLatitudeTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, -110.0, 0.0);
        });
    }
    
    @Test
    public void invalidSmallLongitudeTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, 45.0, -220.0);
        });
    }
    
    @Test
    public void invalidLatitudeLessThanBoundaryTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, -91.0, 40.0);
        });
    }
    
    @Test
    public void invalidLongitudeLessThanBoundaryTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, 45.0, -181.0);
        });
    }
    
    @Test
    public void invalidLatitudeMoreThanBoundaryTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, 91.0, 40.0);
        });
    }
    
    @Test
    public void invalidLongitudeMoreThanBoundaryTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(Constant.BLANK_STRING, 45.0, 181.0);
        });
    }
    
//  Will validate that least value for latitude is -90
    @Test
    public void latitudeOnNegativeBoundaryTest() {
    	new Point(Constant.BLANK_STRING, -90.0, 0);
    }
    
//  Will validate that least value for longitude is -180
    @Test
    public void longitudeOnNegativeBoundaryTest() {
    	new Point(Constant.BLANK_STRING, 45.0, -180.0);
    }
    
 // Will validate that max value for latitude is 90
    @Test
    public void latitudeOnPostiveBoundaryTest() {
    	new Point(Constant.BLANK_STRING, 90.0, 0);
    }

//    Will validate that max value for longitude is 180
    @Test
    public void longitudeOnPositiveBoundaryTest() {
    	new Point(Constant.BLANK_STRING, 45.0, 180.0);
    }
}
