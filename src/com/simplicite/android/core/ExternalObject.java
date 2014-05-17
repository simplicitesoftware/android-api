/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>External object</p>
 */
public class ExternalObject implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Unique external object ID</p>
	 */
	public String id;
	/**
	 * <p>Logical name</p>
	 */
	public String name;
	
	/**
	 * <p>Translated label</p>
	 */
	public String label;
	/**
	 * <p>Translated contextual help</p>
	 */
	public String help;
	/**
	 * <p>Icon code</p>
	 */
	public String icon;

	/**
	 * <p>URL</p>
	 */
	public String url;
	/**
	 * <p>Mime type</p>
	 */
	public String mime;

	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Id: " + id + "\n");
		s.append("Name: " + name + "\n");
		s.append("Label: " + label + "\n");
		s.append("Icon: " + icon + "\n");
		s.append("url: " + url + "\n");
		s.append("mime: " + mime + "\n");
		return s.toString();
	}
}