package edu.northeastern.info6205.tspsolver.harshil;

import edu.northeastern.info6205.tspsolver.model.Point;

import java.io.IOException;
import java.util.List;


import static java.lang.Math.max;

public class OneTree {
    private Edge[] oneTree;
    private static Edge[] maxOneTree;
    private long oneTreeCost;
    private MinIndexedDHeap<Edge> ipq;
    private static long maxOneTreeCost = 0;

    public OneTree(int n) {
        int degree = (int) Math.ceil(Math.log(n) / Math.log(2));
        this.ipq = new MinIndexedDHeap<>(max(2, degree), n);
        this.oneTreeCost = 0;
        this.oneTree = null;
    }

    public void buildOneTree(Edge[] mst, Point excludedNode, long mstCost, List<Point> nodes) {
        this.oneTreeCost = mstCost;
        for(int i = 0; i < nodes.size(); i++) {
            if(!excludedNode.getId().equals(nodes.get(i).getId())) {
                ipq.insert(i, new Edge(excludedNode, nodes.get(i)));
            }
        }
        mst[mst.length - 2] = ipq.pollMinValue();
        mst[mst.length - 1] = ipq.pollMinValue();

        this.oneTree = mst;
        this.oneTreeCost += mst[mst.length - 2].distance + mst[mst.length - 1].distance;

        //update the maxOneTreeCost
        if(this.oneTreeCost > maxOneTreeCost) {
            maxOneTreeCost = this.oneTreeCost;
            maxOneTree = this.oneTree;
        }
    }

    public void printOneTree() {
        for(int i = 0; i < oneTree.length; i++) {
            System.out.println(oneTree[i].to + " " + oneTree[i].from + " " + oneTree[i].distance);
        }
    }

    public long getOnetreeCost() {
        return this.oneTreeCost;
    }

    public Edge[] getMaxOneTree() throws IOException{
        List<Point> nodes = CSVLoader.loadNodesFromCleanData("src/main/resources/sample-data/crimeSample.csv");

        for(int i = 0; i < nodes.size(); i++) {
            //exclue first node
            Point excludedNode = nodes.get(i);
            nodes.remove(i);

            //build MST
            PrimsMST mstSolver = new PrimsMST(nodes);
            mstSolver.solve();
            Edge[] mst = mstSolver.getMst();

            //build one tree
            OneTree oneTreeSolver = new OneTree(nodes.size());
            oneTreeSolver.buildOneTree(mst, excludedNode, mstSolver.getMstCost(), nodes);

            //One tree cost
            System.out.println("One Tree Cost: " + oneTreeSolver.getOnetreeCost());
        }
        System.out.println("Lower bound using one-tree: " + OneTree.maxOneTreeCost);

        return maxOneTree;
    }
}