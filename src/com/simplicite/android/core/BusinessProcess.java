/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

/**
 * <p>Business process</p>
 */
public class BusinessProcess extends Common {
	private static final long serialVersionUID = 1L;

	/**
	 * </p>Constructor</p>
	 * @param name Object logical name
	 * @param app Application session
	 */
	public BusinessProcess(String name, AppSession app) {
		this(name, app._login, app._password, app._baseURL, app._client);
	}

	private BusinessProcess(String name, String login, String password, String baseURL, DefaultHttpClient client) {
		super(login, password, baseURL, client);
		_serviceURI = "jsonpcsservice.jsp";
		this.name = name;
		clear();
	}
	
	/**
	 * <p>Clears all meta data and current data</p>
	 */
	public void clear() {
		id = null;
	}

	/**
	 * <p>Business process name</p>
	 */
	public String name;

	/**
	 * <p>Business process unique ID</p>
	 */
	public String id;

	protected BusinessProcess _parse(String json) throws AppException {
		try {
			JSONObject msg = (JSONObject)super._parse(json);
			String type = (String)msg.get("type");
			JSONObject res = (JSONObject)msg.get("response");
			if (res != null) {
				if (type.equals("metadata")) {
					// TODO : to be completed...
					
					return this;
				} else
					throw new AppException("Unhandled " + type + " response type");
			} else 
				throw new AppException("No response data");
		} catch (JSONException e) {
			throw new AppException("Parsing exception: " + e.getMessage());
		}
	}
}