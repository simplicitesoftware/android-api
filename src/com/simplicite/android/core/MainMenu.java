/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>Main menu definition</p>
 */
public class MainMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Menu domains</p>
	 */
	public ArrayList<MenuDomain> domains;
	
	/**
	 * <p>Constructor</p>
	 */
	public MainMenu() {
		domains = new ArrayList<MenuDomain>();
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