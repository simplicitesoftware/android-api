/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import com.simplicite.android.R;
import com.simplicite.android.core.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppSessionConnection extends AppSessionCommon {
	private static final String PREFS_NAME = "SimpliciteDemoPrefs";

	private String baseUrl = null;
	private String appName = null;
	private String login = null;
	private String password = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appsessionconnection);
		
		// Restore preferences
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
		baseUrl = prefs.getString("baseUrl", getString(R.string.simplicite_baseUrlDefault));
		setEditTextValue(R.id.baseUrlEditText, baseUrl);
		appName = prefs.getString("appName", getString(R.string.simplicite_appNameDefault));
		setEditTextValue(R.id.appNameEditText, appName);
		login = prefs.getString("login", getString(R.string.simplicite_loginDefault));
		setEditTextValue(R.id.loginEditText, login);
		password = prefs.getString("password", getString(R.string.simplicite_passwordDefault));
		setEditTextValue(R.id.passwordEditText, password);
		
		((Button)findViewById(R.id.connectionButton)).setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View button) {
				baseUrl = getEditTextValue(R.id.baseUrlEditText, true);
				appName = getEditTextValue(R.id.appNameEditText, true);
				login = getEditTextValue(R.id.loginEditText, true);
				password = getEditTextValue(R.id.passwordEditText, true);

				new LoadingAsyncTask(true, true) {
					@Override
					protected Object task(Object[] params) throws Exception {
						String url = baseUrl;
						if (!url.endsWith("/")) url += "/";
						url += appName + "ws";
						
						// Instanciate application session singleton
						app = new AppSession(login, password, url);
						
						// Set debug
						app.setDebug(Boolean.parseBoolean(getString(R.string.simplicite_debug)));
						
						// Set timeout
						int timeout = 30;
						try { timeout = Integer.parseInt(getString(R.string.simplicite_timeout)); } catch (Exception e) {}
						app.setTimeout(timeout);
						
						// Load application session data
						app.getInfo();
						app.getGrant(true);
						app.getSysParams();
						app.getTexts();
						app.getMenu();
						
						return app;
					}
					
					@Override
					protected void postTask(Object result) {
						// Start home activity
						startActivity(new Intent(AppSessionConnection.this, AppSessionHome.class));
						
						// Finish activity
						finish();
					}
				}.execute();
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		findViewById(R.id.passwordEditText).requestFocus();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		// Save preferences
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("baseUrl", baseUrl);
		editor.putString("appName", appName);
		editor.putString("login", login);
		editor.commit();
	}
}