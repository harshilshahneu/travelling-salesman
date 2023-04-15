package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HaversineDistanceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(edu.northeastern.info6205.tspsolver.harshil.HaversineDistanceTest.class);

    @Test
    public void haversineBetweenSamePoints() {
        LOGGER.info("haversineBetweenSamePoints()");
        Point point1 = new Point("a",42.358056, -71.063611); // Boston
        Point point2 = new Point("b",42.358056, -71.063611); // Boston

        double distance = HaversineDistanceUtil.haversine(point1, point2);
        assertEquals(0, distance);
    }

    @Test
    public void haversineBetweenDifferentPoints() {
        LOGGER.trace("haversineBetweenDifferentPoints()");
        Point point1 = new Point("a",40.712776, -74.005974); // New York City
        Point point2 = new Point("b",42.360081, -71.058884); // Los Angeles
        double expectedDistance = 306108.2254187825;
        double distance = HaversineDistanceUtil.haversine(point1, point2);
        assertEquals(expectedDistance, distance);
    }

    @Test
    public void nullPointCheck() {
        LOGGER.trace("nullPointCheck()");
        Point point1 = null;
        Point point2 = new Point("b",42.360081, -71.058884); // Los Angeles

        assertThrows(NullPointerException.class, () -> {
            HaversineDistanceUtil.haversine(point1, point2);
        });
    }

    @Test
    public void nullPointCheck2() {
        LOGGER.trace("nullPointCheck2()");
        Point point1 = new Point("b",42.360081, -71.058884); // New York City
        Point point2 = null;

        assertThrows(NullPointerException.class, () -> {
            HaversineDistanceUtil.haversine(point1, point2);
        });
    }

    @Test
    public void nullPointCheck3() {
        LOGGER.trace("nullPointCheck3()");
        Point point1 = null;
        Point point2 = null;

        assertThrows(NullPointerException.class, () -> {
            HaversineDistanceUtil.haversine(point1, point2);
        });
    }

    @Test
    public void test5() {
        LOGGER.trace("test5()");
        Point northPole = new Point("a", 90.0, 0.0);
        Point southPole = new Point("a", -90.0, 0.0);
        double distanceInMeters = HaversineDistanceUtil.haversine(northPole, southPole);
        double expectedDistance = 2.001508679602057E7; // approximate value based on online calculators
        assertEquals(expectedDistance, distanceInMeters);
    }

    @Test
    public void haversineBetweenDifferentPoints2() {
        LOGGER.trace("haversineBetweenDifferentPoints2()");
        Point point1 = new Point("a",19.0760, 72.8777); // Mumbai
        Point point2 = new Point("b",28.7041, 77.1025); // Delhi
        double expectedDistance = 1153241.2912502035;
        double distance = HaversineDistanceUtil.haversine(point1, point2);
        assertEquals(expectedDistance, distance);
    }

    @Test
    public void haversineBetweenSamePoints2() {
        LOGGER.trace("haversineBetweenSamePoints2()");
        Point point1 = new Point("a",1, 180.0);
        Point point2 = new Point("b",1, -180.0);
        double expectedDistance = 0;
        double distance = (float)HaversineDistanceUtil.haversine(point1, point2);
        /* In terms of location on the Earth's surface,
        -180 degrees longitude is equivalent to 180 degrees longitude.
        If we don't use delta, the actual value of distance is 1.5602072966913738E-9
        which is very near to 0.
         */
        assertEquals(expectedDistance, distance, 1E-8);
    }


}
