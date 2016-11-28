/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>User session data and rights</p>
 */
public class Grant implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * <p>User ID</p>
	 */
	public int userid;
	/**
	 * <p>User login</p>
	 */
	public String login;
	/**
	 * <p>User language</p>
	 */
	public String lang;
	/**
	 * <p>User first name</p>
	 */
	public String firstname;
	/**
	 * <p>User last name</p>
	 */
	public String lastname;
	/**
	 * <p>User email</p>
	 */
	public String email;
	/**
	 * <p>Picture</p>
	 */
	public Document picture;
	
	/**
	 * <p>User responsibilities</p>
	 */
	public ArrayList<String> responsibilities;
	
	/**
	 * <p>Constructor</p>
	 */
	public Grant() {
		responsibilities = new ArrayList<String>();
	}

	/**
	 * <p>Check wether the user has specified resposibility</p>
	 * @param responsibility Responsibility
	 */
	public boolean hasResponsibility(String responsibility) {
		return responsibilities.contains(responsibility);
	}
	
	/**
	 * <p>Checks wether user has administrative rights</p>
	 */
	public boolean isAdmin() {
		return hasResponsibility("ADMIN");
	}
	
	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Id: " + userid + "\n");
		s.append("Login: " + login + "\n");
		s.append("Language: " + lang + "\n");
		s.append("First name: " + firstname + "\n");
		s.append("Last name: " + lastname + "\n");
		s.append("Email: " + email + "\n");
		s.append("Responsibilities:\n");
		for (String r : responsibilities)
			s.append("\t" + r + "\n");
		return s.toString();
	}
}