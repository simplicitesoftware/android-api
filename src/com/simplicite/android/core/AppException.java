/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

/**
 * <p>Application exception</p>
 */
public class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public AppException(String message) {
		super(message);
	}
}