package edu.northeastern.info6205.tspsolver.service.impl;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.TestService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;


public class TestServiceImplTest {
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
