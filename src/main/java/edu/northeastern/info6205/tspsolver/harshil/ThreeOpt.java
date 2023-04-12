package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.impl.JspritTSPSolverServiceImpl;
import edu.northeastern.info6205.tspsolver.util.PointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ThreeOpt {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoOpt.class);

    private List<Point> tour;

    /**
        Strategy 1 - Choose two random vertices for swap
        Strategy 2 - Choose two vertices that are adjacent to each other
        Strategy 3 - Choose two vertices that are of highest cost
        Strategy 4 - Combination of 1 and 3
     */
    private int strategy;

    //Computation power budget
    private long budget;

    public ThreeOpt(List<Point> tour, int strategy , long budget) {
        this.tour = tour;
        this.strategy = strategy;
        this.budget = budget;
    }

    public void improve() {
        if (strategy == 1) {
            LOGGER.trace("Strategy 1");
            strategy1();
        } else if (strategy == 2) {
            strategy2();
        } else if (strategy == 3) {
            strategy3();
        } 
    }

    public List<Point> getImprovedTour() {
        return tour;
    }

    private boolean swapNodes(int i, int j, int k) {
        //current tour distance
        double cost = PointUtil.getTotalCost(tour);

        //duplicate the tour
        List<Point> improvedTour_1 = new ArrayList<>(tour);
        List<Point> improvedTour_2 = new ArrayList<>(tour);
        List<Point> improvedTour_3 = new ArrayList<>(tour);
        List<Point> improvedTour_4 = new ArrayList<>(tour);
        double newCost[] = new double[4];

        //swap the nodes for tour 1
        Collections.swap(improvedTour_1, i, j);
        newCost[0] = PointUtil.getTotalCost(improvedTour_1);

        //swap the nodes for tour 2
        Collections.swap(improvedTour_2, j, k);
        newCost[1] = PointUtil.getTotalCost(improvedTour_2);

        //swap the nodes for tour 3
        Collections.swap(improvedTour_3, i, k);
        Collections.swap(improvedTour_3, j, k);
        newCost[2] = PointUtil.getTotalCost(improvedTour_3);

        //swap the nodes for tour 4
        Collections.swap(improvedTour_4, i, k);
        Collections.swap(improvedTour_4, i, j);
        newCost[3] = PointUtil.getTotalCost(improvedTour_4);

        //find the minimum cost tour
        double min = newCost[0];
        int index = 0;
        for(int l = 1; l < 4; l++) {
            if(newCost[l] < min) {
                min = newCost[l];
                index = l;
            }
        }

        //update the tour if new cost is less than old cost
        if(min < cost) {
            LOGGER.trace("Improvement found");
            LOGGER.trace("Cost before swap : {}", cost);
            LOGGER.trace("Cost after swap : {}", min);
            if(index == 0) {
                this.tour = improvedTour_1;
            } else if(index == 1) {
                this.tour = improvedTour_2;
            } else if(index == 2) {
                this.tour = improvedTour_3;
            } else {
                this.tour = improvedTour_4;
            }
            return true;
        }

        return false;
    }

    private void strategy1() {
        // Choose three random vertices for swap
        boolean improvement = true;
        while(improvement || budget > 0) {
            improvement = false;
            List<Integer> randomNumbers;
            Random random = new Random();
            do {
                randomNumbers = random.ints(3, 1, tour.size() - 1)
                        .boxed()
                        .collect(Collectors.toList());
            } while (randomNumbers.get(0) == randomNumbers.get(1) || randomNumbers.get(0) == randomNumbers.get(2) || randomNumbers.get(1) == randomNumbers.get(2));

            int randomEdge_1 = randomNumbers.get(0);
            int randomEdge_2 = randomNumbers.get(1);
            int randomEdge_3 = randomNumbers.get(2);

            improvement = swapNodes(randomEdge_1, randomEdge_2, randomEdge_3);
            budget--;
            LOGGER.trace("Budget remaining : {}", budget);
        }
    }

    private void strategy2() {
        // Choose two vertices that are adjacent to each other
       boolean improvement = true;
       while(improvement) {
           improvement = false;
           for(int i = 1; i < tour.size() - 2; i++) {
               improvement = swapNodes(i, i + 1, i + 2);
               
               if(improvement) {
                   break;
               }
           }
       }

    }

    private void strategy3() {
        // Choose two vertices that are of highest cost
    }
}
