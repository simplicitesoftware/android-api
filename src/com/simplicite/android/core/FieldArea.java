/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>Field area defintion</p>
 */
public class FieldArea implements Serializable, Comparable<FieldArea> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Field area number
	 */
	public int area;
	/**
	 * Field area label
	 */
	public String label;
	// TODO : to be completed...

	/**
	 * <p>Comparator based on area number comparision</p>
	 */
	public int compareTo(FieldArea a) {
		return area - a.area;
	}

	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		// TODO : to be completed...
		return s.toString();
	}
}