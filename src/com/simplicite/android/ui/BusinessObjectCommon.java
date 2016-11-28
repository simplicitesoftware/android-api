/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;

import com.simplicite.android.core.AppException;
import com.simplicite.android.core.AppSession;
import com.simplicite.android.core.BusinessObject;
import com.simplicite.android.core.Document;
import com.simplicite.android.core.Field;
import com.simplicite.android.core.ListOfValues;

public abstract class BusinessObjectCommon extends AppSessionCommon {
	/**
	 * <p>Simplicité(R) business object
	 */
	protected BusinessObject obj = null;
	/**
	 * <p>Simplicité(R) parent business object
	 */
	protected BusinessObject parent = null;
	
	public final static String EXTRA_OBJECTNAME = "objectName";
	public final static String EXTRA_OBJECTINSTANCENAME = "objectInstanceName";
	public final static String EXTRA_ROWID = "objectRowId";
	public final static String EXTRA_PARENTNAME = "parentName";
	public final static String EXTRA_PARENTINSTANCENAME = "parentInstanceName";
	public final static String EXTRA_PARENTREFFIELDNAME = "parentRefFieldName";
	public final static String EXTRA_PARENTROWID = "parentRowId";

	public void setTitle() {
		setTitle((parent == null ? "" : parent.label + " > ") + obj.label);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			String name = getExtraValue(EXTRA_OBJECTNAME);
			if (name == null)
				throw new AppException("No business object name");

			obj = app.getBusinessObject(name, getExtraValue(EXTRA_OBJECTINSTANCENAME));
			
			parent = null;
			obj.clearParent();
			obj.parentName = getExtraValue(EXTRA_PARENTNAME);
			if (obj.parentName != null) {
				obj.parentInstance = getExtraValue(EXTRA_PARENTINSTANCENAME);
				parent = app.getBusinessObject(obj.parentName, obj.parentInstance);
				obj.parentRefField = getExtraValue(EXTRA_PARENTREFFIELDNAME);
				obj.parentRowId = getExtraValue(EXTRA_PARENTROWID);
			}
		} catch (Exception e) {
			error(e);
			finish();
		}
	}
	
	protected void setIntentExtraValues(Intent intent, String rowId) {
		setExtraValue(intent, EXTRA_OBJECTNAME, obj.name);
		setExtraValue(intent, EXTRA_OBJECTINSTANCENAME, obj.instance);
		if (obj.parentName != null) {
			setExtraValue(intent, EXTRA_PARENTNAME, obj.parentName);
			setExtraValue(intent, EXTRA_PARENTINSTANCENAME, obj.parentInstance);
			setExtraValue(intent, EXTRA_PARENTREFFIELDNAME, obj.parentRefField);
			setExtraValue(intent, EXTRA_PARENTROWID, obj.parentRowId);
		}
		if (rowId != null)
			setExtraValue(intent, EXTRA_ROWID, rowId);
	}
	
	private class FieldAndValue {
		public Field field;
		public Object value;

		public FieldAndValue(Field field, Object value) {
			this.field = field;
			this.value = value;
		}
	}
	
	/**
	 * <p>Generate text field input</p>
	 * @param field Field
	 * @param value Field value
	 * @param setInputType Set text input type depending on text type
	 */
	protected EditText textInput(Field field, String value, boolean setInputType) {
		EditText input = new EditText(this);

		input.setId(field.hashCode());
		input.setTag(field);
		
		input.setText(value);
		if (setInputType) {
			if (field.type == Field.TYPE_PHONENUM) {
				input.setInputType(InputType.TYPE_CLASS_PHONE);
			} else if (field.type == Field.TYPE_DATETIME) {
				input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);
			} else if (field.type == Field.TYPE_DATE) {
				input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
			} else if (field.type == Field.TYPE_TIME) {
				input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
			} else if (field.type == Field.TYPE_INT) {
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
			} else if (field.type == Field.TYPE_FLOAT || field.type == Field.TYPE_FLOAT_EMPTY) {
				input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			} else if (field.type == Field.TYPE_EMAIL) {
				input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			} else if (field.type == Field.TYPE_LONG_STRING || field.type == Field.TYPE_NOTEPAD || field.type == Field.TYPE_HTML) {
				input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			} else {
				input.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		}
		return input;
	}
	
	/**
	 * <p>Generate enumeration field input as drop down list</p>
	 * @param field Field
	 * @param selectedCode Field value
	 * @param all All items or only enabled ones ?
	 */
	protected Button enumInputAsDropDown(final Field field, final String selectedCode, boolean all, boolean emptyItem) {
		final Button button = new Button(this);

		button.setId(field.hashCode());
		button.setTag(new FieldAndValue(field, selectedCode));

		final ArrayList<ListOfValues.Item> items = new ArrayList<ListOfValues.Item>();

		int index = 0;
		int selectedIndex = -1;
		
		if (emptyItem) {
			ListOfValues.Item item = field.listOfValues.new Item();
			item.code = "";
			item.value = "";
			items.add(item);
			index++;
		}
		
		for (ListOfValues.Item item : field.listOfValues.items) {
			if (all || item.enabled) {
				items.add(item);
				if (item.code.equals(selectedCode)) {
					selectedIndex = index;
					button.setText(item.value);
				}
				index++;
			}
		}
		
		final String[] is = new String[items.size()];
		for (int i = 0; i < is.length; i++) {
			is[i] = items.get(i).value;
		}
		final int s = selectedIndex;
		
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(BusinessObjectCommon.this);
				builder.setTitle(field.label);
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int index) {
				    	ListOfValues.Item item = items.get(index);
				        button.setText(item.value);
				        ((FieldAndValue)button.getTag()).value = item.code;
						dialog.dismiss();
				    }
				};
				if (s == -1 || AppSession.isEmpty(selectedCode))
					builder.setItems(is, listener);
				else
					builder.setSingleChoiceItems(is, s, listener);
				builder.create().show();
			}	
		});
		
		return button;
	}
	
	/**
	 * <p>Generate enumeration field input as radio buttons</p>
	 * @param field Field
	 * @param value Field value
	 * @param all All items or only enabled ones ?
	 */
	protected RadioGroup enumInputAsRadioButtons(Field field, String selectedCode, boolean all) {
		RadioGroup radioGroup = new RadioGroup(this);

		radioGroup.setId(field.hashCode());
		radioGroup.setTag(field);

		radioGroup.setOrientation(RadioGroup.VERTICAL);
		int checkedId = -1;
		int bid = 0;
		for (ListOfValues.Item item : field.listOfValues.items) {
			if (all || item.enabled) {
				RadioButton r = new RadioButton(this);
				r.setId(bid++);
				r.setTag(item.code);
				r.setText(item.value);
				if (item.code.equals(selectedCode))
					checkedId = r.getId();
				radioGroup.addView(r);
			}
		}
		radioGroup.check(checkedId);

		return radioGroup;
	}
	
	/**
	 * <p>Generate multiple field input as check box</p>
	 * @param field Field
	 * @param value Field value
	 */
	protected LinearLayout multipleEnumInputAsCheckBoxes(Field field, ArrayList<String> values) {
		LinearLayout checkboxes = new LinearLayout(this);
		checkboxes.setOrientation(LinearLayout.VERTICAL);
		checkboxes.setTag(field);
		checkboxes.setId(field.hashCode());
		
		int id = 0;
		for (ListOfValues.Item item : field.listOfValues.items) {
			CheckBox checkbox = new CheckBox(this);

			checkbox.setId(id++);
			checkbox.setTag(item);

			checkbox.setChecked(values != null ? values.contains(item.code) : false);
			
			checkboxes.addView(checkbox);
		}

		return checkboxes;
	}

	/**
	 * <p>Generate boolean field input as check box</p>
	 * @param field Field
	 * @param value Field value
	 */
	protected CheckBox booleanInputAsCheckBox(Field field, Boolean value) {
		CheckBox checkbox = new CheckBox(this);

		checkbox.setId(field.hashCode());
		checkbox.setTag(field);

		checkbox.setChecked(value != null ? value.booleanValue() : false);

		return checkbox;
	}

	/**
	 * <p>Generate boolean field input as radio buttons</p>
	 * @param field Field
	 * @param value Field value
	 */
	protected RadioGroup booleanInputAsRadioButtons(Field field, Boolean value) {
		RadioGroup radioGroup = new RadioGroup(this);

		radioGroup.setId(field.hashCode());
		radioGroup.setTag(field);

		radioGroup.setOrientation(RadioGroup.HORIZONTAL);
		RadioButton r = new RadioButton(this);
		r.setId(0);
		r.setTag(new Boolean(false));
		r.setText(getText("NO", "No"));
		radioGroup.addView(r);
		r = new RadioButton(this);
		r.setId(1);
		r.setTag(new Boolean(true));
		r.setText(getText("YES", "Yes"));
		radioGroup.addView(r);
		if (value != null)
			radioGroup.check(value.booleanValue() ? 1 : 0);

		return radioGroup;
	}
	
	/**
	 * <p>Generate image field input</p>
	 * @param field Field
	 * @param value Field value
	 */
	protected ImageView imageInput(Field field, Document value) {
		ImageView image = image(value, 2, 100);
		
		image.setId(field.hashCode());
		image.setTag(new FieldAndValue(field, value));

		image.setOnClickListener(new ImageView.OnClickListener() {
			@Override
			public void onClick(View image) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, image.getId());
			}
		});
		return image;
	}
	
	/**
	 * <p>Generate document field input</p>
	 * @param field Field
	 * @param value Field value
	 */
	protected View documentInput(Field field, Document value) {
		// TODO
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
        if (resultCode == RESULT_OK) {
        	View input = findViewById(requestCode);
        	if (input instanceof ImageView) {
	        	Bitmap bitmap = (Bitmap)intent.getExtras().get("data");
	        	try {
		        	ImageView image = (ImageView)findViewById(requestCode);
		        	FieldAndValue t = (FieldAndValue)image.getTag();
	
		        	Document oldvalue = null;
		        	try { oldvalue = (Document)obj.item.get(t.field.name); } catch (Exception e) {}
		        	
		        	Document newvalue = new Document();
		        	newvalue.setData(bitmap, CompressFormat.JPEG);
		        	newvalue.id = oldvalue != null ? oldvalue.id : Field.DEFAULT_ROW_ID;
		        	newvalue.name = t.field.name + "_" + obj.item.get(obj.rowIdField) + ".jpg";
		        	t.value = newvalue;
		        	
		        	image.setImageBitmap(bitmap);
	        	} catch (Exception e) {
	        		error(e);
	        	}
	        }
        }
	}	
	
	/**
	 * <p>Get input value</p>
	 * @param input Input
	 */
	protected Object getInputValue(View input) {
		if (input == null) return null;
		
		if (input instanceof EditText) {
			return ((EditText)input).getText().toString();
		} else if (input instanceof Button) {
			return ((FieldAndValue)((Button)input).getTag()).value;
		} else if (input instanceof RadioGroup) {
			RadioGroup rg = (RadioGroup)input;
			RadioButton r = (RadioButton)rg.findViewById(rg.getCheckedRadioButtonId());
			return r != null ? r.getTag() : null;
		} else if (input instanceof CheckBox) {
			return new Boolean(((CheckBox)input).isChecked());
		} else if (input instanceof ImageView) {
			return (Document)((FieldAndValue)((ImageView)input).getTag()).value;
		} else
			return null;
	}
	
	/**
	 * <p>Set input value</p>
	 * @param input Input
	 * @param value Value
	 */
	protected void setInputValue(View input, Object value) {
		if (input == null) return;
		
		if (input instanceof EditText) {
			((EditText)input).setText((String)value);
		}/* else if (input instanceof Button) {
			return ((FieldAndValue)((Button)input).getTag()).value;
		} else if (input instanceof RadioGroup) {
			RadioGroup rg = (RadioGroup)input;
			RadioButton r = (RadioButton)rg.findViewById(rg.getCheckedRadioButtonId());
			return r != null ? r.getTag() : null;
		} else if (input instanceof CheckBox) {
			return new Boolean(((CheckBox)input).isChecked());
		} else if (input instanceof ImageView) {
			return (Document)((FieldAndValue)((ImageView)input).getTag()).value;
		} else
			return null;*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		addHomeMenuItem(menu);
		return super.onCreateOptionsMenu(menu);
	}
}