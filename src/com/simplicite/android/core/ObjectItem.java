/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>Object item</p>
 */
public class ObjectItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Object name</p>
	 */
	public String object;
	/**
	 * <p>Row ID</p>
	 */
	public String rowid;

	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("object:" + object + "\n");
		s.append("rowid:" + rowid + "\n");
		return s.toString();
	}
}