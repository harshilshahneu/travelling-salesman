package edu.northeastern.info6205.tspsolver.algorithm.genetic;
import java.util.*;

public class SalesmanGenome implements Comparable<SalesmanGenome> {
    List<Integer> genome;
    double[][] travelPrices;
    int startingCity;
    int numberOfCities = 0;
    double fitness;

    public SalesmanGenome(int numberOfCities, double[][] travelPrices, int startingCity){
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        genome = randomSalesman();
        fitness = this.calculateFitness();
    }

    public SalesmanGenome(List<Integer> permutationOfCities, int numberOfCities, double[][] travelPrices, int startingCity){
        genome = permutationOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        fitness = this.calculateFitness();
    }

    public double calculateFitness(){
        double fitness = 0;
        int currentCity = startingCity;
        for ( int gene : genome) {
            fitness += travelPrices[currentCity][gene];
            currentCity = gene;
        }
        fitness += travelPrices[genome.get(numberOfCities-2)][startingCity];
        return fitness;
    }

    private List<Integer> randomSalesman(){
        List<Integer> result = new ArrayList<Integer>();
        for(int i=0; i<numberOfCities; i++) {
            if(i!=startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public int getStartingCity() {
        return startingCity;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path: ");
        sb.append(startingCity);
        for ( int gene: genome ) {
            sb.append(" ");
            sb.append(gene);
        }
        sb.append(" ");
        sb.append(startingCity);
        sb.append("\nLength: ");
        sb.append(this.fitness);
        return sb.toString();
    }

	@Override
	public int compareTo(SalesmanGenome o) {
		if(this.fitness > o.getFitness())
            return 1;
        else if(this.fitness < o.getFitness())
            return -1;
        else
            return 0;
	}
	
}
