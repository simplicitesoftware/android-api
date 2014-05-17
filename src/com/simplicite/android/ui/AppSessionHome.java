/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import com.simplicite.android.R;
import com.simplicite.android.core.*;
import com.simplicite.android.ui.BusinessObjectCommon;
import com.simplicite.android.ui.ExternalObjectCommon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AppSessionHome extends AppSessionCommon {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appsessionhome);
		setTitle(app.sysparams.get("WINDOW_TITLE"));
		
		((TextView)findViewById(R.id.grantTextView)).setText(app.grant.firstname + " " + app.grant.lastname);
		
		if (app.grant.picture != null && app.grant.picture instanceof Document) {
			ImageView picture = (ImageView)findViewById(R.id.grantImageView);
			Bitmap b = app.grant.picture.getBitmap();
			if (b != null) picture.setImageBitmap(b);

			if (app.grant.hasResponsibility("ADMIN")
				|| app.grant.hasResponsibility("GRANT_ADMIN")
				|| app.grant.hasResponsibility("USER_ADMIN")) {
				picture.setOnClickListener(new ImageView.OnClickListener() {
					@Override
					public void onClick(View image) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
					}
				});
			}
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		if (!isAfterActivityResult()) {
			buildHome();
		} else
			setAfterActivityResult(false);
	}

	private void buildHome() {
		final LinearLayout homeLayout = (LinearLayout)findViewById(R.id.homeLayout);
		homeLayout.removeAllViews();
		
		// (Re)loads news
		new LoadingAsyncTask(homeLayout, false) {
			@Override
			protected Object task(Object[] params) throws Exception {
				app.getNews(true, true);
				
				if (app.grant.hasResponsibility("ADMIN"))
					app.getSysInfo();
				
				return null;
			}
			
			@Override
			protected void postTask(Object result) {
				homeLayout.removeAllViews();

				TextView newsTitle = new TextView(AppSessionHome.this);
				newsTitle.setText(getText("NEWS", "News"));
				newsTitle.setTextAppearance(AppSessionHome.this, R.style.simplicite_subtitle);
				homeLayout.addView(newsTitle);

				if (app.news != null) {
					TableLayout newsTable = new TableLayout(AppSessionHome.this);
					for (News n : app.news) {
						TableRow newsRow = new TableRow(AppSessionHome.this);
						
						if (n.image != null && n.image instanceof Document)
							newsRow.addView(image(n.image, 5, 75));
						
						TextView newsText = new TextView(AppSessionHome.this);
						newsText.setTextAppearance(AppSessionHome.this, R.style.simplicite_normal);
						newsText.setPadding(0, 5, 0, 5);
						newsText.setText(n.date + "\n" + n.title + ":\n" + n.content);
						newsRow.addView(newsText);
						
						newsTable.addView(newsRow);
					}
					homeLayout.addView(newsTable);					
				} else {
					TextView newsText = new TextView(AppSessionHome.this);
					newsText.setText(getText("NODATA", "No data"));
					homeLayout.addView(newsText);
				}
				
				if (app.grant.hasResponsibility("WEB_ADMIN")) {
					Button newsButton = new Button(AppSessionHome.this);
					newsButton.setText(getText("ADD", "Add"));
					newsButton.setOnClickListener(new Button.OnClickListener() {
						@Override
						public void onClick(View button) {
							startBusinessObjectFormActivity("WebNews", "the_WebNews", Field.DEFAULT_ROW_ID);
						}
					});
					homeLayout.addView(newsButton);
				}

				if (app.grant.hasResponsibility("ADMIN")) {
					TextView infoTitle = new TextView(AppSessionHome.this);
					infoTitle.setText(getText("SYSTEM_INFO", "System info"));
					infoTitle.setTextAppearance(AppSessionHome.this, R.style.simplicite_subtitle);
					homeLayout.addView(infoTitle);

					TextView infoText = new TextView(AppSessionHome.this);
					if (app.sysinfo != null) {
						infoText.setTextAppearance(AppSessionHome.this, R.style.simplicite_normal);
						infoText.setText("Name : " + app.name + "\n");
						infoText.append("Version : " + app.version + "\n");
						infoText.append("Platform : " + app.platformVersion + "\n");
						infoText.append("Encoding : " + app.encoding + "\n");
						infoText.append("Grant cache : " + app.sysinfo.cachegrant + " / " + app.sysinfo.cachegrantmax + "\n");
						infoText.append("Object cache : " + app.sysinfo.cacheobject + " / " + app.sysinfo.cacheobjectmax + "\n");
						infoText.append("Memory : " + (app.sysinfo.heapsize/1024) + " Mb / " + (app.sysinfo.heapmaxsize/1024) + " Mb\n");
						infoText.append("Cache dir : " + (app.sysinfo.dircache/1024) + " Gb\n");
						infoText.append("DBDoc dir : " + (app.sysinfo.dirdbdoc/1024) + " Gb\n");
						infoText.append("RecycleBin dir : " + (app.sysinfo.dirrecyclebin/1024) + " Gb\n");
						infoText.append("Disk free : " + (app.sysinfo.diskfree/1024) + " Gb");
					} else
						infoText.setText(getText("NO_DATA", "No data"));
					homeLayout.addView(infoText);
					
					Button clearCacheButton = new Button(AppSessionHome.this);
					clearCacheButton.setText(getText("SYS_CLEAR_CACHE", "Clear cache"));
					clearCacheButton.setOnClickListener(new Button.OnClickListener() {
						@Override
						public void onClick(View button) {
							new LoadingAsyncTask(true, true) {
								@Override
								protected Object task(Object[] params) throws Exception {
						        	BusinessObject appLoggerMemory = app.getBusinessObject("AppLoggerMemory");
						        	return appLoggerMemory.action("SYS_RESET_CACHE");
								}
								
								@Override
								protected void postTask(Object result) {
									toastMessage(AppSession.isEmpty(result) ? getText("SYS_CACHE_CLEARED", "Cache cleared") : (String)result);
								}
							}.execute();
						}
					});
					homeLayout.addView(clearCacheButton);
				}
			}
		}.execute();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	        	final Bitmap bitmap = (Bitmap)intent.getExtras().get("data");
				
	        	new LoadingAsyncTask(true, true) {
					@Override
					protected Object task(Object[] params) throws Exception {
			        	Document image = new Document();
			        	image.setData(bitmap, CompressFormat.JPEG);
			        	image.id = app.grant.picture != null ? app.grant.picture.id : Field.DEFAULT_ROW_ID;
			        	image.name = app.grant.login + ".jpg";
			        	
			        	BusinessObject user = app.getBusinessObject("User");
			        	user.get(app.grant.userid, false, false);
			        	user.item.put("usr_image_id", image);
			        	return user.update();
					}
					
					@Override
					protected void postTask(Object result) {
			        	toastMessage(getText("SAVE_OK", "Save OK"));
			        	((ImageView)findViewById(R.id.grantImageView)).setImageBitmap(bitmap);
					}
				}.execute();
	        }
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			for (int i = 0; i < app.menu.domains.size(); i++) {
				MenuDomain domain = app.menu.domains.get(i);
				SubMenu menuItem = menu.addSubMenu(domain.label);

				for (int j = 0; j < domain.entries.size(); j++) {
					MenuEntry entry = domain.entries.get(j);
					MenuItem subMenuItem = menuItem.add(i, j, j, entry.label);
					menuItem.setIcon(R.drawable.menu_domain);
					if (entry.type.equals(MenuEntry.TYPE_BUSINESSOBJECT) || entry.type.equals(MenuEntry.TYPE_BUSINESSOBJECT_STATUS)) {
						subMenuItem.setIcon(R.drawable.menu_object);
					} else if (entry.type.equals(MenuEntry.TYPE_BUSINESSPROCESS) || entry.type.equals(MenuEntry.TYPE_BUSINESSWORKFLOW)) {
						subMenuItem.setIcon(R.drawable.menu_process);
					} else if (entry.type.equals(MenuEntry.TYPE_EXTERNALOBJECT)) {
						subMenuItem.setIcon(R.drawable.menu_extobject);
						subMenuItem.setEnabled(getExternalObjectActivity(entry.name) != null);
					}
				}
			}
		} catch (Exception e) {
			error(e.getMessage());
		}
		return super.onCreateOptionsMenu(menu);
	}

	private void startBusinessObjectSearchActivity(String name, String instance) {
		startBusinessObjectActivity(BusinessObjectSearch.class, name, instance, null);
	}
	
	private void startBusinessObjectListActivity(String name, String instance) {
		startBusinessObjectActivity(BusinessObjectList.class, name, instance, null);
	}
	
	private void startBusinessObjectFormActivity(String name, String instance, String rowId) {
		startBusinessObjectActivity(BusinessObjectForm.class, name, instance, rowId);
	}
	
	private void startBusinessObjectActivity(Class<?> activity, String name, String instance, String rowId) {
		Intent intent = new Intent(this, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		setExtraValue(intent, BusinessObjectCommon.EXTRA_OBJECTNAME, name);
		setExtraValue(intent, BusinessObjectCommon.EXTRA_OBJECTINSTANCENAME, instance);
		if (rowId != null)
			setExtraValue(intent, BusinessObjectCommon.EXTRA_ROWID, Field.DEFAULT_ROW_ID);
		startActivity(intent);
	}
	
	private void startBusinessProcessActivity(String name) {
		// TODO
		notYetImplemented();
	}
	
	private Class<?> getExternalObjectActivity(String name) {
		try {
			return Class.forName(getPackageName() + "." + app.name + "." + name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	private void startExternalObjectActivity(String name) {
		Class<?> activity = getExternalObjectActivity(name);
		if (activity != null) {
			Intent intent = new Intent(this, activity);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			setExtraValue(intent, ExternalObjectCommon.EXTRA_EXTERNALOBJECTNAME, name);
			startActivity(intent);
		} else
			info("No Android implementation for external object " + name);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getSubMenu() == null) {
			// Start search activity for object
			MenuDomain domain = app.menu.domains.get(item.getGroupId());
			MenuEntry entry = domain.entries.get(item.getItemId());
			if (entry.type.equals(MenuEntry.TYPE_BUSINESSOBJECT) || entry.type.equals(MenuEntry.TYPE_BUSINESSOBJECT_STATUS)) {
				if (entry.subtype != null && entry.subtype.equals(MenuEntry.SUBTYPE_LIST))
					startBusinessObjectListActivity(entry.name, "the_" + entry.name);
				else
					startBusinessObjectSearchActivity(entry.name, "the_" + entry.name);
				return true;
			} else if (entry.type.equals(MenuEntry.TYPE_BUSINESSPROCESS) || entry.type.equals(MenuEntry.TYPE_BUSINESSWORKFLOW)) {
				startBusinessProcessActivity(entry.name);
				return true;
			} else if (entry.type.equals(MenuEntry.TYPE_EXTERNALOBJECT)) {
				startExternalObjectActivity(entry.name);
				return true;
			} else
				return false;
		}
		else
			return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onBackPressed() {
		yesnoDialog("QUIT", null, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppSessionHome.super.onBackPressed();
				}
			}, null);
	}
}