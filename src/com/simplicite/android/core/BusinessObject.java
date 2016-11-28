/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import android.os.AsyncTask;

/**
 * <p>Business object</p>
 */
public class BusinessObject extends Common {
	private static final long serialVersionUID = 1L;

	/*
	 * Contexts (to be documented)
	 */
	public static final int  CONTEXT_NONE            = 0;
	public static final int  CONTEXT_SEARCH          = 1;
	public static final int  CONTEXT_LIST            = 2;
	public static final int  CONTEXT_CREATE          = 3;
	public static final int  CONTEXT_COPY            = 4;
	public static final int  CONTEXT_UPDATE          = 5;
	public static final int  CONTEXT_DELETE          = 6;
	public static final int  CONTEXT_GRAPH           = 7;
	public static final int  CONTEXT_CROSSTAB        = 8;
	public static final int  CONTEXT_PRINTTMPL       = 9;
	public static final int  CONTEXT_UPDATEALL       = 10;
	public static final int  CONTEXT_REFSELECT       = 11;
	public static final int  CONTEXT_DATAMAPSELECT   = 12;
    public static final int  CONTEXT_PREVALIDATE     = 13;
    public static final int  CONTEXT_POSTVALIDATE    = 14;
    public static final int  CONTEXT_STATETRANSITION = 15;
    public static final int  CONTEXT_EXPORT          = 16;
    public static final int  CONTEXT_IMPORT          = 17;
    public static final int  CONTEXT_ASSOCIATE       = 18;
    public static final int  CONTEXT_PANELLIST       = 19;
	
	/**
	 * </p>Constructor</p>
	 * @param name Object logical name
	 * @param instance Object logical instance name
	 * @param app Application session
	 */
	public BusinessObject(String name, String instance, AppSession app) {
		this(name, instance, app._login, app._password, app._baseURL, app._client);
	}

	private BusinessObject(String name, String instance, String login, String password, String baseURL, DefaultHttpClient client) {
		super(login, password, baseURL, client);
		if (instance == null || instance.length() == 0) instance = "android_" + name;
		_serviceURI = "jsonservice.jsp?object=" + URLEncoder.encode(name) + "&inst=" + URLEncoder.encode(instance);
		this.name = name;
		this.instance = instance;
		clear();
	}

	/**
	 * <p>Clears all meta data and current data</p>
	 */
	public void clear() {
		id = null;
		label = null;
		help = null;

		readonly = false;
		create = false;
		update = false;
		del = false;
		copy = false;
		export = false;
		print = false;
		updateAll = false;

		template = "";
		areas = new ArrayList<FieldArea>();
		
		rowIdField = Field.DEFAULT_ROW_ID_FIELD;
		fields = new HashMap<String, Field>();
		
		actions = new ArrayList<Action>();

		links = new ArrayList<Link>();

		crosstabs = new ArrayList<Crosstab>();

		clearItem();
		clearFilters();
		clearList();
	}
	
	/**
	 * <p>Clears list</p>
	 */
	public void clearList() {
		count = 0;
		page = 0;
		maxPage = 0;
		if (list == null)
			list = new ArrayList<HashMap<String, Object>>();
		else
			list.clear();
	}
	
	/**
	 * <p>Clears list</p>
	 */
	public void clearItem() {
		if (item == null)
			item = new HashMap<String, Object>();
		else
			item.clear();
	}
	
	/**
	 * <p>Clears search filters</p>
	 */
	public void clearFilters() {
		if (filters == null)
			filters = new HashMap<String, Object>();
		else
			filters.clear();
	}
	
	/**
	 * <p>Logical business object name</p>
	 */
	public String name;
	/**
	 * <p>Logical business object instance name</p>
	 */
	public String instance;
	
	/**
	 * <p>Unique business object ID</p>
	 */
	public String id;
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
	 * <p>Is object read only ?</p>
	 */
	public boolean readonly;
	/**
	 * <p>Can create ?</p>
	 */
	public boolean create;
	/**
	 * <p>Can update ?</p>
	 */
	public boolean update;
	/**
	 * <p>Can delete ?</p>
	 */
	public boolean del;
	/**
	 * <p>Can copy ?</p>
	 */
	public boolean copy;
	/**
	 * <p>Can export ?</p>
	 */
	public boolean export;
	/**
	 * <p>Can publish ?</p>
	 */
	public boolean print;
	/**
	 * <p>Can bulk update ?</p>
	 */
	public boolean updateAll;
	
	/**
	 * <p>Form template</p>
	 */
	public String template;
	/**
	 * <p>Form areas</p>
	 */
	public ArrayList<FieldArea> areas;
	
	/**
	 * <p>Name of the object row ID field (defaults to &quot;row_id&quot;)</p>
	 */
	public String rowIdField;
	/**
	 * <p>Object fields map</p>
	 */
	public HashMap<String, Field> fields;
	/**
	 * <p>Object fields ordered array</p>
	 */
	public ArrayList<Field> fieldsByOrder;

	/**
	 * <p>Actions</p>
	 */
	public ArrayList<Action> actions;

	/**
	 * <p>Links</p>
	 */
	public ArrayList<Link> links;

	/**
	 * <p>Cross tables</p>
	 */
	public ArrayList<Crosstab> crosstabs;

	/**
	 * <p>Current item</p>
	 */
	public HashMap<String, Object> item;
	
	/**
	 * <p>Current filters</p>
	 */
	public HashMap<String, Object> filters;
	
	/**
	 * <p>Clear parent object data</p>
	 */
	public void clearParent() {
		parentName = null;
		parentInstance = null;
		parentRefField = null;
		parentRowId = null;
	}
	/**
	 * <p>Parent object logical name</p>
	 */
	public String parentName;
	/**
	 * <p>Parent object logical instance name</p>
	 */
	public String parentInstance;
	/**
	 * <p>Reference field name to parent object</p>
	 */
	public String parentRefField;
	/**
	 * <p>Parent row ID</p>
	 */
	public String parentRowId;
	
	/**
	 * <p>Current list count</p>
	 */
	public int count;
	/**
	 * <p>Current list page (if current list is resulting from a paginated search)</p>
	 */
	public int page;
	/**
	 * <p>Current list max page (if current list is resulting from a paginated search)</p>
	 */
	public int maxPage;
	/**
	 * <p>Current list (limited to current page items in case of a paginated search)</p>
	 */
	public ArrayList<HashMap<String, Object>> list;

	private void _parseItem(HashMap<String, Object> item, JSONObject obj) throws JSONException {
		item.clear();
		for (String name : fields.keySet()) {
			Field field = fields.get(name);
			Object val = null;
			if (obj != null) {
				if (field.type == Field.TYPE_DOC || field.type == Field.TYPE_IMAGE) {
					Document doc = _parseDocument(obj, field.name);
					val = doc == null ? obj.get(field.name) : doc;
				} else if (field.type == Field.TYPE_OBJECT) {
					val = _parseObjectItem(obj, field.name);
				} else
					val = obj.get(field.name);
			}
			item.put(field.name, val);
		}
	}
	
	private void _parseFilters(HashMap<String, Object> filters, JSONObject obj) throws JSONException {
		filters.clear();
		for (String name : fields.keySet()) {
			Field field = fields.get(name);
			Object val = null;
			// TODO : handle min/max for date/datetime filters
			if (obj != null)
				val = obj.get(field.name);
			filters.put(field.name, val);
		}
	}
	
	protected String _parse(String json) throws AppException {
		try {
			JSONObject msg = (JSONObject)super._parse(json);
			String type = msg.getString("type");
			if (type.equals("objectMetaData")) {
				JSONObject md = msg.getJSONObject("response");
	
				id = md.getString("id");
				label = md.getString("label");
				// ZZZ Upward compatibility
				rowIdField = md.has("rowidfield") ? md.getString("rowidfield") : md.getString("rowIdField");
				help = md.getString("help");
				// ZZZ Upward compatibility
				icon = md.has("icon") ? md.getString("icon") : "";

				create = md.getBoolean("create");
				update = md.getBoolean("update");
				del = md.getBoolean("del");
				copy = md.getBoolean("copy");
				export = md.getBoolean("copy");
				print = md.getBoolean("copy");
				updateAll = md.getBoolean("updateAll");

				template = md.getString("template");
				
				if (md.has("areas")) {
					JSONArray as = md.getJSONArray("areas");
					areas.clear();
					for (int i = 0; i < as.length(); i++) {
						JSONObject a = as.getJSONObject(i);
						FieldArea area = new FieldArea();
						area.area = a.getInt("area");
						area.label = a.getString("label");
						areas.add(area);
					}
				}
				
				JSONArray fs = md.getJSONArray("fields");
				fields.clear();
				for (int i = 0; i < fs.length(); i++) {
					JSONObject f = fs.getJSONObject(i);
					
					Field field = new Field();
					// ZZZ Upward compatibility
					field.id = f.has("id") ? f.getString("id") : Field.DEFAULT_ROW_ID;
					String name = f.getString("name");
					field.name = name;
					// ZZZ Upward compatibility
					field.order = f.has("order") ? f.getInt("order") : i;
					field.label = f.getString("label");
					field.help = f.getString("help");
					field.refId = f.getBoolean("refId");
					field.ref = f.getBoolean("ref");
					field.refObject = f.has("refObject") ? f.getString("refObject") : null;
					field.refField = f.has("refField") ? f.getString("refField") : null;
					field.key = f.getBoolean("key");
					field.required = f.getBoolean("required");
					field.updatable = f.getBoolean("updatable");
					field.visible = f.getInt("visible");
					field.searchable = f.getInt("searchable");
					// ZZZ Upward compatibility
					field.searchOrder = f.has("searchOrder") ? f.getInt("searchable") : 0;
					field.rendering = f.getString("rendering");
					field.extended = f.getBoolean("extended");
					field.type = f.getInt("type");
					field.length = f.getInt("length");
					field.precision = f.getInt("precision");
					field.area = f.getInt("area");
					field.defaultValue = f.getString("defaultValue");
	
					if (f.has("listOfValues")) {
						JSONArray lovs = f.getJSONArray("listOfValues");
						field.listOfValues = new ListOfValues(f.getString("listOfValuesName"));
						for (int j = 0; lovs != null && j < lovs.length(); j++) {
							JSONObject lov = lovs.getJSONObject(j);
							ListOfValues.Item item = field.listOfValues.new Item();
							item.code = lov.getString("code");
							item.value = lov.getString("value");
							// ZZZ Upward compatibility
							item.enabled = lov.has("enabled") ? lov.getBoolean("enabled") : true;
							field.listOfValues.items.add(item);
						}
					}
					
					fields.put(name, field);
				}
	
				if (md.has("actions")) {
					JSONArray acs = (JSONArray)md.get("actions");
					actions.clear();
					for (int i = 0; i < acs.length(); i++) {
						JSONObject ac = (JSONObject)acs.get(i);
						Action action = new Action();
						action.name = ac.getString("name");
						action.label = ac.getString("label");
						action.confirm = ac.getBoolean("confirm");
						action.listVisible = ac.getBoolean("listVisible");
						action.listItemVisible = ac.getBoolean("listItemVisible");
						action.formVisible = ac.getBoolean("formVisible");
						actions.add(action);
					}
				}
				
				if (md.has("links")) {
					JSONArray as = md.getJSONArray("links");
					links.clear();
					for (int i = 0; i < as.length(); i++) {
						JSONObject a = as.getJSONObject(i);
						Link link = new Link();
						link.field = a.getString("field");
						link.object = a.getString("object");
						link.order = a.getInt("order");
						link.label = a.getString("label");
						link.visible = a.getBoolean("visible");
						link.minOccurs = a.getInt("minOccurs");
						link.maxOccurs = a.getInt("maxOccurs");
						link.cascadeCopy = a.getBoolean("cascadeCopy");
						link.associate = a.getBoolean("associate");
						links.add(link);
					}
				}
				
				if (md.has("crosstabs")) {
					JSONArray cts = (JSONArray)md.get("crosstabs");
					crosstabs.clear();
					for (int i = 0; i < cts.length(); i++) {
						JSONObject ct = cts.getJSONObject(i);
						Crosstab crosstab = new Crosstab();
						crosstab.name = ct.getString("name");
						crosstab.label = ct.getString("label");
						// TODO : to be completed...
						crosstabs.add(crosstab);
					}
				}
				
				fieldsByOrder = new ArrayList<Field>();
				fieldsByOrder.addAll(fields.values());
				Collections.sort(fieldsByOrder);
	
				return null;
			} else if (type.equals("filters")) {
				JSONObject fs = msg.getJSONObject("response");
	
				_parseFilters(filters, fs);
	
				return null;
			} else if (type.equals("search")) {
				JSONObject sr = (JSONObject)msg.get("response");
	
				count = !sr.has("count") ? 0 : sr.getInt("count");
				page = !sr.has("page") ? 0 : sr.getInt("page");
				maxPage = !sr.has("maxpage") ? 0 : sr.getInt("maxpage");
				JSONArray objs = sr.getJSONArray("list");
				list.clear();
				for (int i = 0; i < objs.length(); i++) {
					HashMap<String, Object> it = new HashMap<String, Object>();
					_parseItem(it, objs.getJSONObject(i));
					list.add(it);
				}
	
				return null;
			} else if (type.equals("get") || type.equals("populate") || type.equals("create") || type.equals("update")) {
				JSONObject i = msg.getJSONObject("response");
	
				_parseItem(item, i);
	
				return null;
			} else if (type.equals("delete")) {
				_parseItem(item, null);
	
				return null;
			} else if (type.equals("action")) {
				JSONObject ar = msg.getJSONObject("response");
				return ar.getString("result");
			} else
				throw new JSONException("Unhandled " + type + " response type");
		} catch (JSONException e) {
			throw new AppException("Parsing exception: " + e.getMessage());
		}
	}
	
	private class MetaDataAsyncTask extends AsyncTask<String, Void, Integer> {
		public Exception exception;
		@Override
		protected Integer doInBackground(String... params) {
			try {
				debug("Get meta data for object " + name + "/" + instance);
				_parse(_get(params[0]));
				return Integer.valueOf(id);
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Retreives meta data for specified parametrized context</p>
	 * @param context Usage context (one of CONTEXT_* constant)
	 * @param contextParam contextParam Usage context parameter (only useful for CONTEXT_GRAPH, CONTEXT_CROSSTAB and CONTEXT_PRINTTMPL usage contexts for indicating the corresponding item)
	 */
	public void getMetaData(int context, String contextParam) throws Exception {
		MetaDataAsyncTask t = new MetaDataAsyncTask();
		if (t.execute("&action=metadata&context=" + context + (contextParam != null && contextParam.length() != 0 ? "&contextparam=" + URLEncoder.encode(contextParam) : "")).get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
	}	
	
	/**
	 * <p>Retreives meta data for specified non paramtrized context</p>
	 * @param context Usage context (one of CONTEXT_* constant)
	 */
	public void getMetaData(int context) throws Exception {
		getMetaData(context, null);
	}
	
	/**
	 * <p>Retreives meta data for current context</p>
	 */
	public void getMetaData() throws Exception {
		getMetaData(CONTEXT_NONE, null);
	}
	
	private class FiltersAsyncTask extends AsyncTask<String, Void, HashMap<String, Object>> {
		public Exception exception;
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			try {
				_parse(_get(params[0]));
				return filters;
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Retreives current filters for specified context</p>
	 * @param context Usage context (one of CONTEXT_* constant, should normally be CONTEXT_SEARCH)
	 * @param reset Reset filters ?
	 */
	public HashMap<String, Object> getFilters(boolean reset, int context) throws Exception {
		FiltersAsyncTask t = new FiltersAsyncTask();
		if (t.execute("&action=filters&reset=" + reset + "&context=" + context).get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return filters;
	}
	
	/**
	 * <p>Searches using current filters (not paginated)</p>
	 * @param context Usage context (one of CONTEXT_* constant, should normally be CONTEXT_LIST)
	 * @param inlineDocs Inline document data for document/image fields
	 * @param inlineThumbs Inline document thumbnails data for image fields
	 */
	public ArrayList<HashMap<String,Object>> search(int context, boolean inlineDocs, boolean inlineThumbs) throws Exception {
		return search(-1, context, inlineDocs, inlineThumbs);
	}
	
	private class SearchAsyncTask extends AsyncTask<String, Void, ArrayList<HashMap<String,Object>>> {
		public Exception exception;
		@Override
		protected ArrayList<HashMap<String,Object>> doInBackground(String... params) {
			try {
				_parse(_post(params[0], filters));
				return list;
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Searches using current filters (paginated)</p>
	 * @param page Page index (must be between 0 and maxpage - 1, -1 means no pagination)
	 * @param context Usage context (one of CONTEXT_* constant, should normally be CONTEXT_LIST)
	 * @param inlineDocs Inline document data for document/image fields
	 * @param inlineThumbs Inline document thumbnails data for image fields
	 */
	public ArrayList<HashMap<String,Object>> search(int page, int context, boolean inlineDocs, boolean inlineThumbs) throws Exception {
		SearchAsyncTask t = new SearchAsyncTask();
		if (t.execute("&action=search" + (page >= 0 ? "&page=" + page : "") + "&context=" + context + "&inline_documents=" + inlineDocs+ "&inline_thumbnails=" + inlineThumbs).get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return list;
	}
	
	private class GetAsyncTask extends AsyncTask<String, Void, HashMap<String, Object>> {
		public Exception exception;
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			try {
				_parse(_get(params[0]));
				return item;
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Get current item using specified row ID</p>
	 * @param rowId Row ID
	 * @param inlineDocs Inline document data for document/image fields
	 * @param inlineThumbs Inline document thumbnails data for image fields
	 */
	public HashMap<String, Object> get(String rowId, boolean inlineDocs, boolean inlineThumbs) throws Exception {
		GetAsyncTask t = new GetAsyncTask();
		if (t.execute("&action=get&" + rowIdField + "=" + rowId + "&inline_documents=" + inlineDocs + "&inline_thumbnails=" + inlineThumbs).get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return item;
	}
	
	/**
	 * <p>Get current item using specified row ID</p>
	 */
	public HashMap<String, Object> get(long rowId, boolean inlineDocs, boolean inlineThumbs) throws Exception {
		return get(Long.valueOf(rowId).toString(), inlineDocs, inlineThumbs);
	}
	
	private class PopulateAsyncTask extends AsyncTask<String, Void, HashMap<String, Object>> {
		public Exception exception;
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			try {
				_parse(_post(params[0], item));
				return item;
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Populate current item using current item data</p>
	 */
	public HashMap<String, Object> populate() throws Exception {
		PopulateAsyncTask t = new PopulateAsyncTask();
		if (t.execute("&action=populate").get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return item;
	}
	
	private class SaveAsyncTask extends AsyncTask<String, Void, HashMap<String, Object>> {
		public Exception exception;
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			try {
				_parse(_post(params[0], item));
				return item;
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Create item using current item data</p>
	 */
	public HashMap<String, Object> create() throws Exception {
		item.put(rowIdField, Field.DEFAULT_ROW_ID);
		SaveAsyncTask t = new SaveAsyncTask();
		if (t.execute("&action=create").get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return item;
	}
	
	/**
	 * <p>Update current item using current item data</p>
	 */
	public HashMap<String, Object> update() throws Exception {
		SaveAsyncTask t = new SaveAsyncTask();
		if (t.execute("&action=update").get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return item;
	}
	
	/**
	 * <p>Delete current item</p>
	 */
	public HashMap<String, Object> del() throws Exception {
		SaveAsyncTask t = new SaveAsyncTask();
		if (t.execute("&action=delete").get(getTimeout(), TimeUnit.SECONDS) == null)
			throw t.exception;
		return item;
	}
		
	private class ActionAsyncTask extends AsyncTask<String, Void, String> {
		public Exception exception;
		@Override
		protected String doInBackground(String... params) {
			try {
				return _parse(_post(params[0], item));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}
	}

	/**
	 * <p>Execute action</p>
	 * @param action Action name
	 */
	public String action(String action) throws Exception {
		ActionAsyncTask t = new ActionAsyncTask();
		String result = null;
		if ((result = t.execute("&action=" + action).get(getTimeout(), TimeUnit.SECONDS)) == null)
			throw t.exception;
		return result;
	}
	
	/**
	 * <p>Is specified field the row ID field ?</p>
	 * @param field Field
	 */
	public boolean isRowIdField(Field field) {
		return !field.ref && field.name.equals(rowIdField);
	}
	
	/**
	 * <p>Is specified field one of the timestamp fields ?</p>
	 * @param field Field
	 */
	public boolean isTimestampField(Field field) {
		return !field.ref && (
				field.name.equals("created_dt")
			||	field.name.equals("created_by")
			||	field.name.equals("updated_dt")
			||	field.name.equals("updated_by")
			);
	}
	
	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Id: " + id + "\n");
		s.append("Name: " + name + "\n");
		s.append("Instance: " + instance + "\n");
		s.append("Label: " + label + "\n");
		s.append("Icon: " + icon + "\n");
		s.append("Fields:\n");
		for (String name : fields.keySet())
			s.append(fields.get(name).toString());
		return s.toString();
	}
}