package edu.northeastern.info6205.tspsolver.model;

import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private Point from;
    private Point to;
    private double distance;

    public Edge(Point from, Point to) {
        this.from = from;
        this.to = to;
        this.distance = HaversineDistanceUtil.haversine(from, to);
    }

	public Point getFrom() {
		return from;
	}
	public void setFrom(Point from) {
		this.from = from;
	}
	public Point getTo() {
		return to;
	}
	public void setTo(Point to) {
		this.to = to;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Edge)) return false;
		Edge edge = (Edge) o;
		return Double.compare(edge.distance, distance) == 0 && from.equals(edge.from) && to.equals(edge.to);
	}
}
