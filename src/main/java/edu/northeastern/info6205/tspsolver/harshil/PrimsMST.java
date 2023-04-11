package edu.northeastern.info6205.tspsolver.harshil;

import java.io.IOException;
import java.util.List;

import edu.northeastern.info6205.tspsolver.model.Point;

public class PrimsMST {
	
    //inputs
    private int n;
    private List<Point> nodes;

    //Internal
    private boolean solved;
    private boolean mstExists;
    private boolean[] visited;
    private MinIndexedDHeap<Edge> ipq;

    //Outputs
    private double minCostSum;
    private Edge[] mstEdges;


    //initialize with nodes list
    public PrimsMST(List<Point> nodes) {
        solved = false;
        mstExists = false;
        this.nodes = nodes;
    }
    // Returns the edges used in finding the minimum spanning tree,
    // or returns null if no MST exists
    public Edge[] getMst(){
        solve();
        return mstExists ? mstEdges : null;
    }

    public double getMstCost() {
        solve();
        return mstExists ? minCostSum : null;
    }

    private void relaxEdgesAtNode(int currentNodeIndex) {
        visited[currentNodeIndex] = true;

        for(int i = 0; i < nodes.size(); i++) {
            int destNodeIndex = i;

            //skip if already visited
            if(visited[destNodeIndex]) continue;

            Edge edge = new Edge(nodes.get(currentNodeIndex), nodes.get(i));
            if(ipq.contains(destNodeIndex)) {
                // Try and improve the cheapest edge at destNodeIndex with the current edge in the IPQ.
                ipq.decrease(destNodeIndex, edge);
            } else {
                //insert edge for the first time
                ipq.insert(destNodeIndex, edge);
            }
        }
    }

    // Computes the minimum spanning tree and minimum spanning tree cost.
    public void solve() {
        if(solved) return;
        solved = true;

        n = nodes.size();
        int m = n - 1, edgeCount = 0;
        visited = new boolean[n];
        //m + 2 to keep space for 2 extra edges from one tree
        mstEdges = new Edge[m];

        // The degree of the d-ary heap supporting the IPQ can greatly impact performance, especially
        // on dense graphs. The base 2 logarithm of n is a decent value based on my quick experiments
        // (even better than E/V in many cases).
        int degree = (int) Math.ceil(Math.log(n) / Math.log(2));
        ipq = new MinIndexedDHeap<>(Math.max(2, degree), n);

        // Add initial set of edges to the priority queue starting at node 0.
        relaxEdgesAtNode(0);
        
        // ApplicationContext applicationContext = TSPApplicationContext.getContext();
        // MapService mapService = applicationContext.getBean(MapService.class);
        
       // mapService.publishPointRelaxed(String.valueOf(0));

        while(!ipq.isEmpty() && edgeCount != m) {
            int destNodeIndex = ipq.peekMinKeyIndex();
            Edge edge = ipq.pollMinValue();

            mstEdges[edgeCount++] = edge;
            minCostSum += edge.distance;

         //   mapService.publishDrawEdge(edge);
            
            relaxEdgesAtNode(destNodeIndex);
            
          //  mapService.publishPointRelaxed(String.valueOf(destNodeIndex));
        }

        // Verify MST spans entire graph.
        mstExists = (edgeCount == m);
    }

    public void printMST(Edge[] edges) {
        for(int i = 0; i < edges.length; i++) {
            System.out.println(edges[i].from.getId()+ ", " + " to " + edges[i].to.getId() +  " Distance: " +edges[i].distance);
        }
    }

    public static void main(String[] args) throws IOException {
//        PrimsMST mstSolver = new PrimsMST();
//        mstSolver.nodes = CSVLoader.loadNodesFromCleanData("data/crimeSample.csv");
//        System.out.println("MST for nodes: " + mstSolver.nodes.size());
//      //  mstSolver.printMST(mstSolver.getMst());
//        System.out.println("Total MST Cost : " + mstSolver.getMstCost());
    }

}
