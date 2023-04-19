package edu.northeastern.info6205.tspsolver.util;

public class RandomNumberUtil {

	public static int generate(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
}
