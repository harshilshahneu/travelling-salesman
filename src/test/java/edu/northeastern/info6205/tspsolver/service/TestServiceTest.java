package edu.northeastern.info6205.tspsolver.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.northeastern.info6205.tspsolver.model.Point;
import edu.northeastern.info6205.tspsolver.service.impl.TestServiceImpl;

public class TestServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceTest.class);
    
    @Test
    public void springContextTest() {
    	LOGGER.trace("ready to start testing");
    	List<Point> emptyList = new ArrayList<>();
    	TestService testService = TestServiceImpl.getInstance();
    	testService.testAsync(emptyList);
    }

}
