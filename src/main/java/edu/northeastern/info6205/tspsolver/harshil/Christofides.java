package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.PerfectMatchingSolverService;
import edu.northeastern.info6205.tspsolver.service.impl.PerfectMatchingSolverServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class Christofides {
	@Autowired
	private PerfectMatchingSolverService perfectMatchingSolverService;
    private List<Point> points;
    private List<Edge> mst;
    private List<Point> oddDegreeNodes;
    private List<Edge> matchingEdges;
    private List<Point> hamiltonianCycle;
    private List<Edge> initialTSPTour;

	public void setPoints(List<Point> points) {
		this.points = points;
	}
    private void getMst() {
        PrimsMST primsMST = new PrimsMST(points);
        primsMST.solve();
        this.mst = new ArrayList<>(Arrays.asList(primsMST.getMst()));
    }

    private void findOddDegreeNodes() {
	    Map<Point, Integer> vertexDegrees = new HashMap<>();
	    for (Point point : points) {
	        vertexDegrees.put(point, 0);
	    }

	    for (Edge edge : mst) {
	        vertexDegrees.put(edge.from, vertexDegrees.get(edge.from) + 1);
	        vertexDegrees.put(edge.to, vertexDegrees.get(edge.to) + 1);
	    }

	    oddDegreeNodes = new ArrayList<>();
	    for (Map.Entry<Point, Integer> entry : vertexDegrees.entrySet()) {
	        if (entry.getValue() % 2 == 1) {
	            oddDegreeNodes.add(entry.getKey());
	        }
	    }
	}

    private void findPerfectMatching() {
		matchingEdges = perfectMatchingSolverService.kolmogorovMatching(oddDegreeNodes);
    }

    private void getEulerianTour() {
        List<Edge> multigraph = new ArrayList<>();
        Map<Integer, Point> pointMap = new HashMap<>();
		for(Point point: points) {
			pointMap.put(Integer.parseInt(point.getId()), point);
		}


		multigraph.addAll(mst);
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

        hamiltonianCycle = new ArrayList<>();
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
    }

    public List<Point> getHamiltonianCycle() {
        return hamiltonianCycle;
    }

    public List<Edge> solve() {
        getMst();
        findOddDegreeNodes();
        findPerfectMatching();
        getEulerianTour();

        //build initial tour
        initialTSPTour = new ArrayList<>();
		for (int i = 0; i < hamiltonianCycle.size() - 1; i++) {
			Point source = hamiltonianCycle.get(i);
			Point destination = hamiltonianCycle.get(i + 1);
			Edge edge = new Edge(source, destination);
			initialTSPTour.add(edge);
		}
        return initialTSPTour;
    }
  
}