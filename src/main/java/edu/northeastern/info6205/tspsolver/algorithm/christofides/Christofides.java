package edu.northeastern.info6205.tspsolver.algorithm.christofides;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.algorithm.eulerian.FluerysAlgorithm;
import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.service.impl.KolmogorovWeightedPerfectMatchingImpl;
import edu.northeastern.info6205.tspsolver.service.impl.TSPChristofidesSolverServiceImpl;

public class Christofides {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSPChristofidesSolverServiceImpl.class);

	private List<Point> points;

	public Christofides(List<Point> points) {
		LOGGER.info("initialising christofides algorithm for points size: {}", points.size());
		this.points = points;
	}

    private List<Edge> getMst() {
    	LOGGER.info("solving the MST for points size: {}", points.size());
        PrimsMST primsMST = new PrimsMST(points);
        primsMST.solve();
        
        List<Edge> mst = Arrays.asList(primsMST.getMst());
        LOGGER.info(
    			"Generated odd mst of size: {} for points size: {}", 
    			mst.size(),
    			points.size());
        return mst;
    }

    private List<Point> findOddDegreeNodes(List<Edge> mst) {
    	LOGGER.info("finding odd degree nodes for mst size: {}", mst.size());
	    Map<Point, Integer> vertexDegrees = new HashMap<>();
	    for (Point point : points) {
	        vertexDegrees.put(point, 0);
	    }

	    for (Edge edge : mst) {
	        vertexDegrees.put(edge.getFrom(), vertexDegrees.get(edge.getFrom()) + 1);
	        vertexDegrees.put(edge.getTo(), vertexDegrees.get(edge.getTo()) + 1);
	    }

	    List<Point> oddDegreeNodes = new ArrayList<>();
	    for (Map.Entry<Point, Integer> entry : vertexDegrees.entrySet()) {
	        if (entry.getValue() % 2 == 1) {
	            oddDegreeNodes.add(entry.getKey());
	        }
	    }
	    
	    LOGGER.info(
    			"Generated odd degree nodes of size: {} for mst size: {}", 
    			oddDegreeNodes.size(),
    			mst.size());
	    return oddDegreeNodes;
	}

    private List<Edge> findPerfectMatching(List<Point> nodes) {
    	LOGGER.info("Computing the minimum weight perfect matching for nodes size: {}", nodes.size());
    	PerfectMatchingSolverService service = KolmogorovWeightedPerfectMatchingImpl.getInstance();
    	List<Edge> matchingEdges = service.getMinimumWeightPerfectMatching(nodes);
    	LOGGER.info(
    			"Generated minimum weight perfect matching of size: {} for nodes size: {}", 
    			matchingEdges.size(),
    			nodes.size());
    	return matchingEdges;
    }

    private List<Point> getEulerianTour(List<Edge> mst, List<Edge> matchingEdges) {
    	LOGGER.info(
    			"generating the Eulerian tour for mst size: {}, matchingEdges size: {}",
    			mst.size(),
    			matchingEdges.size());
        List<Edge> multigraph = new ArrayList<>();
        Map<Integer, Point> pointMap = new HashMap<>();
		for(Point point: points) {
			pointMap.put(Integer.parseInt(point.getId()), point);
		}

		multigraph.addAll(mst);
		multigraph.addAll(matchingEdges);
        FluerysAlgorithm eulerianCircuit = new FluerysAlgorithm(multigraph.size());
		for(Edge edge: multigraph) {
			int sourceIndex = Integer.parseInt(edge.getFrom().getId());
			int destinationIndex = Integer.parseInt(edge.getTo().getId());
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
			Point source = edge.getFrom();
			Point target = edge.getTo();

			if (visited.add(source)) {
				hamiltonianCycle.add(source);
			}

			if (visited.add(target)) {
				hamiltonianCycle.add(target);
			}
		}
			
		// To join last and first point to complete the cycle
		hamiltonianCycle.add(hamiltonianCycle.get(0));
		
		LOGGER.info(
    			"Generated hamiltonian cycle of size: {} for mst size: {}, matchingEdges size: {}", 
    			hamiltonianCycle.size(),
    			mst.size(),
    			matchingEdges.size());
		return hamiltonianCycle;
    }

    public List<Point> solve() {
    	LOGGER.info("Starting to solve the christofides algorithm for points size: {}", points.size());
    	
        List<Edge> mst = getMst();
        List<Point> oddDegreeNodes = findOddDegreeNodes(mst);
        List<Edge> perfectMatching = findPerfectMatching(oddDegreeNodes);
        List<Point> tour = getEulerianTour(mst, perfectMatching);

        LOGGER.info(
    			"Generated tour of size: {} for points size: {}", 
    			tour.size(),
    			points.size());
        return tour;
    }
  
}