package edu.northeastern.info6205.tspsolver.algorithm.onetree;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.MinIndexedDHeap;
import edu.northeastern.info6205.tspsolver.model.Point;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.max;

public class OneTree {
	private static final Logger LOGGER = LoggerFactory.getLogger(OneTree.class);

    private Edge[] oneTree;
    private static Edge[] maxOneTree;
    private double oneTreeCost;
    private MinIndexedDHeap<Edge> ipq;
    private static double maxOneTreeCost = 0;

    public OneTree(int n) {
        int degree = (int) Math.ceil(Math.log(n) / Math.log(2));
        this.ipq = new MinIndexedDHeap<>(max(2, degree), n);
        this.oneTreeCost = 0;
        this.oneTree = null;
    }

    public void buildOneTree(Edge[] mst, Point excludedNode, double mstCost, List<Point> nodes) {
        this.oneTreeCost = mstCost;
        for(int i = 0; i < nodes.size(); i++) {
            ipq.insert(i, new Edge(excludedNode, nodes.get(i)));
        }
        mst[mst.length - 2] = ipq.pollMinValue();
        mst[mst.length - 1] = ipq.pollMinValue();

        this.oneTree = mst;
        this.oneTreeCost += mst[mst.length - 2].getDistance() + mst[mst.length - 1].getDistance();
    }

    public double getOnetreeCost() {
        return this.oneTreeCost;
    }

    public static Edge[] getMaxOneTree(List<Point> nodes) {
        for(int i = 0; i < nodes.size(); i++) {
            //exclue first node
            List<Point> temp = new ArrayList<>(nodes);
            Point excludedNode = nodes.get(i);
            temp.remove(i);
            //build MST
            PrimsMST mstSolver = new PrimsMST(temp);
            mstSolver.solve();
            Edge[] mst = new Edge[mstSolver.getMst().length + 2];
            for(int j = 0; j < mstSolver.getMst().length; j++) {
                mst[j] = mstSolver.getMst()[j];
            }

            //build one tree
            OneTree oneTreeSolver = new OneTree(temp.size());
            oneTreeSolver.buildOneTree(mst, excludedNode, mstSolver.getMstCost(), temp);

            //One tree cost
            //LOGGER.trace("One Tree Cost: " + oneTreeSolver.getOnetreeCost());
             //update the maxOneTreeCost
            if(oneTreeSolver.oneTreeCost > OneTree.maxOneTreeCost) {
                OneTree.maxOneTreeCost = oneTreeSolver.oneTreeCost;
                OneTree.maxOneTree = oneTreeSolver.oneTree;
            }
        }
        LOGGER.trace("Lower bound using one-tree: " + OneTree.maxOneTreeCost);
        return OneTree.maxOneTree;
    }

    //TODO: Please confirm if this method is correctly implemented
    //To use this method, we will need to execute getMaxOneTree first!
    public static double getMaxOneTreeCost(){
        return maxOneTreeCost;
    }
}