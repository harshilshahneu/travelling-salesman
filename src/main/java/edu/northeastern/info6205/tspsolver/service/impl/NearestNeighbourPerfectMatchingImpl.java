package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;

public class NearestNeighbourPerfectMatchingImpl implements PerfectMatchingSolverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NearestNeighbourPerfectMatchingImpl.class);

    private static PerfectMatchingSolverService instance;
	
	private NearestNeighbourPerfectMatchingImpl() {
		LOGGER.info("Initialising the instance");
	}
	
	public static PerfectMatchingSolverService getInstance() {
		if (instance == null) {
			instance = new NearestNeighbourPerfectMatchingImpl();
		}
		
		return instance;
	}
    
    @Override
    public List<Edge> getMinimumWeightPerfectMatching(List<Point> points) {
        LOGGER.trace("solve nearest neighbour perfect matching for points size: {}", points.size());

        List<Edge> edges = new ArrayList<>();
        Set<Point> unvisitedPoints = new HashSet<>(points);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (!unvisitedPoints.contains(point)) {
                continue;
            }

            unvisitedPoints.remove(point);

            Point nearestPoint = getNearestNeighbour(point, unvisitedPoints);
            Edge edge = new Edge(point, nearestPoint);
            edges.add(edge);

            unvisitedPoints.remove(nearestPoint);
        }

        LOGGER.trace("nearest neighbour return edges size: {}", edges.size());
        return edges;
    }

    private Point getNearestNeighbour(Point point, Set<Point> unvisitedPoints) {
        LOGGER.trace("getNearestNeighbour for point: {}, unvisitedPoints size: {}", point, unvisitedPoints.size());

        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (Point unvisitedPoint : unvisitedPoints) {
            double distance = HaversineDistanceUtil.haversine(point, unvisitedPoint);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = unvisitedPoint;
            }
        }

        LOGGER.trace("nearestPoint: {}", nearestPoint);
        return nearestPoint;
    }

}
