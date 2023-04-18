package edu.northeastern.info6205.tspsolver.service;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Edge;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.impl.MapServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapServiceTest {

    @Test
    public void instanceNotNullTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        assertNotNull(mapServiceImpl);
    }

    @Test
    public void singletonInstanceTest() {
        MapService firstInstance = MapServiceImpl.getInstance();
        MapService secondInstance = MapServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void publishClearMapTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishClearMap();
    }

    @Test
    public void publishAddStartPointMarkerTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        Point point = new Point(String.valueOf(0), 10.15, 21.56);
        mapServiceImpl.publishAddStartPointMarker(point);
    }

    @Test
    public void publishAddStartPointMarkerWithNullTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishAddStartPointMarker(null);
    }

    @Test
    public void publishAddPointsAndFitBoundTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        List<Point> points = new ArrayList<>();
        points.add(new Point(String.valueOf(0), 10.0, 10.0));
        mapServiceImpl.publishAddPointsAndFitBound(points);
    }

    @Test
    public void publishAddPointsAndFitBoundWithEmptyListTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        List<Point> points = new ArrayList<>();
        mapServiceImpl.publishAddPointsAndFitBound(points);
    }

    @Test
    public void publishAddPointsAndFitBoundWithNullTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishAddPointsAndFitBound(null);
    }

    @Test
    public void publishAddMSTPolylineAndFitBoundTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(new Point("0", 10.0, 10.0), new Point("1", 20.0, 20.0)));
        mapServiceImpl.publishAddMSTPolylineAndFitBound(edges);
    }

    @Test
    public void publishAddMSTPolylineAndFitBoundWithEmptyListTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        List<Edge> edges = new ArrayList<>();
        mapServiceImpl.publishAddMSTPolylineAndFitBound(edges);
    }

    @Test
    public void publishAddMSTPolylineAndFitBoundWithNullTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishAddMSTPolylineAndFitBound(null);
    }

    @Test
    public void publishClearMSTPolylineTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishClearMSTPolyline();
    }

    @Test
    public void publishAddPolylineAndFitBoundTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        List<Point> points = new ArrayList<>();
        points.add(new Point(String.valueOf(0), 10.0, 10.0));
        mapServiceImpl.publishAddPolylineAndFitBound(points);
    }

    @Test
    public void publishAddPolylineAndFitBoundWithEmptyListTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        List<Point> points = new ArrayList<>();
        mapServiceImpl.publishAddPolylineAndFitBound(points);
    }

    @Test
    public void publishAddPolylineAndFitBoundWithNullTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishAddPolylineAndFitBound(null);
    }

    @Test
    public void publishPointRelaxedTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishPointRelaxed(Constant.APP_NAME);
    }

    @Test
    public void publishPointRelaxedWithBlankStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishPointRelaxed(Constant.BLANK_STRING);
    }

    @Test
    public void publishPointRelaxedWithNullStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishPointRelaxed(null);
    }

    @Test
    public void publishDrawEdgeTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        Edge edge = new Edge(new Point("0", 10.0, 10.0), new Point("1", 11.0, 11.0));
        mapServiceImpl.publishDrawEdge(edge);
    }

    @Test
    public void publishDrawEdgeWithNullTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishDrawEdge(null);
    }

    @Test
    public void publishChangePointColorRedTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishChangePointColorRed(Constant.APP_NAME);
    }

    @Test
    public void publishChangePointColorRedBlankStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishChangePointColorRed(Constant.BLANK_STRING);
    }

    @Test
    public void publishChangePointColorRedWithNullStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishChangePointColorRed(null);
    }

    @Test
    public void publishChangePointColorGreenTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishChangePointColorGreen(Constant.APP_NAME);
    }

    @Test
    public void publishChangePointColorGreenBlankStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishChangePointColorGreen(Constant.BLANK_STRING);
    }

    @Test
    public void publishChangePointColorGreenWithNullStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishChangePointColorGreen(null);
    }

    @Test
    public void publishAddGreenLineTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        Edge edge = new Edge(new Point(String.valueOf(0), 10.0, 10.0), new Point(String.valueOf(1), 11.0, 11.0));;
        mapServiceImpl.publishAddGreenLine(edge);
    }

    @Test
    public void publishAddGreenLineWithNullTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishAddGreenLine(null);
    }

    @Test
    public void publishOutputWithNullStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishOutput(null);
    }

    @Test
    public void publishOutputTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishOutput(Constant.APP_NAME);
    }

    @Test
    public void publishOutputBlankStringTest() {
        MapService mapServiceImpl = MapServiceImpl.getInstance();
        mapServiceImpl.publishOutput(Constant.BLANK_STRING);
    }
}
