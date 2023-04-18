package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.CSVParserService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CSVParserServiceImplTest {
    @Test
    public void testGetInstance() {
        CSVParserService csvService = CSVParserServiceImpl.getInstance();
        assertNotNull(csvService);
    }

    @Test
    public void singletonInstanceTest() {
        CSVParserService firstInstance = CSVParserServiceImpl.getInstance();
        CSVParserService secondInstance = CSVParserServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testParsePoints_ValidCSV_ReturnsCorrectNumberOfPoints() {
        CSVParserService csvService = CSVParserServiceImpl.getInstance();
        List<Point> graph = csvService.parsePoints("src/test/resources/data/tsp-test-small.csv");

        List<Point> expectedGraph = new ArrayList<>();
        expectedGraph.add(new Point(String.valueOf(0),19.167546,72.780647));
        expectedGraph.add(new Point(String.valueOf(1),19.197053,72.773438));
        expectedGraph.add(new Point(String.valueOf(2),19.128951,72.718849));
        expectedGraph.add(new Point(String.valueOf(3),19.109488,72.770004));
        expectedGraph.add(new Point(String.valueOf(4),19.125383,72.667007));
        expectedGraph.add(new Point(String.valueOf(5),19.128951,72.748375));
        expectedGraph.add(new Point(String.valueOf(6),19.220721,72.747345));
        expectedGraph.add(new Point(String.valueOf(7),19.130897,72.658768));
        expectedGraph.add(new Point(String.valueOf(8),19.107541,72.648125));
        expectedGraph.add(new Point(String.valueOf(9),19.163006,72.622375));

        assertEquals(expectedGraph, graph);
    }

    @Test
    public void testParsePoints_EmptyCSV_ReturnsEmptyList() {
        CSVParserService csvService = CSVParserServiceImpl.getInstance();
        List<Point> graph = csvService.parsePoints("src/test/resources/data/tsp-test-empty.csv");
        List<Point> expectedGraph = new ArrayList<>();
        assertEquals(graph, expectedGraph);
    }
}
