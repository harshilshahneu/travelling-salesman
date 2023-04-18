package edu.northeastern.info6205.tspsolver.algorithm.opt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import edu.northeastern.info6205.tspsolver.service.impl.CSVParserServiceImpl;
import edu.northeastern.info6205.tspsolver.util.PointUtil;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;

public class TwoOptTest {
	
    @Test
    public void testStrategy1WithBudget10() {
        List<Point> tour = new ArrayList<>();
        tour.add(new Point(Constant.BLANK_STRING, 0, 0));
        tour.add(new Point(Constant.BLANK_STRING, 1, 0));
        tour.add(new Point(Constant.BLANK_STRING, 2, 2));
        tour.add(new Point(Constant.BLANK_STRING, 0, 1));
        tour.add(new Point(Constant.BLANK_STRING, 1, 1));

        TwoOpt twoOpt = new TwoOpt(tour, 1, 10);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();

        assertTrue(PointUtil.getTotalCost(improvedTour) < PointUtil.getTotalCost(tour));
    }

    @Test
    public void testStrategy1WithBudget1() {
        CSVParserService csvService = CSVParserServiceImpl.getInstance();
        List<Point> tour = csvService.parsePoints(Constant.TEST_DATA_FILE_SMALL);

        TwoOpt twoOpt = new TwoOpt(tour, 1, 1);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();

        assertTrue(PointUtil.getTotalCost(improvedTour) <= PointUtil.getTotalCost(tour));
    }

    @Test
    public void strategy2OptimalInputTest() {
        List<Point> tour = new ArrayList<>();
        tour.add(new Point(Constant.BLANK_STRING, 0, 0));
        tour.add(new Point(Constant.BLANK_STRING, 1, 0));
        tour.add(new Point(Constant.BLANK_STRING, 2, 2));
        
        TwoOpt twoOpt = new TwoOpt(tour, 2, 0);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();

        assertTrue(PointUtil.getTotalCost(improvedTour) == PointUtil.getTotalCost(tour));
    }

    @Test
    public void strategy2NonOptimalInputTest() {
        List<Point> tour = new ArrayList<>();
        tour.add(new Point(Constant.BLANK_STRING, 0, 0));
        tour.add(new Point(Constant.BLANK_STRING, 1, 0));
        tour.add(new Point(Constant.BLANK_STRING, 2, 2));
        tour.add(new Point(Constant.BLANK_STRING, 0, 1));
        tour.add(new Point(Constant.BLANK_STRING, 1, 1));

        TwoOpt twoOpt = new TwoOpt(tour, 2, 0);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();

        assertTrue(PointUtil.getTotalCost(improvedTour) < PointUtil.getTotalCost(tour));
    }

    @Test
    public void testBudget() {
        List<Point> tour = new ArrayList<>(Arrays.asList(
        		new Point(Constant.BLANK_STRING, 0, 0), 
        		new Point(Constant.BLANK_STRING, 1, 0), 
        		new Point(Constant.BLANK_STRING, 1, 1), 
        		new Point(Constant.BLANK_STRING, 0, 1)));
        
        TwoOpt twoOpt = new TwoOpt(tour, 1, 10);
        twoOpt.improve();
        List<Point> improvedTour = twoOpt.getImprovedTour();
        assertEquals(4, improvedTour.size());
    }
}
