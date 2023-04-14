package edu.northeastern.info6205.tspsolver.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class RandomNumberUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomNumberUtilTest.class);

	@Test
	public void testGenerateWithinRange() {
		LOGGER.trace("testGenerateWithinRange()");
		int min = 1;
		int max = 10;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

	@Test
	public void testGenerateWithSameMinMax() {
		LOGGER.trace("testGenerateWithSameMinMax()");
		int min = 5;
		int max = 5;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertEquals(min, randomNumber);
	}

	@Test
	public void testGenerateWithSameMinMax2() {
		LOGGER.trace("testGenerateWithSameMinMax2()");
		int min = 0;
		int max = 0;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertEquals(min, randomNumber);
	}

	@Test
	public void testGenerateWithinRange2() {
		LOGGER.trace("testGenerateWithinRange2()");
		int min = 100;
		int max = 101;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

	@Test
	public void testGenerateWithinRange3() {
		LOGGER.trace("testGenerateWithinRange3()");
		int min = 100;
		int max = 1000;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

	@Test
	public void testGenerateWithinRange4() {
		LOGGER.trace("testGenerateWithinRange4()");
		int min = -1000;
		int max = -100;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

}
