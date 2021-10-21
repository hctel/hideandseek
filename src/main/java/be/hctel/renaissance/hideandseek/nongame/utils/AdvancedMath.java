package be.hctel.renaissance.hideandseek.nongame.utils;

/*
 * This file is a part of the Renaissance Project API
 */

public class AdvancedMath {
	/**
	 * Calculates the sum of an arithmetical series
	 * @param d the common difference
	 * @param a1 First number of serie
	 * @param n last term index
	 * @return the sum of an arithmetical series
	 */
	public static int arithmeticSum(int d, int a1, int n) {
		int an = a1+d*(n-1);
		int out = ((a1+an)*n)/2;
		return out;
	}
}
