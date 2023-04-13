package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * default
 * private double c = 1.0;             //number of trails
 * private double alpha = 1;           //pheromone importance
 * private double beta = 5;            //distance priority
 * private double evaporation = 0.5;
 * private double Q = 500;             //pheromone left on trail per ant
 * private double antFactor = 0.8;     //no of ants per node
 * private double randomFactor = 0.01; //introducing randomness
 * private int maxIterations = 1000;
 */

public class AntColonySimulation
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AntColonySimulation.class);

    public String s="";
    private double c = 1.0;             //number of trails
    private double alpha = 1;           //pheromone importance
    private double beta = 5;            //distance priority
    private double evaporation = 0.5;
    private double Q = 500;             //pheromone left on trail per ant
    private double antFactor = 0.8;     //no of ants per node
    private double randomFactor = 0.01; //introducing randomness
    private int maxIterations = 1000;

    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private double trails[][];
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private double probabilities[];

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    public AntColonySimulation(List<Point> points)
    {
        
                
        graph = generateRandomMatrix(points);
        numberOfCities = points.size();
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];
        
        for(int i=0;i<numberOfAnts;i++)
            ants.add(new Ant(numberOfCities));
    }

    /**
     * Generate initial solution
     */
    public double[][] generateRandomMatrix(List<Point> points) 
    {
         double[][] graph = new double[points.size()][points.size()];
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                graph[i][j] = HaversineDistance.haversine(points.get(i), points.get(j));
            }
        }
        return graph;
    }

    /**
     * Perform ant optimization
     */
    public void startAntOptimization() 
    {
        for(int i=1;i<=5;i++)
        {
            s+=("\nAttempt #" +i);
            solve();
            s+="\n";
        }
    }

    /**
     * Use this method to run the main logic
     */
    public int[] solve() 
    {
        setupAnts();
        clearTrails();
        for(int i=0;i<maxIterations;i++)
        {
            moveAnts();
            updateTrails();
            updateBest();
        }
        s+=("\nBest tour length: " + (bestTourLength - numberOfCities));
        s+=("\nBest tour order: " + Arrays.toString(bestTourOrder));

        LOGGER.trace("Best tour order: " + Arrays.toString(bestTourOrder));

        return bestTourOrder.clone();
    }

    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() 
    {
        for(int i=0;i<numberOfAnts;i++)
        {
            for(Ant ant:ants)
            {
                ant.clear();
                ant.visitCity(-1, random.nextInt(numberOfCities));
            }
        }
        currentIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() 
    {
        for(int i=currentIndex;i<numberOfCities-1;i++)
        {
            for(Ant ant:ants)
            {
                ant.visitCity(currentIndex,selectNextCity(ant));
            }
            currentIndex++;
        }
    }

    /**
     * Select next city for each ant
     */
    private int selectNextCity(Ant ant) 
    {
        LOGGER.trace("Selecting next city for ant: " + numberOfCities);
        int t = random.nextInt(numberOfCities - currentIndex);
        if (random.nextDouble() < randomFactor) 
        {
            int cityIndex=-999;
            for(int i=0;i<numberOfCities;i++)
            {
                if(i==t && !ant.visited(i))
                {
                    cityIndex=i;
                    break;
                }
            }
            LOGGER.trace("Random city selected: " + cityIndex);
            if(cityIndex!=-999)
                return cityIndex;
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfCities; i++) 
        {
            total += probabilities[i];
            if (total >= r) {
                LOGGER.trace("City selected: " + i);
                return i;
            }
           //     return i;
        }

        //return a random city from the remaining ones
        for(int i=0;i<numberOfCities;i++)
        {
            if(!ant.visited(i))
                return i;
        }
        return numberOfCities - 1;
        //should never happen
//        throw new RuntimeException("There are no other cities");
    }

    /**
     * Calculate the next city picks probabilites
     */
    public void calculateProbabilities(Ant ant) 
    {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) 
        {
            if (!ant.visited(l)) 
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
        }
        for (int j = 0; j < numberOfCities; j++) 
        {
            if (ant.visited(j)) 
                probabilities[j] = 0.0;
            else 
            {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * Update trails that ants used
     */
    private void updateTrails() 
    {
        for (int i = 0; i < numberOfCities; i++) 
        {
            for (int j = 0; j < numberOfCities; j++) 
                trails[i][j] *= evaporation;
        }
        for (Ant a : ants) 
        {
            double contribution = Q / a.trailLength(graph);
            for (int i = 0; i < numberOfCities - 1; i++)
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest() 
    {
        if (bestTourOrder == null) 
        {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0).trailLength(graph);
        }
        
        for (Ant a : ants) 
        {
            if (a.trailLength(graph) < bestTourLength) 
            {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail.clone();
            }
        }
    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails() 
    {
        for(int i=0;i<numberOfCities;i++)
        {
            for(int j=0;j<numberOfCities;j++)
                trails[i][j]=c;
        }
    }
}
