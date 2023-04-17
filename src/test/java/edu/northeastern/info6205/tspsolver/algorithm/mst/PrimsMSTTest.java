package edu.northeastern.info6205.tspsolver.algorithm.mst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;

public class PrimsMSTTest {
    private List<Point> nodes;
    private PrimsMST mstSolver;

    @Test
    public void getMstTest() {

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
    public void getMstCostTest() {

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
    public void getMstCostBetweenTwoNodesTest() {

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
    public void getMstCostBetweenOneNodeOnlyTest() {

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

