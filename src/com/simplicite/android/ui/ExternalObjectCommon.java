/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;

import com.simplicite.android.R;
import com.simplicite.android.core.ExternalObject;

public abstract class ExternalObjectCommon extends AppSessionCommon {
	/**
	 * <p>Simplicité(R) external object definition
	 */
	protected ExternalObject extobj = null;

	public final static String EXTRA_EXTERNALOBJECTNAME = "externalObjectName";
	
	public void setTitle() {
		setTitle(extobj.label);
	}

	protected LinearLayout extobjLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.externalobject);
		goHomeOnLogo();

		final String name = getExtraValue(EXTRA_EXTERNALOBJECTNAME);
		if (name == null) {
			error("No external object name");
			return;
		}

		extobjLayout = (LinearLayout)findViewById(R.id.extobjLayout);
		
		new LoadingAsyncTask(extobjLayout, true) {
			@Override
			protected Object task(Object[] params) throws Exception {
				extobj = app.getExternalObject(name);
				return extobj;
			}
			
			@Override
			protected void postTask(Object result) {
				setTitle();
				setHelp(extobj.help);
				
				try {
					display();
				} catch (Exception e) {
					error(e);
				}
			}
		}.execute();
	}
	
	protected abstract void display() throws Exception;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		addHomeMenuItem(menu);
		return super.onCreateOptionsMenu(menu);
	}
}