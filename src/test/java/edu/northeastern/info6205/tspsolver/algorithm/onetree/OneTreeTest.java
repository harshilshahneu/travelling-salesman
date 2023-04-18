package edu.northeastern.info6205.tspsolver.algorithm.onetree;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OneTreeTest {

    @Test
    public void buildOneTreeTest() {
        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("", 0, 0));
        nodes.add(new Point("", 0, 1));
        nodes.add(new Point("", 1, 0));
        nodes.add(new Point("", 1, 1));
        PrimsMST mstSolver = new PrimsMST(nodes);
        mstSolver.solve();
        Edge[] mst = mstSolver.getMst();
        OneTree oneTreeSolver = new OneTree(nodes.size());
        oneTreeSolver.buildOneTree(mst, nodes.get(0), mstSolver.getMstCost(), nodes);
        double expectedOneTreeCost = 444762.7706225027;
        assertEquals(expectedOneTreeCost, oneTreeSolver.getOnetreeCost(), 0);
    }

    @Test
    public void getMaxOneTreeTest() {
        List<Point> nodes = new ArrayList<>();
        nodes.add(new Point("", 0, 0));
        nodes.add(new Point("", 0, 1));
        nodes.add(new Point("", 1, 0));
        nodes.add(new Point("", 1, 1));
        Edge[] maxOneTree = OneTree.getMaxOneTree(nodes);
        double expectedMaxOneTreeCost = 444762.77062250266;
        assertEquals(expectedMaxOneTreeCost, OneTree.getMaxOneTreeCost(), 0.0001);
        assertNotNull(maxOneTree);
    }
}
