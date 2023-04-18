package edu.northeastern.info6205.tspsolver.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.impl.TestServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestServiceTest {
    @Test
    public void springContextTest() {
    	List<Point> emptyList = new ArrayList<>();
    	TestService testService = TestServiceImpl.getInstance();
    	testService.testAsync(emptyList);
    }

    @Test
    public void instanceNotNullTest() {
        TestService tspACOThreeOptSolverService = TestServiceImpl.getInstance();
        assertNotNull(tspACOThreeOptSolverService);
    }

    @Test
    public void singletonInstanceTest() {
        TestService firstInstance = TestServiceImpl.getInstance();
        TestService secondInstance = TestServiceImpl.getInstance();
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testAsyncTest() {
        TestService testService = TestServiceImpl.getInstance();
        List<Point> points = new ArrayList<>();
        points.add(new Point(String.valueOf(0), 10.0, 10.0));
        testService.testAsync(points);
    }

    @Test
    public void testAsyncWithEmptyListTest() {
        TestService testService = TestServiceImpl.getInstance();
        List<Point> points = new ArrayList<>();
        testService.testAsync(points);
    }

    @Test
    public void testAsyncTestWithNullTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            TestService testService = TestServiceImpl.getInstance();
            testService.testAsync(null);
        });
    }

}
