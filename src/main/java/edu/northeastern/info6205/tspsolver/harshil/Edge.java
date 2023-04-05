package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;

public class Edge implements Comparable<Edge> {
    public Point from;
    public Point to;
    public double distance;

    public Edge(Point n1, Point n2) {
        this.from = n1;
        this.to = n2;
        this.distance = HaversineDistance.haversine(n1, n2);
    }

    @Override
    public int compareTo(Edge e) {
        if(this.distance < e.distance) {
            return -1;
        } else if(this.distance > e.distance) {
            return 1;
        } else {
            return 0;
        }
    }

}
