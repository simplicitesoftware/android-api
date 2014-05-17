/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>Menu domain item definition</p>
 */
public class MenuEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** Business object menu entry */
	public static final String TYPE_BUSINESSOBJECT = "object";
	/** Business object menu entry */
	public static final String TYPE_BUSINESSOBJECT_STATUS = "statusobject";
	/** External object menu entry */
	public static final String TYPE_EXTERNALOBJECT = "external";
	/** Business process menu entry */
	public static final String TYPE_BUSINESSPROCESS = "process";
	/** Business workflow menu entry */
	public static final String TYPE_BUSINESSWORKFLOW = "workflow";

	/** Business object search menu entry */
	public static final String SUBTYPE_SEARCH = "search";
	/** Business object list menu entry */
	public static final String SUBTYPE_LIST = "list";

	/**
	 * <p>Menu entry type</p>
	 */
	public String type;
	/**
	 * <p>Menu entry sub type</p>
	 */
	public String subtype;
	
	/**
	 * <p>Menu entry name</p>
	 */
	public String name;
	
	/**
	 * <p>Menu entry translated label</p>
	 */
	public String label;

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