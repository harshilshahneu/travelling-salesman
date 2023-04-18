package edu.northeastern.info6205.tspsolver.model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class ActionTypeTest {
	
    @Test
    public void clearMapTest() {
        assertEquals("clear-map", ActionType.CLEAR_MAP);
    }
    
    @Test
    public void addStartPointTest() {
        assertEquals("add-start-point", ActionType.ADD_START_POINT);
    }
    
    @Test
    public void addPointListAndFitBoundTest() {
        assertEquals("add-point-list-and-fit-bound", ActionType.ADD_POINT_LIST_AND_FIT_BOUND);
    }
    
    @Test
    public void addMSTPolylineAndFitBoundTest() {
        assertEquals("add-mst-polyline-and-fit-bound", ActionType.ADD_MST_POLYLINE_AND_FIT_BOUND);
    }
    
    @Test
    public void clearMSTPoylineTest() {
        assertEquals("clear-mst-polyline", ActionType.CLEAR_MST_POLYLINE);
    }
    
    @Test
    public void addPoylineAndFitBoundTest() {
        assertEquals("add-polyline-and-fit-bound", ActionType.ADD_POLYLINE_AND_FIT_BOUND);
    }
    
    @Test
    public void pointRelaxedTest() {
        assertEquals("point-relaxed", ActionType.POINT_RELAXED);
    }
    
    @Test
    public void drawEdgeTest() {
        assertEquals("draw-edge", ActionType.DRAW_EDGE);
    }

    @Test
    public void changePointColorRedTest() {
        assertEquals("change-point-color-red", ActionType.CHANGE_POINT_COLOR_RED);
    }
    
    @Test
    public void changePointColorGreenTest() {
        assertEquals("change-point-color-green", ActionType.CHANGE_POINT_COLOR_GREEN);
    }
    
    @Test
    public void drawEdgeColorGreenTest() {
        assertEquals("draw-edge-color-green", ActionType.DRAW_EDGE_COLOR_GREEN);
    }
    
    
}
