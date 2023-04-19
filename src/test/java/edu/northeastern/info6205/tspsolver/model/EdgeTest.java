package edu.northeastern.info6205.tspsolver.model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;

public class EdgeTest {
	
    @Test
    public void distanceBetweenCoordinatesTest() {
        Point p1 = new Point(Constant.BLANK_STRING, 0, 0);
        Point p2 = new Point(Constant.BLANK_STRING, 3, 4);
        Edge edge = new Edge(p1, p2);
        assertEquals(555811.9416947138, edge.getDistance(), 0);
    }


    @Test
    public void distanceBetweenSameCoordinatesTest() {
        Point p1 = new Point(Constant.BLANK_STRING, 0, 0);
        Point p2 = new Point(Constant.BLANK_STRING, 0, 0);
        Edge edge = new Edge(p1, p2);
        assertEquals(0, edge.getDistance(), 0);
    }

    @Test
    public void compareEdgeFromPointTest() {
        Point firstPoint = new Point(Constant.BLANK_STRING, 5, 5);
        Point secondPoint = new Point(Constant.BLANK_STRING, 15, 20);
        Edge edge2 = new Edge(firstPoint, secondPoint);
        assertEquals(firstPoint, edge2.getFrom());
    }
    
    @Test
    public void compareEdgeToPointTest() {
        Point firstPoint = new Point(Constant.BLANK_STRING, 5, 5);
        Point secondPoint = new Point(Constant.BLANK_STRING, 15, 20);
        Edge edge2 = new Edge(firstPoint, secondPoint);
        assertEquals(secondPoint, edge2.getTo());
    }
    
    @Test
    public void compareEdgePointsTest() {
        Point firstPoint = new Point(Constant.BLANK_STRING, 5, 5);
        Point secondPoint = new Point(Constant.BLANK_STRING, 15, 20);
        Edge edge2 = new Edge(firstPoint, secondPoint);
        assertEquals(firstPoint, edge2.getFrom());
        assertEquals(secondPoint, edge2.getTo());
    }

    @Test
    public void compareSameCoordinatePointsDifferentEdgesTest() {
        Point firstSetFirstPoint = new Point(Constant.BLANK_STRING, 5, 5);
        Point firstSetSecondPoint = new Point(Constant.BLANK_STRING, 15, 20);
        
        Point secondSetFirstPoint = new Point(Constant.BLANK_STRING, 5, 5);
        Point secondSetSecondPoint = new Point(Constant.BLANK_STRING, 15, 20);
        
        Edge firstEdge = new Edge(firstSetFirstPoint, firstSetSecondPoint);
        Edge secondEdge = new Edge(secondSetFirstPoint, secondSetSecondPoint);
        
        assertEquals(firstEdge.getDistance(), secondEdge.getDistance(), 0);
    }

    @Test
    public void compareSamePointsDifferentEdgesTest() {
        Point firstPoint = new Point(Constant.DASH, 5, 5);
        Point secondPoint = new Point(Constant.DASH, 15, 20);
        
        Edge firstEdge = new Edge(firstPoint, secondPoint);
        Edge secondEdge = new Edge(firstPoint, secondPoint);
        
        assertEquals(firstEdge, secondEdge);
    }

    @Test
    public void sourceNullPointCheck() {
        Point source = new Point(Constant.BLANK_STRING, 0, 0);
        Point destination = null;
        assertThrows(NullPointerException.class, () -> {
            new Edge(source, destination);
        });
    }

    @Test
    public void destinationNullPointCheck() {
        Point source = null;
        Point destination = new Point(Constant.BLANK_STRING, 15, 20);
        assertThrows(NullPointerException.class, () -> {
            new Edge(source, destination);
        });
    }

    @Test
    public void bothNullPointCheck() {
        Point source = null;
        Point destination = null;
        assertThrows(NullPointerException.class, () -> {
            new Edge(source, destination);
        });
    }
    
}

