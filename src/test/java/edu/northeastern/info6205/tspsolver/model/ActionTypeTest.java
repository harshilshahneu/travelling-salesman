package edu.northeastern.info6205.tspsolver.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActionTypeTest {
    @Test
    public void testActionTypeValues() {
        assertEquals("clear-map", ActionType.CLEAR_MAP);
        assertEquals("add-start-point", ActionType.ADD_START_POINT);
        assertEquals("add-point-list-and-fit-bound", ActionType.ADD_POINT_LIST_AND_FIT_BOUND);
        assertEquals("add-mst-polyline-and-fit-bound", ActionType.ADD_MST_POLYLINE_AND_FIT_BOUND);
        assertEquals("clear-mst-polyline", ActionType.CLEAR_MST_POLYLINE);
        assertEquals("add-polyline-and-fit-bound", ActionType.ADD_POLYLINE_AND_FIT_BOUND);
        assertEquals("point-relaxed", ActionType.POINT_RELAXED);
        assertEquals("draw-edge", ActionType.DRAW_EDGE);
        assertEquals("change-point-color-red", ActionType.CHANGE_POINT_COLOR_RED);
        assertEquals("change-point-color-green", ActionType.CHANGE_POINT_COLOR_GREEN);
        assertEquals("draw-edge-color-green", ActionType.DRAW_EDGE_COLOR_GREEN);
    }
}
