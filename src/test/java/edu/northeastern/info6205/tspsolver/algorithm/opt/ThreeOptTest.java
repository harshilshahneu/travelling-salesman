package edu.northeastern.info6205.tspsolver.algorithm.opt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

public class ThreeOptTest {

    private List<Point> tour;

    @BeforeEach
    public void init() {
        // create a sample tour for testing
        Point p1 = new Point(Constant.BLANK_STRING, 0, 0);
        Point p2 = new Point(Constant.BLANK_STRING, 0, 2);
        Point p3 = new Point(Constant.BLANK_STRING, 2, -1);
        Point p4 = new Point(Constant.BLANK_STRING, 3, 3);
        this.tour = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p1));
    }

    @Test
    public void testGetImprovedTour() {
    	ThreeOpt threeOpt = new ThreeOpt(this.tour, 1, 1000);
        threeOpt.improve();
        List<Point> improvedTour = threeOpt.getImprovedTour();
        assertNotNull(improvedTour);
        assertEquals(this.tour.size(), improvedTour.size());
        assertEquals(this.tour.get(0), improvedTour.get(0));
        assertEquals(this.tour.get(this.tour.size() - 1), improvedTour.get(improvedTour.size() - 1));
    }

    @Test
    public void testStrategy1() {
        double intialTourCost = PointUtil.getTotalCost(tour);
        ThreeOpt threeOpt = new ThreeOpt(this.tour, 1, 10);
        threeOpt.improve();
        List<Point> improvedTour = threeOpt.getImprovedTour();
        double improvedTourCost = PointUtil.getTotalCost(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

    @Test
    public void testStrategy2() {
        double intialTourCost = PointUtil.getTotalCost(tour);
        ThreeOpt threeOpt = new ThreeOpt(this.tour, 2, 10);
        threeOpt.improve();
        List<Point> improvedTour = threeOpt.getImprovedTour();
        double improvedTourCost = PointUtil.getTotalCost(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

    @Test
    public void testStrategy3() {
        double intialTourCost = PointUtil.getTotalCost(this.tour);
        ThreeOpt threeOpt = new ThreeOpt(this.tour, 3, 10);
        threeOpt.improve();
        List<Point> improvedTour = threeOpt.getImprovedTour();
        double improvedTourCost = PointUtil.getTotalCost(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

    @Test
    public void testStrategy4() {
        double intialTourCost = PointUtil.getTotalCost(tour);
        ThreeOpt threeOpt = new ThreeOpt(this.tour, 4, 1000);
        threeOpt.improve();
        List<Point> improvedTour = threeOpt.getImprovedTour();
        double improvedTourCost = PointUtil.getTotalCost(improvedTour);
        assertTrue(intialTourCost > improvedTourCost);
    }

}
