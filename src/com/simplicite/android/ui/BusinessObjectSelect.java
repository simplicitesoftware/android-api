/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import java.util.HashMap;

import com.simplicite.android.R;
import com.simplicite.android.core.BusinessObject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class BusinessObjectSelect extends BusinessObjectListCommon {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businessobjectselect);
		
		listLayout = (LinearLayout)findViewById(R.id.selectMainLayout);
		initListButtons();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		buildList(obj.page);
	}
	
	@Override
	protected int initList() throws Exception {
		int ctx = BusinessObject.CONTEXT_REFSELECT;
		
		parent.getMetaData(BusinessObject.CONTEXT_NONE);
		obj.getMetaData(ctx, parent.name);
		
		return ctx;
	}
	
	@Override
	protected void onListClick(HashMap<String, Object> item) {
		Intent result = new Intent();
		result.putExtra("ROWIDFIELD", obj.rowIdField);
		result.putExtra("ITEM", item);
		setResult(RESULT_OK, result);
		finish();
	}
}