package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.service.impl.JspritTSPSolverServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TwoOpt {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoOpt.class);

    private List<Edge> tour;

    /**
        Strategy 1 - Choose two random vertices for swap
        Strategy 2 - Choose two vertices that are adjacent to each other
        Strategy 3 - Choose two vertices that are of highest cost
        Strategy 4 - Combination of 1 and 3
     */
    private int strategy;

    //Computation power budget
    private long budget;

    public TwoOpt(List<Edge> tour, int strategy , long budget) {
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

    public List<Edge> getImprovedTour() {
        return tour;
    }

    private boolean swapEdges(int i, int j) {
        LOGGER.trace("Swapping edges {} and {}", i, j);
        //Selecting first node i.e from the edge
        Edge e1 = tour.get(i);
        Edge e2 = tour.get(j);
        Edge prevE1 = tour.get(i - 1);
        Edge prevE2 = tour.get(j - 1);

           //get the current distance
           double cost = e1.distance + e2.distance + prevE1.distance + prevE2.distance;

          //swap the from of current
          Edge improvedE1 = new Edge(e2.from, e1.to);
          Edge improvedE2 = new Edge(e1.from, e2.to);

          //swap the to of prev
          Edge improvedPrevE1 = new Edge(prevE1.from, prevE2.to);
          Edge improvedPrevE2 = new Edge(prevE2.from, prevE1.to);

          //get the new distance
          double newCost = improvedE1.distance + improvedE2.distance + improvedPrevE1.distance + improvedPrevE2.distance;

          //update the tour if new cost is less than old cost
            if(newCost < cost) {
                LOGGER.trace("Improvement found");
                LOGGER.trace("Cost before swap : {}", cost);
                LOGGER.trace("Cost after swap : {}", newCost);
                tour.set(i, improvedE1);
                tour.set(j, improvedE2);
                tour.set(i - 1, improvedPrevE1);
                tour.set(j - 1, improvedPrevE2);
                LOGGER.trace("New path E1 : {}", improvedPrevE1.from.getId() + " " + improvedPrevE1.to.getId() + " " + improvedE1.from.getId() + " " + improvedE1.to.getId());
                LOGGER.trace("New path E2 : {}", improvedPrevE2.from.getId() + " " + improvedPrevE2.to.getId() + " " + improvedE2.from.getId() + " " + improvedE2.to.getId());
                return true;
            }

        return false;
    }

    private void strategy1() {
        // Choose two random vertices for swap
        boolean improvement = true;
        while(improvement || budget > 0) {
            improvement = false;
            List<Integer> randomNumbers;
            Random random = new Random();
            do {
                randomNumbers = random.ints(2, 1, tour.size() - 1)
                        .boxed()
                        .collect(Collectors.toList());
            } while (randomNumbers.get(0) == randomNumbers.get(1));

            int randomEdge_1 = randomNumbers.get(0);
            int randomEdge_2 = randomNumbers.get(1);

            improvement = swapEdges(randomEdge_1, randomEdge_2);
            budget--;
        }
    }

    private void strategy2() {

    }

    private void strategy3() {
        
    }
}
