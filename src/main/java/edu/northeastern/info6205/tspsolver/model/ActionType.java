package edu.northeastern.info6205.tspsolver.model;

public class ActionType {
	public static final String CLEAR_MAP = "clear-map";
	public static final String ADD_START_POINT = "add-start-point";
	public static final String ADD_POINT_LIST_AND_FIT_BOUND = "add-point-list-and-fit-bound";
	public static final String ADD_MST_POLYLINE_AND_FIT_BOUND = "add-mst-polyline-and-fit-bound";
	public static final String CLEAR_MST_POLYLINE = "clear-mst-polyline";
	public static final String ADD_POLYLINE_AND_FIT_BOUND = "add-polyline-and-fit-bound";
	public static final String POINT_RELAXED = "point-relaxed";
	public static final String DRAW_EDGE = "draw-edge";
	
	public static final String CHANGE_POINT_COLOR_RED = "change-point-color-red";
	public static final String CHANGE_POINT_COLOR_GREEN = "change-point-color-green";
	public static final String DRAW_EDGE_COLOR_GREEN = "draw-edge-color-green";
}
