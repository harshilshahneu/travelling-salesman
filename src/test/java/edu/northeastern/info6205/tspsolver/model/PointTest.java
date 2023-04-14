package edu.northeastern.info6205.tspsolver.model;

import edu.northeastern.info6205.tspsolver.harshil.HaversineDistance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(edu.northeastern.info6205.tspsolver.model.PointTest.class);

    @Test
    public void invalidCoordinates() {
        LOGGER.trace("invalidCoordinates()");

        assertThrows(IllegalArgumentException.class, () -> {
            Point point1 = new Point("", 100.0, 0.0);
            Point point2 = new Point("", 40.712776, 40.0);
        });
    }

    @Test
    public void invalidCoordinates2() {
        LOGGER.trace("invalidCoordinates2()");

        assertThrows(IllegalArgumentException.class, () -> {
            Point point1 = new Point("", 0.0, 181.0);
            Point point2 = new Point("", 1.0, 40.0);
        });
    }

    @Test
    public void invalidCoordinates3() {
        LOGGER.trace("invalidCoordinates3()");

        assertThrows(IllegalArgumentException.class, () -> {
            Point point1 = new Point("", 0.0, 18.0);
            Point point2 = new Point("", -91.0, 40.0);
        });
    }

    @Test
    public void invalidCoordinates4() {
        LOGGER.trace("invalidCoordinates4()");

        assertThrows(IllegalArgumentException.class, () -> {
            Point point1 = new Point("", 0.0, 18.0);
            Point point2 = new Point("", -9.0, -181.0);
        });
    }

    @Test
    public void sameCoordinates() {
        LOGGER.trace("sameCoordinates()");
        Point point1 = new Point("a", 1.0, 18.0);
        Point point2 = new Point("a", 1.0, 18.0);
        assertEquals(point1, point2);
    }

    @Test
    public void sameCoordinatesDifferentId() {
        LOGGER.trace("sameCoordinates2()");
        Point point1 = new Point("a", 1.0, 18.0);
        Point point2 = new Point("b", 1.0, 18.0);
        assertNotEquals(point1, point2);
    }

}
