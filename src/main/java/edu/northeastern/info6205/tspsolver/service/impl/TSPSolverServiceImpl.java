package edu.northeastern.info6205.tspsolver.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.northeastern.info6205.tspsolver.harshil.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.MapService;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.service.SimulatedAnnealingService;
import edu.northeastern.info6205.tspsolver.service.TSPSolverService;
import edu.northeastern.info6205.tspsolver.util.EdgeUtil;

@Service
public class TSPSolverServiceImpl implements TSPSolverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPSolverServiceImpl.class);

	@Autowired
	private PerfectMatchingSolverService perfectMatchingSolverService;
	
	@Autowired
	private MapService mapService;
	
	@Autowired
	private SimulatedAnnealingService simulatedAnnealingService;
	
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

			double mstCost = EdgeUtil.getTotalCost(mstEdgeList);
			LOGGER.info("mstCost: {}", mstCost);
			
			List<Point> oddDegreePoints = findOddDegreeVertices(points, mstEdgeList);
			
			List<Edge> matchingEdges = perfectMatchingSolverService.kolmogorovMatching(oddDegreePoints);
			LOGGER.trace("matchingEdges size: {}", matchingEdges.size());
			
			double totalCost = EdgeUtil.getTotalCost(matchingEdges);
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

			for(int[] edge: circuit) {
				int sourceIndex = edge[0];
				int destinationIndex = edge[1];

				Point source = pointMap.get(sourceIndex);
				Point destination = pointMap.get(destinationIndex);

				Edge path = new Edge(source, destination);
				eulerianTour.add(path);
			}

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
			
			// To join last and first point to complete the cycle
			hamiltonianCycle.add(hamiltonianCycle.get(0));

			List<Edge> initialTSPTour = new ArrayList<>();
			for (int i = 0; i < hamiltonianCycle.size() - 1; i++) {
				Point source = hamiltonianCycle.get(i);
				Point destination = hamiltonianCycle.get(i + 1);
				Edge edge = new Edge(source, destination);
				initialTSPTour.add(edge);
			}
			
			double totalTSPCost = EdgeUtil.getTotalCost(initialTSPTour);
			LOGGER.trace("totalCost of initial TSP Tour: {}", totalTSPCost);

			double goldenRatioInitialTSP = totalTSPCost / mstCost;
			LOGGER.trace("goldenRatioInitialTSP: {}", goldenRatioInitialTSP);
			
			mapService.publishClearMap();
			mapService.publishAddPointsAndFitBound(points);

			ThreeOpt threeOpt = new ThreeOpt(hamiltonianCycle, 1, 1000000);
			threeOpt.improve();
			List<Point> improvedTSPList = threeOpt.getImprovedTour();

			//convert point to list of edges
			List<Edge> improvedTSPTour = new ArrayList<>();
			for (int i = 0; i < improvedTSPList.size() - 1; i++) {
				Point source = improvedTSPList.get(i);
				Point destination = improvedTSPList.get(i + 1);
				Edge edge = new Edge(source, destination);
				improvedTSPTour.add(edge);
			}

			//add last edge
			Point source = improvedTSPList.get(improvedTSPList.size() - 1);
			Point destination = improvedTSPList.get(0);
			Edge lastEdge = new Edge(source, destination);
			improvedTSPTour.add(lastEdge);

			double improvedTSPTourCost = EdgeUtil.getTotalCost(improvedTSPTour);
			LOGGER.trace("totalCost of improved TSP Tour: {}", improvedTSPTourCost);

			mapService.publishClearMap();
			for (Edge edge : improvedTSPTour) {
				mapService.publishDrawEdge(edge);
			}

			double goldenRatio = improvedTSPTourCost / mstCost;
			LOGGER.trace("goldenRatio: {}", goldenRatio);
			
			/*
			for (Edge edge : initialTSPTour) {
				mapService.publishDrawEdge(edge);
			}

			
			final double STARTING_TEMPERATURE = 1000;
			final double COOLING_RATE = 0.9995;
			
			List<Point> annealingPoints = simulatedAnnealingService.simulatedAnnealing(
					hamiltonianCycle, 
					STARTING_TEMPERATURE, 
					COOLING_RATE);

			List<Edge> annealingTour = new ArrayList<>();
			for (int i = 0; i < annealingPoints.size() - 1; i++) {
				Point source = annealingPoints.get(i);
				Point destination = annealingPoints.get(i + 1);
				Edge edge = new Edge(source, destination);
				annealingTour.add(edge);
			}
			
			double annealingCost = EdgeUtil.getTotalCost(annealingTour);
			LOGGER.trace("totalCost of annealing Tour: {}", annealingCost);
			
			for (Edge edge : annealingTour) {
				mapService.publishDrawEdge(edge);
			}
			
			double goldenRatioAnnealing = annealingCost / mstCost;
			LOGGER.trace("goldenRatioAnnealing: {}", goldenRatioAnnealing);
			*/
			/*
			TSPDynamicProgramming solver = new TSPDynamicProgramming(0, points);
			List<Integer> path = solver.getTour();
			
			List<Edge> dynamicProgrammingTour = new ArrayList<>();
			for (int i = 0; i < path.size() - 1; i++) {
				Point source = pointMap.get(path.get(i));
				Point destination = pointMap.get(path.get(i + 1));
				Edge edge = new Edge(source, destination);
				dynamicProgrammingTour.add(edge);
			}
			
			for (Edge edge : dynamicProgrammingTour) {
				mapService.publishDrawEdge(edge);
			}
			
			double dynamicProgrammingCost = EdgeUtil.getTotalCost(dynamicProgrammingTour);
			LOGGER.trace("dynamicProgrammingCost: {}", dynamicProgrammingCost);
			
			double goldenRatioDynamicProgramming = dynamicProgrammingCost / mstCost;
			LOGGER.trace("goldenRatioDynamicProgramming: {}", goldenRatioDynamicProgramming);
			*/
			
		};
		
		/*
		Runnable runnable = () -> {
			List<Point> tspPoints = jspritTSPSolverService.getTSPTour(points, startingPointIndex);
		};
		**/
		

		new Thread(runnable).start();
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
