/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import java.util.HashMap;

import com.simplicite.android.R;
import com.simplicite.android.core.BusinessObject;
import com.simplicite.android.core.Field;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BusinessObjectList extends BusinessObjectListCommon {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businessobjectlist);
		goHomeOnLogo();
				
		listLayout = (LinearLayout)findViewById(R.id.listMainLayout);
		initListButtons();
	}

	@Override
	protected void initListButtons() {
		super.initListButtons();
		
		Button createButton = (Button)findViewById(R.id.createButton);
		//createButton.setText(getText("CREATE", "Create"));
		createButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View button) {
				// Start update activity in create mode
				Intent intent = new Intent(BusinessObjectList.this, BusinessObjectForm.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				setIntentExtraValues(intent, Field.DEFAULT_ROW_ID);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void disableListButtons() {
		super.disableListButtons();
		findViewById(R.id.createButton).setEnabled(false);
	}
	
	@Override
	protected void enableListButtons() {
		super.enableListButtons();
		findViewById(R.id.createButton).setEnabled(obj.create);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if (!isAfterActivityResult()) {
			buildList(obj.page);
		} else
			setAfterActivityResult(false);
	}

	protected int initList() throws Exception {
		boolean panel = obj.instance.startsWith("panel_") && parent != null;

		int ctx = panel ? BusinessObject.CONTEXT_PANELLIST : BusinessObject.CONTEXT_LIST;
		if (panel) {
			parent.getMetaData(BusinessObject.CONTEXT_NONE);

			String param = parent == null ? null : parent.name + ":" + parent.instance;
			obj.getMetaData(ctx, param);

			obj.clearFilters();
			obj.filters.put(obj.parentRefField, obj.parentRowId);
		} else
			obj.getMetaData(ctx);
		
		return ctx;
	}
	
	protected void onListClick(HashMap<String, Object> item) {
		// Start update activity
		Intent intent = new Intent(BusinessObjectList.this, BusinessObjectForm.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		setIntentExtraValues(intent, (String)item.get(obj.rowIdField));
		startActivity(intent);
	}
}