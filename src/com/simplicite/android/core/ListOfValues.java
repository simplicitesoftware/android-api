/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>List of values</p>
 */
public class ListOfValues implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor</p>
	 * @param name Name
	 */
	public ListOfValues(String name) {
		items = new ArrayList<ListOfValues.Item>();
	}
	
	/**
	 * <p>Logical name</p>
	 */
	public String name;
	
	/**
	 * <p>List items</p>
	 */
	public ArrayList<Item> items;
	
	public class Item {
		/**
		 * <p>List of values item code</p>
		 */
		public String code;

		/**
		 * <p>List of values item value</p>
		 */
		public String value;

		/**
		 * <p>List of values item enabled ?</p>
		 */
		public boolean enabled;
		
		/**
		 * <p>String representation</p>
		 */
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("\tCode: " + code + "\n");
			s.append("\tValue: " + value + "\n");
			s.append("\tEnabled: " + enabled + "\n");
			return s.toString();
		}
	}
	
	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Name: " + name + "\n");
		s.append("Items:\n");
		for (Item item : items) {
			s.append(item.toString());
		}
		return s.toString();
	}
}