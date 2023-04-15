package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(edu.northeastern.info6205.tspsolver.harshil.EdgeTest.class);

    @Test
    public void distanceBetweenCoordinates() {
        LOGGER.trace("distanceBetweenCoordinates()");
        Point p1 = new Point("", 0, 0);
        Point p2 = new Point("", 3, 4);
        Edge edge = new Edge(p1, p2);
        assertEquals(p1, edge.getFrom());
        assertEquals(p2, edge.getTo());
        assertEquals(555811.9416947138, edge.getDistance());
    }


    @Test
    public void distanceBetweenSameCoordinates() {
        LOGGER.trace("distanceBetweenSameCoordinates()");
        Point p1 = new Point("", 0, 0);
        Point p2 = new Point("", 0, 0);
        Edge edge = new Edge(p1, p2);
        assertEquals(p1, edge.getFrom());
        assertEquals(p2, edge.getTo());
        assertEquals(0, edge.getDistance());
    }

    @Test
    public void distanceBetweenSameCoordinates2() {
        LOGGER.trace("distanceBetweenSameCoordinates2()");
        Point p1 = new Point("", 5, 9);
        Point p2 = new Point("", 5, 9);
        Edge edge = new Edge(p1, p2);
        assertEquals(p1, edge.getFrom());
        assertEquals(p2, edge.getTo());
        assertEquals(0, edge.getDistance());
    }

    @Test
    public void compareEdgePoints() {
        LOGGER.trace("compareEdgePoints()");
        Point p1 = new Point("", 5, 5);
        Point p2 = new Point("", 15, 20);
        Point p3 = new Point("", 5, 5);
        Point p4 = new Point("", 15, 20);
        Edge edge2 = new Edge(p3, p4);
        assertEquals(p1, edge2.getFrom());
        assertEquals(p2, edge2.getTo());
    }

    @Test
    public void compareEdgePoints2() {
        LOGGER.trace("compareEdgePoints2()");
        Point p1 = new Point("a", 5, 5);
        Point p2 = new Point("b", 15, 20);
        Point p3 = new Point("c", 5, 5);
        Point p4 = new Point("d", 15, 20);
        Edge edge1 = new Edge(p1, p2);
        Edge edge2 = new Edge(p3, p4);
        assertEquals(edge1.getDistance(), edge2.getDistance());
    }

    @Test
    public void compareEdges() {
        LOGGER.trace("compareEdges()");
        Point p1 = new Point("a", 5, 5);
        Point p2 = new Point("b", 15, 20);
        Edge edge1 = new Edge(p1, p2);
        Edge edge2 = new Edge(p1, p2);
        assertNotEquals(edge1, edge2);
    }

    @Test
    public void nullPointCheck() {
        LOGGER.trace("nullPointCheck()");
        Point p1 = new Point("", 0, 0);
        Point p2 = null;
        assertThrows(NullPointerException.class, () -> {
            new Edge(p1, p2);
        });
    }

    @Test
    public void nullPointCheck2() {
        LOGGER.trace("nullPointCheck2()");

        Point p1 = null;
        Point p2 = new Point("b", 15, 20);
        assertThrows(NullPointerException.class, () -> {
            new Edge(p1, p2);
        });
    }

    @Test
    public void nullPointCheck3() {
        LOGGER.trace("nullPointCheck3()");

        Point p1 = null;
        Point p2 = null;
        assertThrows(NullPointerException.class, () -> {
            new Edge(p1, p2);
        });
    }
}

