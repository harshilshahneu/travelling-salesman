package edu.northeastern.info6205.tspsolver.algorithm.mst;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.info6205.tspsolver.util.HaversineDistanceUtil;
import org.junit.Before;
import org.junit.Test;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;

public class PrimsMSTTest {
    private List<Point> nodes;
    private PrimsMST mstSolver;

    @Test
    public void testGetMst() {

        nodes = new ArrayList<>();
        nodes.add(new Point("", 0, 0));
        nodes.add(new Point("", -1, 1));
        nodes.add(new Point("", 1, 0));
        nodes.add(new Point("", 1, 1));
        nodes.add(new Point("", 2, 2));
        mstSolver = new PrimsMST(nodes);

        Edge[] expectedEdges = {
                new Edge(new Point("", 0, 0), new Point("", -1, 1)),
                new Edge(new Point("", 0, 0), new Point("", 1, 0)),
                new Edge(new Point("", 1, 0), new Point("", 1, 1)),
                new Edge(new Point("", 1, 1), new Point("", 2, 2))
        };
        Edge[] actualEdges = mstSolver.getMst();

        boolean atleastOneNodeNotFound = false;
        for (Edge expectedEdge : expectedEdges) {
            boolean foundEqualNodeInternally = false;
            // Check if the element exists in listB
            for (Edge actualEdge: actualEdges) {
                if(actualEdge.equals(expectedEdge)){
                    foundEqualNodeInternally = true;
                    break;
                }
            }
            if(!foundEqualNodeInternally){
                atleastOneNodeNotFound = true;
                break;
            }
        }
        assertTrue(!atleastOneNodeNotFound);
    }

    @Test
    public void testGetMstCost() {

        nodes = new ArrayList<>();
        nodes.add(new Point("", 0, 0));
        nodes.add(new Point("", -1, 1));
        nodes.add(new Point("", 1, 0));
        nodes.add(new Point("", 1, 1));
        nodes.add(new Point("", 2, 2));
        mstSolver = new PrimsMST(nodes);

        Edge[] expectedEdges = {
                new Edge(new Point("", 0, 0), new Point("", -1, 1)),
                new Edge(new Point("", 0, 0), new Point("", 1, 0)),
                new Edge(new Point("", 1, 0), new Point("", 1, 1)),
                new Edge(new Point("", 1, 1), new Point("", 2, 2))
        };
        double expectedCost = 0;
        for (Edge edge: expectedEdges) {
            expectedCost += edge.getDistance();
        }

        double actualCost = mstSolver.getMstCost();
        assertEquals(expectedCost, actualCost, 0.000000);
    }

    @Test
    public void testGetMstCostBetweenTwoNodes() {

        nodes = new ArrayList<>();
        nodes.add(new Point("", 1, 0));
        nodes.add(new Point("", 1, 1));
        mstSolver = new PrimsMST(nodes);

        Edge[] expectedEdges = {
                new Edge(new Point("", 1, 0), new Point("", 1, 1))
        };
        double expectedCost = 0;
        for (Edge edge: expectedEdges) {
            expectedCost += edge.getDistance();
        }

        double actualCost = mstSolver.getMstCost();
        assertEquals(expectedCost, actualCost, 0.000000);
    }

    @Test
    public void testGetMstCostBetweenOneNodeOnly() {

        nodes = new ArrayList<>();
        nodes.add(new Point("", 1, 0));
        mstSolver = new PrimsMST(nodes);

        double actualCost = mstSolver.getMstCost();
        //TODO: check if assertFalse is correct.
        assertFalse(Double.isNaN(actualCost));
    }

    //TODO: Clear the doubt for empty Point List provided for MST.
    @Test
    public void testGetMstCostWithEmptyList() {

        nodes = new ArrayList<>();
        mstSolver = new PrimsMST(nodes);

        double actualCost = mstSolver.getMstCost();
        assertFalse(Double.isNaN(actualCost));
    }

}

