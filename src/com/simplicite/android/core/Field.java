/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>Field definition</p>
 */
public class Field implements Serializable, Comparable<Field> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default row ID field name
	 */
	public static final String DEFAULT_ROW_ID_FIELD = "row_id";

	/**
	 * Default row ID
	 */
	public static final String DEFAULT_ROW_ID = "0";

	/**
	 * <p>Technical ID</p>
	 */
	public static final int TYPE_ID          = 0;
	/**
	 * <p>Integer</p>
	 */
	public static final int TYPE_INT         = 1;
	/**
	 * <p>Float</p>
	 */
	public static final int TYPE_FLOAT       = 2;
	/**
	 * <p>String</p>
	 */
	public static final int TYPE_STRING      = 3;
	/**
	 * <p>Date</p>
	 */
	public static final int TYPE_DATE        = 4;
	/**
	 * <p>Date and time</p>
	 */
	public static final int TYPE_DATETIME    = 5;
	/**
	 * <p>Time</p>
	 */
	public static final int TYPE_TIME        = 6;
	/**
	 * <p>Single value enumeration</p>
	 */
	public static final int TYPE_ENUM        = 7;
	/**
	 * <p>Boolean</p>
	 */
	public static final int TYPE_BOOLEAN     = 8;
	/**
	 * <p>Password</p>
	 */
	public static final int TYPE_PASSWORD    = 9;
	/**
	 * <p>URL</p>
	 */
	public static final int TYPE_URL         = 10;
	/**
	 * <p>HTML content</p>
	 */
	public static final int TYPE_HTML        = 11;
	/**
	 * <p>Email address</p>
	 */
	public static final int TYPE_EMAIL       = 12;
	/**
	 * <p>Long string</p>
	 */
	public static final int TYPE_LONG_STRING = 13;
	/**
	 * <p>Multiple values enumeration</p>
	 */
	public static final int TYPE_ENUM_MULTI  = 14;
	/**
	 * <p>String with regular expression validation</p>
	 */
	public static final int TYPE_REGEXP      = 15;
	/**
	 * <p>Document</p>
	 */
	public static final int TYPE_DOC         = 17;
	/**
	 * <p>Float (without helper)</p>
	 */
	public static final int TYPE_FLOAT_EMPTY = 18;
	/**
	 * <p>External file URL</p>
	 */
	public static final int TYPE_EXTFILE     = 19;
	/**
	 * <p>Image</p>
	 */
	public static final int TYPE_IMAGE       = 20;
	/**
	 * <p>Long string with incremental notepad behavior</p>
	 */
	public static final int TYPE_NOTEPAD     = 21;
	/**
	 * <p>Phone number</p>
	 */
	public static final int TYPE_PHONENUM    = 22;
	/**
	 * <p>Color</p>
	 */
	public static final int TYPE_COLOR	    = 23;
	/**
	 * <p>Object</p>
	 */
	public static final int TYPE_OBJECT    = 24;

	/**
	 * Not visible
	 */
	public static final int VIS_NOT = 0;
	/**
	 * Visible on lists
	 */
	public static final int VIS_LIST = 1;
	/**
	 * Visible on forms
	 */
	public static final int VIS_FORM = 2;
	/**
	 * Visible on lists and forms.
	 */
	public static final int VIS_BOTH = 3;

	/**
	 * Non searchable.
	 */
	public static final int SEARCH_NONE = 0;
	/**
	 * Searchable
	 */
	public static final int SEARCH_MONO = 1;
	/**
	 * <p>Searchable using check boxes</p>
	 */
	public static final int SEARCH_MULTI_CHECK = 2;
	/**
	 * <p>Searchable using list box</p>
	 */
	public static final int SEARCH_MULTI_LIST = 3;

	/**
	 * <p>Unique field ID</p>
	 */
	public String id;
	/**
	 * <p>Logical name</p>
	 */
	public String name;
	/**
	 * <p>Order in object</p>
	 */
	public int order;
	/**
	 * <p>Translated label</p>
	 */
	public String label;
	/**
	 * <p>Translated help</p>
	 */
	public String help;
	/**
	 * <p>Is a reference key to another object ?</p>
	 */
	public boolean refId;
	/**
	 * <p>Is a referenced field from another object ?</p>
	 */
	public boolean ref;
	/**
	 * <p>Referenced object (when field is a reference key or a referenced field)</p>
	 */
	public String refObject;
	/**
	 * <p>Referenced object (when field is a referenced field)</p>
	 */
	public String refField;
	/**
	 * <p>Is field part of the unique key ?</p>
	 */
	public boolean key;
	/**
	 * <p>Is field required ?</p>
	 */
	public boolean required;
	/**
	 * <p>Is field updatable ?</p>
	 */
	public boolean updatable;
	/**
	 * <p>Field search behavior (one of SEARCH_* constants)</p>
	 */
	public int searchable;
	/**
	 * <p>Field search order</p>
	 */
	public int searchOrder;
	/**
	 * <p>Field visibility (one of VIS_* constants)</p>
	 */
	public int visible;
	/**
	 * <p>Field rendering</p>
	 */
	public String rendering;
	/**
	 * <p>Is field an extended field ?</p>
	 */
	public boolean extended;
	/**
	 * <p>Field type (one of TYPE_* constants)</p>
	 */
	public int type;
	/**
	 * <p>Field length</p>
	 */
	public int length;
	/**
	 * <p>Field precision</p>
	 */
	public int precision;
	/**
	 * <p>Field area number</p>
	 */
	public int area;
	/**
	 * <p>Default value expression</p>
	 */
	public String defaultValue;
	
	/**
	 * <p>List of values (only for TYPE_ENUM and TYPE_ENUM_MULTI fields)</p>
	 */
	public ListOfValues listOfValues;

	public static String stringValue(Object val) { return stringValue(val, null); }
	public static String stringValue(Object val, String def) {
		if (val == null) {
			return def;
		} else if (val instanceof Double || val instanceof Boolean) {
			return val.toString();
		} else
			return (String)val;
	}
	
	public boolean isSearchable() {
		return !refId && searchable != SEARCH_NONE;
	}
	
	public boolean isListVisible() {
		return !refId && (visible == VIS_BOTH || visible == VIS_LIST);
	}
	
	public boolean isFormVisible() {
		return !refId && (visible == VIS_BOTH || visible == VIS_FORM);
	}
	
	/**
	 * <p>Comparator based on field order comparision</p>
	 */
	public int compareTo(Field field) {
		return order - field.order;
	}

	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("\tName: " + name + "\n");
		s.append("\tLabel: " + label + "\n");
		// TODO : to be completed...
		return s.toString();
	}
}