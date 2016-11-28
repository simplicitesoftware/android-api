/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>Menu domain definition</p>
 */
public class MenuDomain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Menu domain name</p>
	 */
	public String name;

	/**
	 * <p>Menu domain translated label</p>
	 */
	public String label;

	/**
	 * <p>Menu domain entries</p>
	 */
	public ArrayList<MenuEntry> entries;

	/**
	 * <p>Constructor</p>
	 */
	public MenuDomain() {
		entries = new ArrayList<MenuEntry>();
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