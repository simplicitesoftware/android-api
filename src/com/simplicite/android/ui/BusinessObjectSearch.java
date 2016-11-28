/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import java.util.HashMap;

import com.simplicite.android.R;
import com.simplicite.android.core.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusinessObjectSearch extends BusinessObjectCommon {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businessobjectsearch);
		goHomeOnLogo();
				
		((Button)findViewById(R.id.searchButton)).setText(getText("SEARCH", "Search"));

		Button createButton = (Button)findViewById(R.id.createButton);
		createButton.setText(getText("CREATE", "Create"));
		createButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View button) {
				// Start update activity in create mode
				Intent intent = new Intent(BusinessObjectSearch.this, BusinessObjectForm.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				setIntentExtraValues(intent, Field.DEFAULT_ROW_ID);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		if (!isAfterActivityResult()) {
			buildSearchForm();
		} else
			setAfterActivityResult(false);
	}
	
	private void buildSearchForm() {
		final LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
		searchLayout.removeAllViews();
		
		final Button searchButton = (Button)findViewById(R.id.searchButton);
		final Button createButton = (Button)findViewById(R.id.createButton);

		new LoadingAsyncTask(searchLayout, true) {
			@Override
			protected void preTask() {
				searchButton.setEnabled(false);
				createButton.setEnabled(false);
			}
			
			@Override
			protected Object task(Object[] params) throws Exception {
				obj.getMetaData(BusinessObject.CONTEXT_SEARCH);
				obj.getFilters(false, BusinessObject.CONTEXT_SEARCH);
				return obj;
			}
			
			@Override
			protected void postTask(Object result) {
				setTitle();
				setHelp(obj.help);

				searchButton.setEnabled(true);
				createButton.setEnabled(obj.create);

				final HashMap<String, View> inputs = new HashMap<String, View>();
				for (Field field : obj.fieldsByOrder) {
					if (field.isSearchable()) {
						TextView label = new TextView(BusinessObjectSearch.this);
						label.setText(field.label);
						searchLayout.addView(label);
						
						Object filter = obj.filters.get(field.name);
						View input = null;
						// TODO : other field types
						if (field.type == Field.TYPE_ENUM || field.type == Field.TYPE_ENUM_MULTI) {
							input = enumInputAsDropDown(field, (String)filter, true, true);
						} else {
							input = textInput(field, filter instanceof String ? (String)filter : "", false);
						}
						searchLayout.addView(input);
						inputs.put(field.name, input);
					}
				}
				
				((Button)findViewById(R.id.searchButton)).setOnClickListener(new Button.OnClickListener() {
					public void onClick(View button) {
						try {
							obj.filters.clear();
							for (String name : obj.fields.keySet()) {
								String filter = (String)getInputValue(inputs.get(name));
								if (filter != null)
									obj.filters.put(name, filter);
							}
							
							// Start list activity
							Intent intent = new Intent(BusinessObjectSearch.this, BusinessObjectList.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							setExtraValue(intent, "objectName", obj.name);
							setExtraValue(intent, "objectInstanceName", obj.instance);
							startActivity(intent);
						} catch (Exception e) {
							error(e);
						}
					}
				});
			}
		}.execute();
	}
}