package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Christofides {
    private Edge[] mst;
    private List<Point> oddDegreeNodes;

    public Christofides(Edge[] mst) {
        this.mst = mst;
    }

    public void findOddDegreeNodes() {
        this.oddDegreeNodes = new ArrayList<>();
        //Build a hashmap of nodes with count of edges
        Map<Point, Integer> nodeEdgeCount = new HashMap<>();

        for(int i = 0; i < mst.length; i++) {
            Point from = mst[i].from;
            Point to = mst[i].to;

            if(nodeEdgeCount.containsKey(from)) {
                nodeEdgeCount.put(from, nodeEdgeCount.get(from) + 1);
            } else {
                nodeEdgeCount.put(from, 1);
            }

            if(nodeEdgeCount.containsKey(to)) {
                nodeEdgeCount.put(to, nodeEdgeCount.get(to) + 1);
            } else {
                nodeEdgeCount.put(to, 1);
            }
        }

        //get odd degree nodes
        for(Map.Entry<Point, Integer> entry : nodeEdgeCount.entrySet()) {
            if(entry.getValue() % 2 != 0) {
                this.oddDegreeNodes.add(entry.getKey());
            }
        }
    }

    public void findPerfectMatching() {
        //using hungarian algo, with the same vertices used for rows and columns

        //build a 2x2 matrix using the odd degree nodes with the distance between them
        double[][] matrix = new double[oddDegreeNodes.size()][oddDegreeNodes.size()];
        for(int i = 0; i < oddDegreeNodes.size(); i++) {
            for(int j = 0; j < oddDegreeNodes.size(); j++) {
                if(i == j) {
                    matrix[i][j] = 999999;
                    continue;
                }
                Point n1 = oddDegreeNodes.get(i);
                Point n2 = oddDegreeNodes.get(j);
                matrix[i][j] = HaversineDistance.haversine(n1, n2);
            }
        }

        //run hungarian algo
        HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(matrix);
        int[][] assignment = hungarianAlgorithm.findOptimalAssignment();

        //get the matchings
        Edge[] perfectMatchingEdges = new Edge[this.oddDegreeNodes.size()];

        double cost = 0;
        for (int i = 0; i < assignment.length; i++) {
            System.out.print("Col " + assignment[i][0] + " => Row " + assignment[i][1] + " (" + ")");
            System.out.println();
            perfectMatchingEdges[i] = new Edge(this.oddDegreeNodes.get((int) assignment[i][0]), this.oddDegreeNodes.get((int) assignment[i][1]));
            cost += perfectMatchingEdges[i].distance;
        }

        System.out.println("Cost of perfect matching: " + cost / 2);
    }

    public void printOddDegreeNodes() {
        for(int i = 0; i < oddDegreeNodes.size(); i++) {
            System.out.println(oddDegreeNodes.get(i).getId());
        }
        System.out.println("Number of odd degree nodes: " + this.oddDegreeNodes.size());
    }

    public static void main(String[] args) throws IOException {
        List<Point> nodes = CSVLoader.loadNodesFromCleanData("src/main/resources/sample-data/crimeSample.csv");
        //build MST
        PrimsMST mstSolver = new PrimsMST(nodes);
        mstSolver.solve();
        Edge[] mst = mstSolver.getMst();

        Christofides TSPSolver = new Christofides(mst);
        TSPSolver.findOddDegreeNodes();
        TSPSolver.findPerfectMatching();
    }
}