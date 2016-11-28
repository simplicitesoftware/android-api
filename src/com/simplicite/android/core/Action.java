/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>Business object action</p>
 */
public class Action implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Name</p>
	 */
	public String name;

	/**
	 * <p>Translated label</p>
	 */
	public String label;

	/**
	 * <p>With confirmation ?</p>
	 */
	public boolean confirm;

	/**
	 * <p>Visible on list ?</p>
	 */
	public boolean listVisible;

	/**
	 * <p>Visible on list items ?</p>
	 */
	public boolean listItemVisible;

	/**
	 * <p>Visible on form ?</p>
	 */
	public boolean formVisible;

	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Name: " + name + "\n");
		s.append("Label: " + label + "\n");
		s.append("Confirm: " + confirm + "\n");
		s.append("List visible: " + listVisible + "\n");
		s.append("List item visible: " + listItemVisible + "\n");
		s.append("Form visible: " + formVisible + "\n");
		return s.toString();
	}
}