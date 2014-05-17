/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import java.util.HashMap;

import com.simplicite.android.R;
import com.simplicite.android.core.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusinessObjectForm extends BusinessObjectCommon {
	private boolean create = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businessobjectform);
		goHomeOnLogo();
		
		((Button)findViewById(R.id.saveButton)).setText(getText("SAVE", "Save"));
		((Button)findViewById(R.id.deleteButton)).setText(getText("DELETE", "Delete"));
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		if (!isAfterActivityResult()) {
			String rowId = getExtraValue(EXTRA_ROWID);
			if (rowId != null)
				buildForm(rowId);
			else
				error("No row ID");
		}
		else
			setAfterActivityResult(false);
	}

	private HashMap<String, View> inputs = new HashMap<String, View>();

	private void buildForm(final String rowId) {
		create = rowId.equals(Field.DEFAULT_ROW_ID);

		final LinearLayout formLayout = (LinearLayout)findViewById(R.id.formLayout);
		formLayout.removeAllViews();
		
		new LoadingAsyncTask(formLayout, true) {
			@Override
			protected void preTask() {
				findViewById(R.id.saveButton).setEnabled(false);
				findViewById(R.id.deleteButton).setEnabled(false);
			}
			
			@Override
			protected Object task(Object[] params) throws Exception {
				obj.get(rowId, true, false);
				// ZZZ getMetadata must be called after get
				if (create) {
					String param = parent == null ? null : parent.name + ":" + parent.instance;
					obj.getMetaData(BusinessObject.CONTEXT_CREATE, param);
					
					// Refered field from parent object are populated
					if (parent != null)
						populateRefFields(obj.fields.get(obj.parentRefField), parent.item, parent.rowIdField);
				} else {
					obj.getMetaData(BusinessObject.CONTEXT_UPDATE, rowId);
				}
				return obj;
			}
			
			@Override
			protected void postTask(Object result) {
				setTitle();
				setHelp(obj.help);
				
				findViewById(R.id.saveButton).setEnabled(obj.update);
				findViewById(R.id.deleteButton).setEnabled(obj.del);

				for (Field field : obj.fieldsByOrder) {
					if (field.isFormVisible() || field.refId && !field.ref) {
						TextView label = new TextView(BusinessObjectForm.this);
						label.setText(field.label);
						formLayout.addView(label);
						if (field.required && !field.ref)
							label.setTextAppearance(getApplicationContext(), R.style.simplicite_fieldreq);
						else if (field.ref)
							label.setTextAppearance(getApplicationContext(), R.style.simplicite_fieldref);
						else
							label.setTextAppearance(getApplicationContext(), R.style.simplicite_fieldnormal);
					}
					
					if (field.isFormVisible()) {
						Object value = obj.item.get(field.name);
						View input = null;
						// TODO : other field types
						if (field.type == Field.TYPE_BOOLEAN) {
							if (field.required)
								input = booleanInputAsCheckBox(field, value instanceof Boolean ? (Boolean)value : null);
							else
								input = booleanInputAsRadioButtons(field, value instanceof Boolean ? (Boolean)value : null);
						} else if (field.type == Field.TYPE_ENUM) {
							boolean req = field.required && !AppSession.isEmpty(value);
							input = (field.listOfValues.items.size() <= 3)
								? enumInputAsRadioButtons(field, (String)value, false)
								: enumInputAsDropDown(field, (String)value, false, !req);
						} else if (field.type == Field.TYPE_IMAGE) {
							input = imageInput(field, value instanceof Document ? (Document)value : null);
						} else {
							input = textInput(field, (String)value, true);
						}
						input.setEnabled((create && obj.create || obj.update) && field.updatable && !field.ref);
						formLayout.addView(input);
						inputs.put(field.name, input);
					}
					
					if (field.refId && !field.ref) {
						Button selectButton = new Button(BusinessObjectForm.this);
						selectButton.setText(getText("SELECT", "Select"));
						selectButton.setId(field.hashCode());
						selectButton.setTag(field);
						selectButton.setEnabled(field.updatable);
						
						selectButton.setOnClickListener(new Button.OnClickListener() {
							public void onClick(View button) {
								Field field = (Field)button.getTag();
								Intent intent = new Intent(BusinessObjectForm.this, BusinessObjectSelect.class);
								setExtraValue(intent, EXTRA_OBJECTNAME, field.refObject);
								setExtraValue(intent, EXTRA_OBJECTINSTANCENAME, "select_" + field.refObject);
								setExtraValue(intent, EXTRA_PARENTNAME, obj.name);
								setExtraValue(intent, EXTRA_PARENTINSTANCENAME, obj.instance);
								setExtraValue(intent, EXTRA_PARENTREFFIELDNAME, field.name);
								setExtraValue(intent, EXTRA_PARENTROWID, (String)obj.item.get(obj.rowIdField));
								startActivityForResult(intent, button.getId());
							}
						});

						formLayout.addView(selectButton);
					}
				}
				
				Button saveButton = (Button)findViewById(R.id.saveButton);
				saveButton.setEnabled(create && obj.create || obj.update);
				saveButton.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View button) {
						// Update item
						new LoadingAsyncTask(true, true) {
							@Override
							protected Object task(Object[] params) throws Exception {
								for (String name : obj.fields.keySet()) {
									Object value = getInputValue(inputs.get(name));
									if (value != null)
										obj.item.put(name, value);
								}

								return create ? obj.create() : obj.update();
							}
							
							@Override
							protected void postTask(Object result) {
					        	toastMessage(getText("SAVE_OK", "Save OK"));
								// Force form regeneration
								buildForm((String)obj.item.get(obj.rowIdField));
							}
						}.execute();
					}
				});
				
				Button deleteButton = (Button)findViewById(R.id.deleteButton);
				deleteButton.setEnabled(!create && obj.del);
				deleteButton.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View button) {
						yesnoDialog("DELETE", null, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// Delete item...
								new LoadingAsyncTask(true, true) {
									@Override
									protected Object task(Object[] params) throws Exception {
										obj.getMetaData(BusinessObject.CONTEXT_DELETE);
										obj.del();
										return obj;
									}
									
									@Override
									protected void postTask(Object result) {
										// ...and close activity
										BusinessObjectForm.this.finish();
									}
								}.execute();
							}
						}, null);
					}
				});
			}
		}.execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
        if (resultCode == RESULT_OK) {
        	View v = findViewById(requestCode);
        	if (v instanceof Button) {
        		Button button = (Button)v;
	        	try {
	        		Field field = (Field)button.getTag();
	        		
					String rowIdField = (String)intent.getExtras().get("ROWIDFIELD");
	        		@SuppressWarnings("unchecked")
					HashMap<String, Object> item = (HashMap<String, Object>)intent.getExtras().get("ITEM");
	        		populateRefFields(field, item, rowIdField);
	        	} catch (Exception e) {
	        		error(e);
	        	}
	        }
        }
	}
	
	private void populateRefFields(Field fk, HashMap<String, Object> item, String rowIdField) {
		obj.item.put(fk.name, item.get(rowIdField));
		for (String n : obj.fields.keySet()) {
			Field f = obj.fields.get(n);
			if (f.ref && f.name.startsWith(fk.name)) {
				String name = f.name.replaceFirst(fk.name + "__", "");
				Object value = item.get(name);
				if (value != null) {
					debug(f.name + " < " + name + " = " + value);
					obj.item.put(n, value);
					setInputValue(inputs.get(n), value);
				}
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!create) {
			int i = 0;
			for (Link link : obj.links) {
				MenuItem item = menu.add(1, i, i, link.label);
				item.setIcon(R.drawable.menu_object);
				i++;
			}
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getGroupId() == 1) {
			// Start panel list activity for object
			Link link = obj.links.get(item.getItemId());
			Intent intent = new Intent(this, BusinessObjectList.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			setExtraValue(intent, EXTRA_OBJECTNAME, link.object);
			setExtraValue(intent, EXTRA_OBJECTINSTANCENAME, "panel_" + link.object);
			setExtraValue(intent, EXTRA_PARENTNAME, obj.name);
			setExtraValue(intent, EXTRA_PARENTINSTANCENAME, obj.instance);
			setExtraValue(intent, EXTRA_PARENTREFFIELDNAME, link.field);
			setExtraValue(intent, EXTRA_PARENTROWID, (String)obj.item.get(obj.rowIdField));
			startActivity(intent);
			return true;
		}
		else
			return super.onMenuItemSelected(featureId, item);
	}
}