package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.constant.Constant;
import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.TestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestServiceImplTest {
	
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
        points.add(new Point(Constant.BLANK_STRING, 10.0, 10.0));
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
