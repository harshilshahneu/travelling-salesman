package edu.northeastern.info6205.tspsolver.algorithm.opt;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Edge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.northeastern.info6205.tspsolver.model.Point;

public class ThreeOptTest {

    private ThreeOpt threeOpt;
    private List<Point> tour;

    @BeforeEach
    public void init() {
        // create a sample tour for testing
        Point p1 = new Point("", 0, 0);
        Point p2 = new Point("", 0, 2);
        Point p3 = new Point("", 2, -1);
        Point p4 = new Point("", 3, 3);
        this.tour = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p1));
    }

    @Test
    public void testGetImprovedTour() {
        this.threeOpt = new ThreeOpt(this.tour, 1, 1000);
        this.threeOpt.improve();
        List<Point> improvedTour = this.threeOpt.getImprovedTour();
        assertNotNull(improvedTour);
        assertEquals(this.tour.size(), improvedTour.size());
        assertEquals(this.tour.get(0), improvedTour.get(0));
        assertEquals(this.tour.get(this.tour.size() - 1), improvedTour.get(improvedTour.size() - 1));
    }

//    @Test
//    public void testSwapNodes() {
//        assertFalse(this.threeOpt.swapNodes(0, 1, 2)); // swap 3 random edges that are not adjacent to each other
//        assertTrue(this.threeOpt.swapNodes(1, 2, 3)); // swap 3 edges that are adjacent to each other
//        assertTrue(this.threeOpt.swapNodes(1, 3, 2)); // swap 3 edges that are not adjacent to each other
//    }

    @Test
    public void testStrategy1() {
        double intialTourCost = getTourDistance(this.tour);
        this.threeOpt = new ThreeOpt(this.tour, 1, 1000);
        List<Point> initialTour = new ArrayList<>(this.tour);
        this.threeOpt.improve();
        List<Point> improvedTour = this.threeOpt.getImprovedTour();
        double improvedTourCost = getTourDistance(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

    @Test
    public void testStrategy2() {
        double intialTourCost = getTourDistance(this.tour);
        this.threeOpt = new ThreeOpt(this.tour, 2, 1000);
        List<Point> initialTour = new ArrayList<>(this.tour);
        this.threeOpt.improve();
        List<Point> improvedTour = this.threeOpt.getImprovedTour();
        double improvedTourCost = getTourDistance(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

    @Test
    public void testStrategy3() {
        double intialTourCost = getTourDistance(this.tour);

        this.threeOpt = new ThreeOpt(this.tour, 3, 1000);
        List<Point> initialTour = new ArrayList<>(this.tour);
        this.threeOpt.improve();
        List<Point> improvedTour = this.threeOpt.getImprovedTour();
        double improvedTourCost = getTourDistance(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

    @Test
    public void testStrategy4() {
        double intialTourCost = getTourDistance(this.tour);
        this.threeOpt = new ThreeOpt(this.tour, 4, 1000);
        List<Point> initialTour = new ArrayList<>(this.tour);
        this.threeOpt.improve();
        List<Point> improvedTour = this.threeOpt.getImprovedTour();
        double improvedTourCost = getTourDistance(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

    private double getTourDistance(List<Point> tour) {
        double tourCost = 0;
        for (int i=0; i<tour.size() - 1; i++) {
            Edge edge = new Edge(tour.get(i), tour.get(i+1));
            tourCost += edge.getDistance();
        }
        return tourCost;
    }
}

