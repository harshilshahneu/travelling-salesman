package edu.northeastern.info6205.tspsolver.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomNumberUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomNumberUtilTest.class);

    @Test
	public void test() {
    	LOGGER.info("test()");
    	// TODO Dummy test to setup test infrastructure
	}
    
    @Test
	public void test1() {
    	LOGGER.trace("test1()");
    	// TODO Dummy test to setup test infrastructure and capture failure
    	int x = 1/0;
	}
	
}
