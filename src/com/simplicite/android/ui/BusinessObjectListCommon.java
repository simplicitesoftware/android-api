/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.simplicite.android.R;
import com.simplicite.android.core.Field;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public abstract class BusinessObjectListCommon extends BusinessObjectCommon {
	protected void initListButtons() {
		((Button)findViewById(R.id.firstButton)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View button) { buildList(0); }
		});
		((Button)findViewById(R.id.prevButton)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View button) { buildList(Math.max(obj.page - 1, 0)); }
		});
		((Button)findViewById(R.id.nextButton)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View button) { buildList(Math.min(obj.page + 1, obj.maxPage)); }
		});
		((Button)findViewById(R.id.lastButton)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View button) { buildList(obj.maxPage); }
		});
	}
	
	protected void disableListButtons() {
		((TextView)findViewById(R.id.pageTextView)).setText("");
		findViewById(R.id.firstButton).setEnabled(false);
		findViewById(R.id.prevButton).setEnabled(false);
		findViewById(R.id.nextButton).setEnabled(false);
		findViewById(R.id.lastButton).setEnabled(false);
	}
	
	protected void enableListButtons() {
		((TextView)findViewById(R.id.pageTextView)).setText((obj.page + 1) + " / " + (obj.maxPage + 1));
		findViewById(R.id.firstButton).setEnabled(obj.page > 0);
		findViewById(R.id.prevButton).setEnabled(obj.page > 0);
		findViewById(R.id.nextButton).setEnabled(obj.page < obj.maxPage);
		findViewById(R.id.lastButton).setEnabled(obj.page < obj.maxPage);
	}

	protected LinearLayout listLayout = null;
	
	private ListView listListView = null;
	
	protected void buildList(final int page) {
		obj.clearList();

		if (listListView != null)
			listLayout.removeView(listListView);
		
		new LoadingAsyncTask(listLayout, true) {
			@Override
			protected void preTask() {
				disableListButtons();
			}
			
			@Override
			protected Object task(Object[] params) throws Exception {
				obj.search(page, initList(), false, false);
				return obj;
			}
			
			@Override
			protected void postTask(Object result) {
				setTitle();
				setHelp(obj.help);

				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
				for (HashMap<String, Object> item : obj.list) {
					HashMap<String, String> i = new HashMap<String, String>();
					StringBuilder l = new StringBuilder();
					for (Field field : obj.fieldsByOrder) {
						if (field.isListVisible() && field.key)
							l.append((l.length() == 0 ? "" : " / ") + item.get(field.name));
					}
					i.put("key", l.toString());
					list.add(i);
				}
				
				listListView = new ListView(BusinessObjectListCommon.this);
				listLayout.addView(listListView);
				
				SimpleAdapter adapter = new SimpleAdapter(BusinessObjectListCommon.this, list, R.layout.businessobjectlistrow, new String[] { "key" }, new int[] { R.id.listRowText });
				listListView.setAdapter(adapter);

				listListView.setOnItemClickListener(new ListView.OnItemClickListener() {
			        @Override
			        public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
			        	onListClick(obj.list.get(i));
			        }
			    });

				enableListButtons();
			}
		}.execute();
	}
	
	protected abstract int initList() throws Exception;
	
	protected abstract void onListClick(HashMap<String, Object> item);
}