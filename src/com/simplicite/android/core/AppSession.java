/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.*;

import android.os.AsyncTask;

/**
 * <p>Application user session data</p>
 */
public class AppSession extends Common {
	private static final long serialVersionUID = 1L;
	
	/**
	 * </p>Constructor for external usage outside of generic web UI</p>
	 * @param login User login
	 * @param password user password
	 * @param baseURL Base URL of current application generic web services gateway (e.g. http://myserver:8080/myapp<u>ws</u>)
	 */
	public AppSession(String login, String password, String baseURL) {
		super(login, password, baseURL, null);
		_serviceURI = "jsonappservice.jsp";
		clear();
	}

	/**
	 * <p>Clears all session data</p>
	 */
	public void clear() {
		name = null;
		version = null;
		platformVersion = null;
		encoding = null;

		sysinfo = null;

		session = null;
		grant = null;
		menu = null;
		texts = null;
		sysparams = null;
		news = null;
		
		clearBusinessObjects();
		clearExternalObjects();
		clearBusinessProcesses();
	}
	
	/**
	 * <p>Clears business objects</p>
	 */
	public void clearBusinessObjects() {
		if (businessObjects == null)
			businessObjects = new HashMap<String, BusinessObject>();
		else
			businessObjects.clear();
	}
	
	/**
	 * <p>Clears external objects</p>
	 */
	public void clearExternalObjects() {
		if (externalObjects == null)
			externalObjects = new HashMap<String, ExternalObject>();
		else
			externalObjects.clear();
	}
	
	/**
	 * <p>Clears business processes</p>
	 */
	public void clearBusinessProcesses() {
		if (businessProcesses == null)
			businessProcesses = new HashMap<String, BusinessProcess>();
		else
			businessProcesses.clear();
	}
	
	/**
	 * <p>Application name</p>
	 */
	public String name;
	
	/**
	 * <p>Application version</p>
	 */
	public String version;
	
	/**
	 * <p>Platform version</p>
	 */
	public String platformVersion;

	/**
	 * <p>Encoding</p>
	 */
	public String encoding;

	/**
	 * <p>System info</p>
	 */
	public SysInfo sysinfo;
	
	/**
	 * <p>Session ID</p>
	 */
	public String session;

	/**
	 * <p>User data</p>
	 */
	public Grant grant;
	
	/**
	 * <p>Main menu</p>
	 */
	public MainMenu menu;

	/**
	 * <p>System parameters</p>
	 */
	public HashMap<String, String> sysparams;

	/**
	 * <p>Texts</p>
	 */
	public HashMap<String, String> texts;

	/**
	 * <p>List of values</p>
	 */
	public HashMap<String, HashMap<String, String>> listOfValues;

	/**
	 * <p>News</p>
	 */
	public ArrayList<News> news;

	protected Object _parse(String json) throws AppException {
		try {
			JSONObject msg = (JSONObject)super._parse(json);
			String type = msg.getString("type");
			if (type.equals("info")) {
				JSONObject i = msg.getJSONObject("response");
				// ZZZ Upward compatibility
				name = i.has("name") ? i.getString("name") : "";
				version = i.getString("version");
				platformVersion = i.getString("platformversion");
				encoding = i.getString("encoding");
	
				return name;
			} else if (type.equals("sysinfo")) {
				JSONObject si = msg.getJSONObject("response");
				
				sysinfo = new SysInfo();
				
				sysinfo.cacheobject = si.getInt("cacheobject");
				sysinfo.cacheobjectmax = si.getInt("cacheobjectmax");
				
				// ZZZ Upward compatibility
				sysinfo.cachegrant = si.has("cachegrant") ? si.getInt("cachegrant") : 0;
				sysinfo.cachegrantmax = si.has("cachegrantmax") ? si.getInt("cachegrantmax") : 0;
				
				// ZZZ Upward compatibility
				sysinfo.heapsize = si.has("heapsize") ? si.getLong("heapsize") : 0;
				sysinfo.heapfreesize = si.has("heapfreesize") ? si.getLong("heapfreesize") : 0;
				sysinfo.heapmaxsize = si.has("heapmaxsize") ? si.getLong("heapmaxsize") : 0;
				
				// ZZZ Upward compatibility
				sysinfo.dirdbdoc = si.has("dirdbdoc") ? si.getLong("dirdbdoc") : 0;
				sysinfo.dircache = si.has("dircache") ? si.getLong("dircache") : 0;
				sysinfo.dirrecyclebin = si.has("dirrecyclebin") ? si.getLong("dirrecyclebin") : 0;
				sysinfo.diskfree = si.has("diskfree") ? si.getLong("diskfree") : 0;
	
				return sysinfo;
			} else if (type.equals("session")) {
				JSONObject s = msg.getJSONObject("response");
				session = s.getString("id");
				return session;
			} else if (type.equals("grant")) {
				JSONObject g = msg.getJSONObject("response");
	
				session = g.getString("sessionid");
				
				grant = new Grant();
				grant.userid    = g.getInt("userid");
				grant.login     = g.getString("login");
				grant.lang      = g.getString("lang");
				grant.firstname = g.getString("firstname");
				grant.lastname  = g.getString("lastname");
				grant.email     = g.getString("email");
				// ZZZ Upward compatibility
				grant.picture   = g.has("picture") ? _parseDocument(g, "picture") : null;
				
				grant.responsibilities.clear();
				JSONArray rs = g.getJSONArray("responsibilities");
				for (int i = 0; i < rs.length(); i++) {
					grant.responsibilities.add(rs.getString(i));
				}
				
				return grant;
			} else if (type.equals("menu")) {
				JSONArray ds = msg.getJSONArray("response");
	
				menu = new MainMenu();
				for (int i = 0; i < ds.length(); i++) {
					JSONObject d = ds.getJSONObject(i);
	
					MenuDomain domain = new MenuDomain();
					domain.name = d.getString("name");
					domain.label = d.getString("label");
					
					JSONArray its = d.getJSONArray("items");
					for (int j = 0; j < its.length(); j++) {
						JSONObject it = its.getJSONObject(j);
	
						MenuEntry entry = new MenuEntry();
						entry.type = it.getString("type");
						entry.name = it.getString("name");
						entry.label = it.getString("label");
						
						domain.entries.add(entry);
					}
	
					menu.domains.add(domain);
				}
				
				return menu;
			} else if (type.equals("sysparams")) {
				JSONArray sps = msg.getJSONArray("response");
	
				sysparams = new HashMap<String, String>();
				for (int i = 0; i < sps.length(); i++) {
					JSONObject sp = sps.getJSONObject(i);
					sysparams.put(sp.getString("name"), sp.getString("value"));
				}
	
				return sysparams;
			} else if (type.equals("sysparam")) {
				if (sysparams == null) sysparams = new HashMap<String, String>();
	
				JSONObject sp = msg.getJSONObject("response");
				String val = sp.getString("value");
				sysparams.put(sp.getString("name"), val);
	
				return val;
			} else if (type.equals("sysparamdb")) {
				// TODO : to be completed...
				return null;
			} else if (type.equals("listofvalue")) {
				// TODO : to be completed...
				return null;
			} else if (type.equals("texts")) {
				JSONArray ts = msg.getJSONArray("response");
	
				texts = new HashMap<String, String>();
				for (int i = 0; i < ts.length(); i++) {
					JSONObject t = ts.getJSONObject(i);
					texts.put(t.getString("code"), t.getString("value"));
				}
	
				return texts;
			} else if (type.equals("text")) {
				if (texts == null) texts = new HashMap<String, String>();
				
				JSONObject t = msg.getJSONObject("response");
				String val = t.getString("value");
				texts.put(t.getString("code"), val);
	
				return val;
			} else if (type.equals("news")) {
				JSONArray ns = msg.getJSONArray("response");
	
				news = new ArrayList<News>();
				for (int i = 0; ns != null && i < ns.length(); i++) {
					JSONObject n = ns.getJSONObject(i);
					News nw = new News();
					//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//nw.date = null;
					//try { nw.date = df.parse(n.getString("date")); } catch (ParseException e) {}
					nw.date = n.getString("date");
					//nw.expdate = null;
					//try { nw.expdate = df.parse(n.getString("expdate")); } catch (ParseException e) {}
					nw.expdate = n.getString("expdate");
					nw.title = n.getString("title");
					nw.image = _parseDocument(n, "image");
					nw.content = n.getString("content");
					news.add(nw);
				}
	
				return news;
			} else if (type.equals("extobject")) {
				JSONObject e = msg.getJSONObject("response");
				
				ExternalObject extobj = new ExternalObject();
				extobj.id = e.getString("id");
				extobj.name = e.getString("name");
				extobj.label = e.getString("label");
				extobj.help = e.getString("help");
				extobj.icon = e.getString("icon");
				extobj.url = e.getString("url");
				extobj.mime = e.getString("mime");
				
				return extobj;
			} else
				throw new AppException("Unhandled " + type + " response type");
		} catch (JSONException e) {
			throw new AppException("Parsing exception: " + e.getMessage());
		}
	}

	private class InfoAsyncTask extends AsyncTask<Void, Void, String> {
		public Exception exception;
		@Override
		protected String doInBackground(Void... params) {
			try {
				debug("Get info");
				return (String)_parse(_get("?data=info"));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Gets application info</p>
	 */
	public String getInfo() throws Exception {
		if (name == null) {
			InfoAsyncTask t = new InfoAsyncTask();
			if (t.execute().get(getTimeout(), TimeUnit.SECONDS) == null)
				throw t.exception;
		}
		return name;
	}

	private class SysInfoAsyncTask extends AsyncTask<Void, Void, SysInfo> {
		public Exception exception;
		@Override
		protected SysInfo doInBackground(Void... params) {
			try {
				debug("Get system info");
				return (SysInfo)_parse(_get("?data=sysinfo"));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Gets system info</p>
	 */
	public SysInfo getSysInfo() throws Exception {
		SysInfoAsyncTask t = new SysInfoAsyncTask();
		if (t.execute().get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return sysinfo;
	}

	private class SessionAsyncTask extends AsyncTask<Void, Void, String> {
		public Exception exception;
		@Override
		protected String doInBackground(Void... params) {
			try {
				debug("Get session");
				return (String)_parse(_get("?data=session"));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Gets user session ID</p>
	 */
	public String getSession() throws Exception {
		if (session == null) {
			SessionAsyncTask t = new SessionAsyncTask();
			if (t.execute().get(getTimeout(), TimeUnit.SECONDS) == null)
				throw t.exception;
		}
		return session;
	}

	private class GrantAsyncTask extends AsyncTask<String, Void, Grant> {
		public Exception exception;
		@Override
		protected Grant doInBackground(String... params) {
			try {
				debug("Get grant");
				return (Grant)_parse(_get("?data=grant" + (params != null && params.length>0 ? params[0] : "")));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}
	
	/**
	 * <p>Gets user grant data</p>
	 */
	public Grant getGrant(boolean inlinePicture) throws Exception {
		if (grant == null) {
			GrantAsyncTask t = new GrantAsyncTask();
			if (t.execute(inlinePicture ? "&inline_picture=true" : "").get(getTimeout(), TimeUnit.SECONDS) == null)
				throw t.exception;
		}
		return grant;
	}	

	private class SysParamAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {
		public Exception exception;
		@SuppressWarnings("unchecked")
		@Override
		protected HashMap<String, String> doInBackground(Void... params) {
			try {
				debug("Get system parameters");
				return (HashMap<String, String>)_parse(_get("?data=sysparams"));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}
	
	/**
	 * <p>Gets system parameters</p>
	 */
	public HashMap<String, String> getSysParams() throws Exception {
		if (sysparams == null) {
			SysParamAsyncTask t = new SysParamAsyncTask();
			if (t.execute().get(getTimeout(), TimeUnit.SECONDS) == null)
				throw t.exception;
		}
		return sysparams;
	}
	
	private class TextAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {
		public Exception exception;
		@SuppressWarnings("unchecked")
		@Override
		protected HashMap<String, String> doInBackground(Void... params) {
			try {
				debug("Get texts");
				return (HashMap<String, String>)_parse(_get("?data=texts"));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}
	
	/**
	 * <p>Gets translated texts in user's language</p>
	 */
	public HashMap<String, String> getTexts() throws Exception {
		if (texts == null) {
			TextAsyncTask t = new TextAsyncTask();
			if (t.execute().get(getTimeout(), TimeUnit.SECONDS) == null)
				throw t.exception;
		}
		return texts;
	}
	
	/**
	 * <p>Get text for specified code (must be called after getTexts)</p>
	 * @param code Text code
	 * @param def Default value
	 */
	public String getText(String code, String def) {
		String val = texts != null ? texts.get(code) : null;
		return val == null ? def : val;
	}

	private class MenuAsyncTask extends AsyncTask<Void, Void, MainMenu> {
		public Exception exception;
		@Override
		protected MainMenu doInBackground(Void... params) {
			try {
				debug("Get menu");
				return (MainMenu)_parse(_get("?data=menu"));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}
	
	/**
	 * <p>Gets user menu</p>
	 */
	public MainMenu getMenu() throws Exception {
		if (menu == null) {
			MenuAsyncTask t = new MenuAsyncTask();
			if (t.execute().get(getTimeout(), TimeUnit.SECONDS) == null)
				throw t.exception;
		}
		return menu;
	}
		
	private class NewsAsyncTask extends AsyncTask<String, Void, ArrayList<News>> {
		public Exception exception;
		@SuppressWarnings("unchecked")
		@Override
		protected ArrayList<News> doInBackground(String... params) {
			try {
				debug("Get news");
				return (ArrayList<News>)_parse(_get("?data=news" + (params != null && params.length>0 ? params[0] : "")));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}
	
	/**
	 * <p>Gets news</p>
	 * @param inlineImages Inline images ?
	 * @param refresh Force refresh ?
	 */
	public ArrayList<News> getNews(boolean inlineImages, boolean refresh) throws Exception {
		if (news == null || refresh) {
			NewsAsyncTask t = new NewsAsyncTask();
			if (t.execute(inlineImages ? "&inline_images=true" : "").get(getTimeout(), TimeUnit.SECONDS) == null)
				throw t.exception;
		}
		return news;
	}
	
	private HashMap<String, BusinessObject> businessObjects;
	
	/**
	 * <p>Gets a business object instance for current user session</p>
	 * @param name Business object logical name
	 */
	public BusinessObject getBusinessObject(String name)  throws Exception {
		return getBusinessObject(name, null);
	}
	
	/**
	 * <p>Gets a business object instance for current user session</p>
	 * @param name Business object logical name
	 * @param instance Business object logical instance name
	 */
	public BusinessObject getBusinessObject(String name, String instance)  throws Exception {
		BusinessObject obj = businessObjects.get(name + ":" + instance);
		if (obj == null) {
			obj = new BusinessObject(name, instance, this);
			obj.setDebug(isDebug());
			obj.setTimeout(getTimeout());
			obj.getMetaData();
			businessObjects.put(name + ":" + instance, obj);
		}
		return obj;
	}

	private HashMap<String, ExternalObject> externalObjects;
	
	private class ExtObjectAsyncTask extends AsyncTask<String, Void, ExternalObject> {
		public Exception exception;
		@Override
		protected ExternalObject doInBackground(String... params) {
			try {
				debug("Get external object");
				return (ExternalObject)_parse(_get("?data=extobject" + (params != null && params.length>0 ? params[0] : "")));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}
	
	/**
	 * <p>Gets an external object for current user session</p>
	 * @param name External object logical name
	 */
	public ExternalObject getExternalObject(String name)  throws Exception {
		ExternalObject extobj = externalObjects.get(name);
		if (extobj == null) {
			ExtObjectAsyncTask t = new ExtObjectAsyncTask();
			if ((extobj = t.execute("&name=" + name).get(getTimeout(), TimeUnit.SECONDS)) == null)
				throw t.exception;
		}
		return extobj;
	}

	private HashMap<String, BusinessProcess> businessProcesses;
	
	/**
	 * <p>Gets a business process for current user session</p>
	 * @param name Business process logical name
	 */
	public BusinessProcess getBusinessProcess(String name)  throws Exception {
		BusinessProcess pcs = businessProcesses.get(name);
		if (pcs == null) {
			pcs = new BusinessProcess(name, this);
			pcs.setDebug(isDebug());
			pcs.setTimeout(getTimeout());
			businessProcesses.put(name, pcs);
		}
		return pcs;
	}
	
	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Session:" + session + "\n");
		s.append(grant.toString());
		s.append("System params:\n");
		for (String key : sysparams.keySet()) {
			s.append("\t" + key + " = [" + sysparams.get(key) + "]\n");
		}
		s.append("Texts:\n");
		for (String key : texts.keySet()) {
			s.append("\t" + key + " = [" + texts.get(key) + "]\n");
		}
		return s.toString();
	}
}