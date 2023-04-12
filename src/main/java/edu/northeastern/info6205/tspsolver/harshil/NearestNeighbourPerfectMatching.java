package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.impl.PerfectMatchingSolverServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NearestNeighbourPerfectMatching {
    private static final Logger LOGGER = LoggerFactory.getLogger(NearestNeighbourPerfectMatching.class);

    public static List<Edge> calculatePerfectMatching(List<Point> points) {
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

    private static Point getNearestNeighbour(Point point, Set<Point> unvisitedPoints) {
        LOGGER.trace("getNearestNeighbour for point: {}, unvisitedPoints size: {}", point, unvisitedPoints.size());

        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (Point unvisitedPoint : unvisitedPoints) {
            double distance = HaversineDistance.haversine(point, unvisitedPoint);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = unvisitedPoint;
            }
        }

        LOGGER.trace("nearestPoint: {}", nearestPoint);
        return nearestPoint;
    }

}
