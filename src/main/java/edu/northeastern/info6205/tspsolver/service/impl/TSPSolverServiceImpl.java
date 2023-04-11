package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import edu.northeastern.info6205.tspsolver.harshil.FluerysAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.harshil.Edge;
import edu.northeastern.info6205.tspsolver.harshil.OneTree;
import edu.northeastern.info6205.tspsolver.harshil.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.JspritTSPSolverService;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;

@Service
public class TSPSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPSolverServiceImpl.class);

	@Autowired
	private JspritTSPSolverService jspritTSPSolverService;
	
	@Autowired
	private PerfectMatchingSolverService perfectMatchingSolverService;
	
	@Autowired
	private MapService mapService;
	
	@Override
	public void solveAsync(List<Point> points, int startingPointIndex) {
		LOGGER.info(
				"TSPSolverService will solve asynchronosly for points size: {}, startingPointIndex: {}", 
				points.size(),
				startingPointIndex);
		
		Runnable runnable = () -> {
			Map<Integer, Point> pointMap = new HashMap<>();
			for(Point point: points) {
				pointMap.put(Integer.parseInt(point.getId()), point);
			}

			PrimsMST primsMST = new PrimsMST(points);
			primsMST.solve();
			Edge[] edges = primsMST.getMst();
			
			LOGGER.info("MST Cost: {}", primsMST.getMstCost());
			
			List<Edge> mstEdgeList = new ArrayList<>(Arrays.asList(edges));
			while (mstEdgeList.remove(null));
			LOGGER.info("mstEdgeList size: {}", mstEdgeList.size());

			double mstCost = getTourDistance(mstEdgeList);
			LOGGER.info("mstCost: {}", mstCost);
			
			List<Point> oddDegreePoints = findOddDegreeVertices(points, mstEdgeList);
			
			List<Edge> matchingEdges = perfectMatchingSolverService.kolmogorovMatching(oddDegreePoints);
			LOGGER.trace("matchingEdges size: {}", matchingEdges.size());
			
			double totalCost = matchingEdges.stream()
                    .mapToDouble(Edge::getDistance)
                    .sum();
			LOGGER.trace("totalCost of matching edges: {}", totalCost);

			List<Edge> multigraph = new ArrayList<>();
			multigraph.addAll(mstEdgeList);
			multigraph.addAll(matchingEdges);

			FluerysAlgorithm eulerianCircuit =  new FluerysAlgorithm(multigraph.size());
			for(Edge edge: multigraph) {
				int sourceIndex = Integer.parseInt(edge.from.getId());
				int destinationIndex = Integer.parseInt(edge.to.getId());

				eulerianCircuit.addEdge(sourceIndex, destinationIndex);
			}

			eulerianCircuit.printEulerTour();
			List<int[]> circuit = eulerianCircuit.getResult();
			List<Edge> eulerianTour = new ArrayList<>();


			mapService.publishClearMap();
			for(int[] edge: circuit) {
				int sourceIndex = edge[0];
				int destinationIndex = edge[1];

				Point source = pointMap.get(sourceIndex);
				Point destination = pointMap.get(destinationIndex);

				Edge path = new Edge(source, destination);
				eulerianTour.add(path);


			}


			mapService.publishAddPointsAndFitBound(points);

			List<Point> hamiltonianCycle = new ArrayList<>();
			Set<Point> visited = new HashSet<>();
			for (Edge edge : eulerianTour) {
				Point source = edge.from;
				Point target = edge.to;

				if (visited.add(source)) {
					hamiltonianCycle.add(source);
				}

				if (visited.add(target)) {
					hamiltonianCycle.add(target);
				}
			}

			List<Edge> initialTSPTour = new ArrayList<>();
			for (int i = 0; i < hamiltonianCycle.size() - 1; i++) {
				Point source = hamiltonianCycle.get(i);
				Point destination = hamiltonianCycle.get(i + 1);
				Edge edge = new Edge(source, destination);
				initialTSPTour.add(edge);
			}

			// To complete last edge of cycle where we come back to the source
			Point source = hamiltonianCycle.get(hamiltonianCycle.size()  - 1);
			Point destination = hamiltonianCycle.get(0);
			Edge lastEdge = new Edge(source, destination);
			initialTSPTour.add(lastEdge);

//			Map<Point, List<Edge>> multigraph = new HashMap<>();
//
//			for (Point point : points) {
//				multigraph.put(point, new ArrayList<>());
//			}
//
//			for (Edge edge : mstEdgeList) {
//				Point source = edge.from;
//			    Point target = edge.to;
//
//			    multigraph.get(source).add(new Edge(source, target));
//			    multigraph.get(target).add(new Edge(target, source));
//			}
//
//			for (Edge edge : matchingEdges) {
//				Point source = edge.from;
//			    Point target = edge.to;
//
//			    multigraph.get(source).add(new Edge(source, target));
//			    multigraph.get(target).add(new Edge(target, source));
//			}
//
//			List<Edge> eulerianTour = findEulerianTour(multigraph);
//
//			List<Point> hamiltonianCycle = new ArrayList<>();
//			Set<Point> visited = new HashSet<>();
//			for (Edge edge : eulerianTour) {
//				Point source = edge.from;
//				Point target = edge.to;
//
//				if (visited.add(source)) {
//					hamiltonianCycle.add(source);
//				}
//
//				if (visited.add(target)) {
//					hamiltonianCycle.add(target);
//				}
//			}
//
//			LOGGER.trace("hamiltonianCycle size: {}", hamiltonianCycle.size());
//
//			mapService.publishClearMap();
//
//			mapService.publishAddPointsAndFitBound(hamiltonianCycle);
//
//			List<Edge> initialTSPTour = new ArrayList<>();
//			for (int i = 0; i < hamiltonianCycle.size() - 1; i++) {
//				Point source = hamiltonianCycle.get(i);
//				Point destination = hamiltonianCycle.get(i + 1);
//				Edge edge = new Edge(source, destination);
//				initialTSPTour.add(edge);
//			}
//
//			// To complete last edge of cycle where we come back to the source
//			Point source = hamiltonianCycle.get(hamiltonianCycle.size()  - 1);
//			Point destination = hamiltonianCycle.get(0);
//			Edge lastEdge = new Edge(source, destination);
//			initialTSPTour.add(lastEdge);
//
//			LOGGER.trace("initial tspTour size: {}", initialTSPTour.size());
//
			double totalTSPCost = getTourDistance(initialTSPTour);
			LOGGER.trace("totalCost of initial TSP Tour: {}", totalTSPCost);

			List<Edge> improved2OptTSPTour = improve2OPT(initialTSPTour);
			mapService.publishClearMap();
			for (Edge edge : improved2OptTSPTour) {
				mapService.publishDrawEdge(edge);
			}

			double improved2OptTSPTourCost = getTourDistance(improved2OptTSPTour);
			LOGGER.trace("totalCost of improved2OptTSPTour: {}", improved2OptTSPTourCost);

			double goldenRatio = improved2OptTSPTourCost / mstCost;
			LOGGER.trace("goldenRatio: {}", goldenRatio);

		};
		
		/*
		Runnable runnable = () -> {
			List<Point> tspPoints = jspritTSPSolverService.getTSPTour(points, startingPointIndex);
			// TODO Need to publish in web socket for visualisation
		};
		**/
		

		new Thread(runnable).start();
	}
	
	private List<Edge> improve2OPT(List<Edge> initialTSPTour) {
		List<Edge> tspTour = initialTSPTour;
				
		boolean improved = true;

		while (improved) {
			improved = false;
			for (int i = 0; i < initialTSPTour.size() - 1; i++) {
				for (int j = i + 1; j < initialTSPTour.size(); j++) {
					List<Edge> newTour = twoOptSwap(initialTSPTour, i, j);
					if (getTourDistance(newTour) < getTourDistance(initialTSPTour)) {
						LOGGER.trace("tour improved when swapping edge at {} with {}", i, j);
						tspTour = newTour;
						improved = true;
					}
				}
			}
		}
		
		return tspTour;
	}

	private List<Edge> twoOptSwap(List<Edge> initialTSPTour, int i, int j) {
		List<Edge> newTour = new ArrayList<>(initialTSPTour);

	    for (int k = i + 1, l = j; k < l; k++, l--) {
	        Edge temp = newTour.get(k);
	        newTour.set(k, newTour.get(l));
	        newTour.set(l, temp);
	    }

	    return newTour;
	}

	private double getTourDistance(List<Edge> tour) {
		double totalTSPCost = tour.stream()
                .mapToDouble(Edge::getDistance)
                .sum();
		return totalTSPCost;
	}

	private List<Edge> findEulerianTour(Map<Point, List<Edge>> multigraph) {
		List<Edge> tour = new ArrayList<>();
		Stack<Point> stack = new Stack<>();
		stack.push(multigraph.keySet().iterator().next());

		while (!stack.isEmpty()) {
			Point vertex = stack.peek();
			List<Edge> edges = multigraph.get(vertex);

			if (edges.isEmpty()) {
				stack.pop();
				if (!stack.isEmpty()) {
					Point prev = stack.peek();
					tour.add(new Edge(prev, vertex));
				}
			} else {
				Edge edge = edges.remove(0);
				stack.push(edge.to);
				tour.add(edge);
			}
		}

		return tour;
	}

	private List<Point> findOddDegreeVertices(List<Point> points, List<Edge> edges) {
	    Map<Point, Integer> vertexDegrees = new HashMap<>();
	    for (Point point : points) {
	        vertexDegrees.put(point, 0);
	    }

	    for (Edge edge : edges) {
	        vertexDegrees.put(edge.from, vertexDegrees.get(edge.from) + 1);
	        vertexDegrees.put(edge.to, vertexDegrees.get(edge.to) + 1);
	    }

	    List<Point> oddDegreeVertices = new ArrayList<>();
	    for (Map.Entry<Point, Integer> entry : vertexDegrees.entrySet()) {
	        if (entry.getValue() % 2 == 1) {
	            oddDegreeVertices.add(entry.getKey());
	        }
	    }

	    return oddDegreeVertices;
	}

}
