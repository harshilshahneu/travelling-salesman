package edu.northeastern.info6205.tspsolver.algorithm.opt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Edge;
import org.junit.Test;

import edu.northeastern.info6205.tspsolver.model.Point;

public class TwoOptTest {
    @Test
    public void testStrategy1WithBudget10() {
        List<Point> tour = new ArrayList<>();
        tour.add(new Point("",0, 0));
        tour.add(new Point("",1, 0));
        tour.add(new Point("",2, 2));
        tour.add(new Point("",0, 1));
        tour.add(new Point("",1, 1));

        double initialTourCost = 0;
        for (int i=0; i<tour.size() - 1; i++) {
            Edge edge = new Edge(tour.get(i), tour.get(i+1));
            initialTourCost += edge.getDistance();
        }

        TwoOpt twoOpt = new TwoOpt(tour, 1, 10);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();

        double improvedTourCost = 0;
        for (int i=0; i<improvedTour.size() - 1; i++) {
            Edge edge = new Edge(improvedTour.get(i), improvedTour.get(i+1));
            improvedTourCost += edge.getDistance();
        }
        assertTrue(improvedTourCost < initialTourCost);
    }

    @Test
    public void testStrategy1WithBudget1() {
        List<Point> tour = new ArrayList<>();
        tour.add(new Point("",0, 0));
        tour.add(new Point("",1, 0));
        tour.add(new Point("",2, 2));
        tour.add(new Point("",0, 1));
        tour.add(new Point("",1, 1));

        double initialTourCost = 0;
        for (int i=0; i<tour.size() - 1; i++) {
            Edge edge = new Edge(tour.get(i), tour.get(i+1));
            initialTourCost += edge.getDistance();
        }

        TwoOpt twoOpt = new TwoOpt(tour, 1, 1);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();

        double improvedTourCost = 0;
        for (int i=0; i<improvedTour.size() - 1; i++) {
            Edge edge = new Edge(improvedTour.get(i), improvedTour.get(i+1));
            improvedTourCost += edge.getDistance();
        }
        assertTrue(improvedTourCost <= initialTourCost);
    }

    @Test
    public void testStrategy2() {

        List<Point> tour = new ArrayList<>();
        tour.add(new Point("",0, 0));
        tour.add(new Point("",1, 0));
        tour.add(new Point("",2, 2));
        tour.add(new Point("",0, 1));
        tour.add(new Point("",1, 1));
        TwoOpt twoOpt = new TwoOpt(tour, 2, 0);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();

        double intitalTourCost = 0;
        for (int i=0; i<tour.size() - 1; i++) {
            Edge edge = new Edge(tour.get(i), tour.get(i+1));
            intitalTourCost += edge.getDistance();
        }

        double improvedTourCost = 0;
        for (int i=0; i<improvedTour.size() - 1; i++) {
            Edge edge = new Edge(improvedTour.get(i), improvedTour.get(i+1));
            improvedTourCost += edge.getDistance();
        }

        assertTrue(intitalTourCost > improvedTourCost);
    }

    @Test
    public void testBudget() {
        List<Point> tour = new ArrayList<>(Arrays.asList(new Point("",0, 0), new Point("",1, 0), new Point("",1, 1), new Point("",0, 1)));
        TwoOpt twoOpt = new TwoOpt(tour, 1, 10);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();
        assertEquals(4, improvedTour.size());
    }
}

