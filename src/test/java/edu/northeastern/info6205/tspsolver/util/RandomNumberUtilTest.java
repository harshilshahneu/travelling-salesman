package edu.northeastern.info6205.tspsolver.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class RandomNumberUtilTest {

	@Test
	public void testGenerateWithinRange() {
		int min = 1;
		int max = 10;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

	@Test
	public void testGenerateWithSameMinMax() {
		int min = 5;
		int max = 5;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertEquals(min, randomNumber);
	}

	@Test
	public void testGenerateWithSameMinMax2() {
		int min = 0;
		int max = 0;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertEquals(min, randomNumber);
	}

	@Test
	public void testGenerateWithinRange2() {
		int min = 100;
		int max = 101;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

	@Test
	public void testGenerateWithinRange3() {
		int min = 100;
		int max = 1000;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

	@Test
	public void testGenerateWithinRange4() {
		int min = -1000;
		int max = -100;
		int randomNumber = RandomNumberUtil.generate(min, max);
		assertTrue(randomNumber >= min && randomNumber <= max);
	}

}
