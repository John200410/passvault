package org.passvault.core.utils;

/**
 * Helper class for math related operations
 *
 * @author john@chav.is 10/16/2024
 */
public class MathUtils {
	
	public static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}
	
}
