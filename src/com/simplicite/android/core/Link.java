/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>Link</p>
 */
public class Link implements Serializable, Comparable<Link> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Field
	 */
	public String field;
	/**
	 * Object
	 */
	public String object;
	/**
	 * Order
	 */
	public int order;
	/**
	 * Field area label
	 */
	public String label;
	/**
	 * Visible ?
	 */
	public boolean visible;
	/**
	 * Min occurs
	 */
	public int minOccurs;
	/**
	 * Max occurs
	 */
	public int maxOccurs;
	/**
	 * Cascade copy ?
	 */
	public boolean cascadeCopy;
	/**
	 * Associate ?
	 */
	public boolean associate;

	/**
	 * <p>Comparator based on link order comparision</p>
	 */
	public int compareTo(Link l) {
		return order - l.order;
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