package edu.northeastern.info6205.tspsolver.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;

public class HaversineDistanceUtilTest {

    @Test
    public void distanceBetweenSamePointsTest() {
        Point boston = new Point(Constant.APP_NAME, 42.358056, -71.063611);
        Point bostonDuplicate = new Point(Constant.APP_NAME, 42.358056, -71.063611);
        double distance = HaversineDistanceUtil.haversine(boston, bostonDuplicate);
        assertEquals(0, distance);
    }

    @Test
    public void distanceBetweenNewYorkAndLosAngelesTest() {
        Point newYork = new Point(Constant.APP_NAME, 40.712776, -74.005974);
        Point losAngeles = new Point(Constant.APP_NAME, 42.360081, -71.058884);
        double expectedDistance = 306108.2254187825;
        double distance = HaversineDistanceUtil.haversine(newYork, losAngeles);
        assertEquals(expectedDistance, distance);
    }
    
    @Test
    public void distanceBetweenMumbaiAndDelhiTest() {
        Point mumbai = new Point(Constant.APP_NAME, 19.0760, 72.8777);
        Point delhi = new Point(Constant.APP_NAME, 28.7041, 77.1025);
        double expectedDistance = 1153241.2912502035;
        double distance = HaversineDistanceUtil.haversine(mumbai, delhi);
        assertEquals(expectedDistance, distance);
    }
    
    @Test
    public void distanceBetweenNorthPoleSouthPoleTest() {
        Point northPole = new Point(Constant.APP_NAME, 90.0, 0.0);
        Point southPole = new Point(Constant.APP_NAME, -90.0, 0.0);
        double distanceInMeters = HaversineDistanceUtil.haversine(northPole, southPole);
        
        // approximate value based on online calculators
        double expectedDistance = 2.001508679602057E7;
        assertEquals(expectedDistance, distanceInMeters);
    }

    @Test
    public void distanceBetweenExtremeLongitudeTest() {
        Point point1 = new Point(Constant.APP_NAME, 1, 180.0);
        Point point2 = new Point(Constant.APP_NAME, 1, -180.0);
        double expectedDistance = 0;
        double distance = HaversineDistanceUtil.haversine(point1, point2);
        
        /* In terms of location on the Earth's surface,
        -180 degrees longitude is equivalent to 180 degrees longitude.
        If we don't use delta, the actual value of distance is 1.5602072966913738E-9
        which is very near to 0.
         */
        assertEquals(expectedDistance, distance, 1E-8);
    }

    @Test
    public void nullDestinationPointTest() {
        Point nullPoint = null;
        Point losAngeles = new Point(Constant.APP_NAME, 42.360081, -71.058884);

        assertThrows(NullPointerException.class, () -> {
            HaversineDistanceUtil.haversine(nullPoint, losAngeles);
        });
    }

    @Test
    public void nullSourcePointTest() {
        Point newYork = new Point(Constant.APP_NAME, 42.360081, -71.058884);
        Point nullPoint = null;

        assertThrows(NullPointerException.class, () -> {
            HaversineDistanceUtil.haversine(newYork, nullPoint);
        });
    }

    @Test
    public void nullBothPointsTest() {
        Point firstNullPoint = null;
        Point secondNullPoint = null;

        assertThrows(NullPointerException.class, () -> {
            HaversineDistanceUtil.haversine(firstNullPoint, secondNullPoint);
        });
    }

}
