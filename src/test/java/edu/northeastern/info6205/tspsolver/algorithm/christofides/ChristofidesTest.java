package edu.northeastern.info6205.tspsolver.algorithm.christofides;

import edu.northeastern.info6205.tspsolver.algorithm.mst.PrimsMST;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.util.PointUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChristofidesTest {

    //TODO: pass mumbai coordinates for this test case
    @Test
    public void testSolve() {
        // create points for testing
        Point p1 = new Point("0", 0, 0);
        Point p2 = new Point("1", 1, 1);
        Point p3 = new Point("2", 2, 0);
        Point p4 = new Point("3", 3, 1);

        List<Point> graph = new ArrayList<>(Arrays.asList(p1, p2));

        // create Christofides object and solve TSP
        Christofides christofides = new Christofides(graph);
        List<Point> chritofidesTour = christofides.solve();
        double chritofidesTourCost = PointUtil.getTotalCost(chritofidesTour);

        PrimsMST primsMst = new PrimsMST(graph);
        double mstCost = primsMst.getMstCost();

        double christofidesCostDiff = (chritofidesTourCost - mstCost);
        double percentage = (christofidesCostDiff/mstCost) * 100;
//        double goldenRatio =


//        assertTrue(percentage < 50);
        assertEquals(50, percentage, 0);


    }
}



