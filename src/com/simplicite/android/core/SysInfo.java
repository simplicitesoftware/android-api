/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;

/**
 * <p>System info</p>
 */
public class SysInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Object cache size</p>
	 */
	public int cacheobject;
	/**
	 * <p>Object cache max size</p>
	 */
	public int cacheobjectmax;
	/**
	 * <p>Grant cache size</p>
	 */
	public int cachegrant;
	/**
	 * <p>Grant cache max size</p>
	 */
	public int cachegrantmax;
	/**
	 * <p>Heap size</p>
	 */
	public long heapsize;
	/**
	 * <p>Heap free size</p>
	 */
	public long heapfreesize;
	/**
	 * <p>Heap max size</p>
	 */
	public long heapmaxsize;
	/**
	 * <p>DBDoc dir size</p>
	 */
	public long dirdbdoc;
	/**
	 * <p>Cache dir size</p>
	 */
	public long dircache;
	/**
	 * <p>Recycle bin dir size</p>
	 */
	public long dirrecyclebin;
	/**
	 * <p>Disk free</p>
	 */
	public long diskfree;

	
	/**
	 * <p>Constructor</p>
	 */
	public SysInfo() {
	}

	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Object cache: " + cacheobject + "\n");
		s.append("Object cache max: " + cacheobjectmax + "\n");
		s.append("Grant cache: " + cachegrant + "\n");
		s.append("Grant cache max: " + cachegrantmax + "\n");
		s.append("Heap size: " + heapsize + "\n");
		s.append("Heap free size: " + heapfreesize + "\n");
		s.append("Heap max size: " + heapmaxsize + "\n");
		s.append("DBDoc dir size: " + dirdbdoc + "\n");
		s.append("Cache dir size: " + dircache + "\n");
		s.append("Recyclebin dir size: " + dirrecyclebin + "\n");
		s.append("Disk free: " + diskfree + "\n");
		return s.toString();
	}
}