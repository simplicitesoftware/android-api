/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import org.json.*;

import android.util.Log;

/**
 * <p>Common abstract class from which all services classes inherit</p>
 */
public abstract class Common implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Tag for use in Log.x() methods</p>
	 */
	public static final String LOG_TAG = "SIMPLICITE";

	private static boolean _debug = false;
	/**
	 * <p>Set/unset debug traces</p>
	 * @param debug Debug traces ?
	 */
	public void setDebug(boolean debug) {
		_debug = debug;
	}
	/**
	 * <p>Are debug traces active ?</p>
	 */
	public static boolean isDebug() {
		return _debug;
	}

	/**
	 * <p>Generate debug trace</p>
	 * @param msg Message
	 */
	public void debug(String msg) {
		if (_debug)
			Log.d(LOG_TAG, msg == null ? "null" : msg);
	}

	private static long _timeout = 30; 
	/**
	 * <p>Set timeout</p>
	 * @param timeout Timeout (seconds)
	 */
	public void setTimeout(long timeout) {
		_timeout = timeout;
	}
	/**
	 * <p>Get timeout</p>
	 */
	public static long getTimeout() {
		return _timeout;
	}

	/**
	 * <p>Encoding</p>
	 */
	protected static String _encoding = "ISO-8859-1";
	
	/**
	 * <p>User login (null for internal usage within generic web UI)</p>
	 */
	protected String _login;
	/**
	 * <p>User password (null for internal usage within generic web UI)</p>
	 */
	protected String _password;

	/**
	 * <p>Base URL of JSON services</p>
	 */
	protected String _baseURL;
	
	/**
	 * <p>Service URI (to be specialised in each sub class)</p>
	 */
	protected String _serviceURI;

	protected DefaultHttpClient _client;

	/**
	 * <p>Constructor</p>
	 * @param login User login
	 * @param password user password
	 * @param baseURL Base URL of current application generic web services gataway (e.g. http://myserver:8080/myapp<u>ws</u>)
	 * @param client Existing HTTP client (null creates a new one)
	 */
	public Common(String login, String password, String baseURL, DefaultHttpClient client) {
		_login = login.trim();
		_password = password.trim();
		_baseURL = baseURL.trim();
		
		if (client == null) {
			_client = new DefaultHttpClient();
			if (login != null && password != null)
				_client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(login, password));
		} else
			_client = client;
	}

	private static int getCookieIndex(DefaultHttpClient client, String name) {
		List<Cookie> cookies = client.getCookieStore().getCookies();
		int i = 0;
		while (i < cookies.size()) {
            Cookie cookie = cookies.get(i);
            if (cookie.getName().equals(name))
            	return i;
            i++;
		}
		return -1;
	}
	
	protected static String getCookie(DefaultHttpClient client, String name) {
		int i = getCookieIndex(client, name);
		return i == -1 ? null : client.getCookieStore().getCookies().get(i).getValue();
	}
	
	protected static void setCookie(DefaultHttpClient client, String name, String value) {
		int i = getCookieIndex(client, name);
		if (i != -1)
			client.getCookieStore().getCookies().remove(i);
		client.getCookieStore().getCookies().add(new BasicClientCookie(name, value));
	}

	protected Object _parse(String json) throws AppException, JSONException {
		JSONObject res = new JSONObject(json);
		String type = res.getString("type");
		if (type.equals("error")) {
			JSONArray msgs = res.getJSONObject("response").optJSONArray("messages");
			if (msgs != null) {
				StringBuilder m = new StringBuilder();
				for (int i = 0; i < msgs.length(); i++)
					m.append((i == 0 ? "" : "\n") + msgs.getString(i));
				throw new AppException(m.toString());
			} else
				throw new AppException(res.getJSONObject("response").getString("message"));
		}
		return res;
	}

	protected String _get(String params) throws Exception {
		HttpGet get = new HttpGet(new URI(_baseURL + "/" + _serviceURI + (params != null && params.length() > 0 ? params : "")));
		String res = _client.execute(get, new BasicResponseHandler());
		if (_debug)
			Log.d(LOG_TAG, res);
		return res;
	}

	protected String _post(String params, HashMap<String, Object> data) throws Exception {
		HttpPost post = new HttpPost(new URI(_baseURL + "/" + _serviceURI + (params != null && params.length() > 0 ? params : "")));
		
		if (data != null) {
			ArrayList<BasicNameValuePair> p = new ArrayList<BasicNameValuePair>();
			for (String i : data.keySet()) {
				Object v = data.get(i);
				if (v instanceof Document) {
					Document d = (Document)v;
					p.add(new BasicNameValuePair(i, "id|" + d.id + "|name|" + d.name + "|content|" + d.content));
				} else if (v instanceof ObjectItem) {
					ObjectItem o = (ObjectItem)v;
					p.add(new BasicNameValuePair(i, "object|" + o.object + "|row_id|" + o.rowid));
				} else
					p.add(new BasicNameValuePair(i, v.toString()));
			}

			post.setEntity(new UrlEncodedFormEntity(p, _encoding));
		}
		
		String res = _client.execute(post, new BasicResponseHandler());
		if (_debug)
			Log.d(LOG_TAG, res);
		return res;
	}
	
	protected Document _parseDocument(JSONObject jsonObject, String key) {
		try {
			JSONObject d = jsonObject.getJSONObject(key);
			Document doc = new Document();
			doc.id = d.getString("id");
			doc.name = d.getString("name");
			doc.path = d.getString("path");
			doc.mime = d.getString("mime");
			doc.size = d.getInt("size");
			if (d.has("content"))
				doc.content = d.getString("content");
			if (d.has("thumbnail"))
				doc.thumbnail = d.getString("thumbnail");
			return doc;
		} catch (Exception e) {
			return null;
		}
	}

	protected ObjectItem _parseObjectItem(JSONObject jsonObject, String key) {
		try {
			try {
				String o = jsonObject.getString(key);
				String[] v = o.split(":");
				if (v.length == 2) {
					ObjectItem item = new ObjectItem();
					item.object = v[0];
					item.rowid = v[1];
					return item;
				}
				return null;
			} catch (JSONException e1) {
				JSONObject o = jsonObject.getJSONObject(key);
				ObjectItem item = new ObjectItem();
				item.object = o.getString("object");
				try {
					item.rowid = o.getString("row_id");
				} catch (JSONException e2) {
					item.rowid = o.getString("id");
				}
				return item;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>Check if object is empty</p>
	 * @param o Object
	 * @return true is object is null or is empty
	 */
	public static boolean isEmpty(Object o) {
		if (o != null) {
			if (o instanceof String)
				return ((String)o).length() == 0;
			else if (o instanceof ArrayList)
				return ((ArrayList<?>)o).isEmpty();
			else if (o instanceof HashMap)
				return ((HashMap<?, ?>)o).isEmpty();
			else
				return false;
		} else
			return true;
	}
}