package edu.northeastern.info6205.tspsolver.algorithm.mst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;

public class PrimsMSTTest {
	
    @Test
    public void getMstTest() {
    	List<Point> nodes = new ArrayList<>();
        nodes.add(new Point(Constant.BLANK_STRING, 0, 0));
        nodes.add(new Point(Constant.BLANK_STRING, -1, 1));
        nodes.add(new Point(Constant.BLANK_STRING, 1, 0));
        nodes.add(new Point(Constant.BLANK_STRING, 1, 1));
        nodes.add(new Point(Constant.BLANK_STRING, 2, 2));
        
        Edge[] expectedEdges = {
                new Edge(new Point(Constant.BLANK_STRING, 0, 0), new Point("", -1, 1)),
                new Edge(new Point(Constant.BLANK_STRING, 0, 0), new Point("", 1, 0)),
                new Edge(new Point(Constant.BLANK_STRING, 1, 0), new Point("", 1, 1)),
                new Edge(new Point(Constant.BLANK_STRING, 1, 1), new Point("", 2, 2))
        };

        PrimsMST mstSolver = new PrimsMST(nodes);
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
        
        assertFalse(atleastOneNodeNotFound);
    }

    @Test
    public void getMstCostTest() {
    	List<Point> nodes = new ArrayList<>();
        nodes.add(new Point(Constant.BLANK_STRING, 0, 0));
        nodes.add(new Point(Constant.BLANK_STRING, -1, 1));
        nodes.add(new Point(Constant.BLANK_STRING, 1, 0));
        nodes.add(new Point(Constant.BLANK_STRING, 1, 1));
        nodes.add(new Point(Constant.BLANK_STRING, 2, 2));
        
        Edge[] expectedEdges = {
                new Edge(new Point(Constant.BLANK_STRING, 0, 0), new Point(Constant.BLANK_STRING, -1, 1)),
                new Edge(new Point(Constant.BLANK_STRING, 0, 0), new Point(Constant.BLANK_STRING, 1, 0)),
                new Edge(new Point(Constant.BLANK_STRING, 1, 0), new Point(Constant.BLANK_STRING, 1, 1)),
                new Edge(new Point(Constant.BLANK_STRING, 1, 1), new Point(Constant.BLANK_STRING, 2, 2))
        };
        double expectedCost = 0;
        for (Edge edge: expectedEdges) {
            expectedCost += edge.getDistance();
        }

        PrimsMST mstSolver = new PrimsMST(nodes);
        double actualCost = mstSolver.getMstCost();
        assertEquals(expectedCost, actualCost, 0.000000);
    }

    @Test
    public void getMstCostBetweenTwoNodesTest() {
    	List<Point> nodes = new ArrayList<>();
        nodes.add(new Point(Constant.BLANK_STRING, 1, 0));
        nodes.add(new Point(Constant.BLANK_STRING, 1, 1));

        Edge[] expectedEdges = {
                new Edge(new Point(Constant.BLANK_STRING, 1, 0), new Point(Constant.BLANK_STRING, 1, 1))
        };
        
        double expectedCost = 0;
        for (Edge edge: expectedEdges) {
            expectedCost += edge.getDistance();
        }

        PrimsMST mstSolver = new PrimsMST(nodes);
        double actualCost = mstSolver.getMstCost();
        assertEquals(expectedCost, actualCost, 0.000000);
    }

    @Test
    public void getMstCostBetweenOneNodeOnlyTest() {
    	List<Point> nodes = new ArrayList<>();
        nodes.add(new Point(Constant.BLANK_STRING, 1, 0));

        PrimsMST mstSolver = new PrimsMST(nodes);
        double actualCost = mstSolver.getMstCost();
        assertEquals(actualCost, 0, 0);
    }

    @Test
    public void getMstCostWithEmptyListTest() {
    	List<Point> nodes = new ArrayList<>();
    	PrimsMST mstSolver = new PrimsMST(nodes);
        assertThrows(NegativeArraySizeException.class, () -> {
            double actualCost = mstSolver.getMstCost();
            assertFalse(Double.isNaN(actualCost));
        });
    }
}
