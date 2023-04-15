package edu.northeastern.info6205.tspsolver.algorithm.antcolony;

public class Ant {
    public int[] trail;
    private boolean[] visited;
    private int trailSize;

    public Ant(int numberOfCities) {
        this.trail = new int[numberOfCities];
        this.visited = new boolean[numberOfCities];
        this.trailSize = numberOfCities;
    }

    public void clear() {
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
    }

    public void visitCity(int currentIndex, int city) {
        trail[currentIndex + 1] = city;
        visited[city] = true;
    }

    public boolean visited(int i) {
        return visited[i];
    }

    public double trailLength(double graph[][]) {
        double length = graph[trail[trailSize - 1]][trail[0]];
        for (int i = 0; i < trailSize - 1; i++) {
            length += graph[trail[i]][trail[i + 1]];
        }
        return length;
    }
}
