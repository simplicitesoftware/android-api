/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.simplicite.android.R;
import com.simplicite.android.core.AppSession;
import com.simplicite.android.core.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public abstract class AppSessionCommon extends Activity {
	/**
	 * <p>Simplicité(R) application
	 */
	public static AppSession app;
	
	/**
	 * <p>Prefix for intent extras keys</p>
	 */
	protected static final String EXTRA_PREFIX = "simplicite_";

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		TextView t = (TextView)findViewById(R.id.simplicite_title);
		if (t != null) t.setText(title);
	}
	
	protected String getText(String code, String def) {
		return app != null ? app.getText(code, def) : def;
	}
	
	private String logTag = AppSession.LOG_TAG + "/" + getClass().getSimpleName();
	
	private String logMessage(Exception e) {
		if (e == null)
			return null;
		else {
			String cause = "";
			if (e.getCause() != null) {
				cause = " caused by " + e.getCause().getClass().getSimpleName();
				String causeMsg = e.getCause().getMessage();
				if (causeMsg == null)
					cause += " (" + causeMsg + ")";
			}
			
			if (e.getMessage() == null)
				return e.getClass().getSimpleName() + cause;
			else
				return e.getMessage() + cause;
		}
	}
	
	private String logMessage(String msg) {
		return msg == null ? "null" : msg;
	}
	
	/**
	 * <p>Display and log fatal error message</p>
	 * @param msg Message
	 */
	protected void fatal(String msg) {
		String m = logMessage(msg);
		Log.wtf(logTag, m);
		message("FATAL", m, android.R.drawable.ic_dialog_alert, true);
	}

	/**
	 * <p>Display and log fatal error message</p>
	 * @param e Exception
	 */
	protected void fatal(Exception e) {
		fatal(logMessage(e));
	}

	/**
	 * <p>Display and log error message</p>
	 * @param msg Message
	 */
	protected void error(String msg) {
		String m = logMessage(msg);
		Log.e(logTag, m);
		message("ERROR", m, android.R.drawable.ic_dialog_alert, false);
	}

	/**
	 * <p>Display and log error message</p>
	 * @param e Exception
	 */
	protected void error(Exception e) {
		error(logMessage(e));
	}

	/**
	 * <p>Display and log warning message</p>
	 * @param msg Message
	 */
	protected void warning(String msg) {
		String m = logMessage(msg);
		Log.w(logTag, m);
		message("WARNING", m, android.R.drawable.ic_dialog_alert, false);
	}

	/**
	 * <p>Display info message/p>
	 * @param msg Message
	 */
	protected void info(String msg) {
		String m = logMessage(msg);
		Log.i(logTag, m);
		message("INFO", m, android.R.drawable.ic_dialog_info, false);
	}

	/**
	 * <p>Log debug message</p>
	 * @param msg Message
	 */
	protected void debug(String msg) {
		if (AppSession.isDebug())
			Log.d(logTag, logMessage(msg));
	}

	private void message(String title, String msg, int iconId, final boolean finish) {
		okDialog(title, msg, iconId, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					if (finish) finish();
				}
			});
	}

	/**
	 * <p>Display a &quot;not yet implemented&quot; warning</p>
	 */
	protected void notYetImplemented() {
		warning(getText("NOT_IMPLEMENTED", "Not implemented"));
	}

	/**
	 * <p>Simple OK dialog</p>
	 * @param title Title (looked up as a text code)
	 * @param msg Message
	 * @param onClick On click handler
	 */
	protected void okDialog(String title, String msg, DialogInterface.OnClickListener onClick) {
		okDialog(title, msg, android.R.drawable.ic_dialog_info, onClick);
	}
	/**
	 * <p>Simple OK dialog</p>
	 * @param title Title (looked up as a text code)
	 * @param msg Message
	 * @param iconId Icon ID (e.g. android.R.drawable.ic_dialog_info)
	 * @param onClick On click handler
	 */
	protected void okDialog(String title, String msg, int iconId, DialogInterface.OnClickListener onClick) {
		new AlertDialog.Builder(this)
			.setTitle(getText(title, title))
			.setMessage(msg)
			.setIcon(iconId)
			.setCancelable(false)
			.setPositiveButton(getText("OK", "Ok"), onClick).show();
	}
	
	/**
	 * <p>Simple OK dialog</p>
	 * @param title Title (looked up as a text code)
	 * @param msg Message
	 * @param onYesClick On click handler for yes
	 * @param onNoClick On click handler for no
	 */
	protected void yesnoDialog(String title, String msg, DialogInterface.OnClickListener onYesClick, DialogInterface.OnClickListener onNoClick) {
		yesnoDialog(title, msg, android.R.drawable.ic_dialog_alert, onYesClick, onNoClick);
	}
	/**
	 * <p>Simple OK dialog</p>
	 * @param title Title (looked up as a text code)
	 * @param msg Message
	 * @param iconId Icon ID (e.g. android.R.drawable.ic_dialog_alert)
	 * @param onYesClick On click handler for yes
	 * @param onNoClick On click handler for no
	 */
	protected void yesnoDialog(String title, String msg, int iconId, DialogInterface.OnClickListener onYesClick, DialogInterface.OnClickListener onNoClick) {
		new AlertDialog.Builder(this)
			.setTitle(getText(title, title))
			.setMessage(msg)
			.setIcon(iconId)
			.setCancelable(false)
			.setPositiveButton(getText("YES", "Yes"), onYesClick)
			.setNegativeButton(getText("NO", "No"), onNoClick).show();
	}
	
	/**
	 * <p>Display toast message</p>
	 * @param msg Message
	 */
	public void toastMessage(String msg) {
    	Toast.makeText(AppSessionCommon.this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * <p>Get text value from an text field</p>
	 * @param id Text field ID
	 * @param trim Trim value ?
	 */
	protected String getEditTextValue(int id, boolean trim) {
		EditText e = (EditText)findViewById(id);
		if (e == null) return null;
		String val = e.getText().toString();
		if (val == null) return null;
		return trim ? val.trim() : val;
	}
	
	/**
	 * <p>Set text value from an text field</p>
	 * @param id Text field ID
	 * @param value Value
	 */
	protected void setEditTextValue(int id, String value) {
		EditText e = (EditText)findViewById(id);
		if (e != null) e.setText(value);
	}
	
	/**
	 * <p>Set Intent extra value</p>
	 * @param name Extra name (EXTRA_PREFIX is used as prefix)
	 * @param value Value
	 */
	protected void setExtraValue(Intent intent, String name, String value) {
		intent.putExtra(EXTRA_PREFIX + name, value);
	}
	
	/**
	 * <p>Get value from Intent extras</p>
	 * @param name Extra name (EXTRA_PREFIX is used as prefix)
	 */
	protected String getExtraValue(String name) {
		Bundle extras = getIntent().getExtras();
		if (extras == null) return null;
		return extras.getString(EXTRA_PREFIX + name);
	}
	
	/**
	 * <p>Show soft keyboard</p>
	 */
	protected void showKeyboard() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}
	
	/**
	 * <p>Hide soft keyboard</p>
	 */
	protected void hideKeyboard() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	/**
	 * <p>Generate image view from document</p>
	 * @param d Document
	 */
	protected ImageView image(Document d, int padding, int maxSize) {
		ImageView image = new ImageView(this);
		if (d != null) {
			try {
				image.setImageBitmap(d.getBitmap());
				image.setPadding(padding, padding, padding, padding);
				image.setAdjustViewBounds(true);
				image.setMaxWidth(maxSize);
				image.setMaxHeight(maxSize);
			} catch (Exception e) {}
			image.setTag(d.name);
		} else
			image.setImageResource(R.drawable.empty);
		return image;
	}

	/**
	 * <p>Generate image view from document's image thumbnail</p>
	 * @param d Document
	 */
	protected ImageView thumbnail(Document d, int padding, int maxSize) {
		ImageView image = new ImageView(this);
		if (d != null && d.thumbnail != null) {
			try {
				image.setImageBitmap(d.getThumbnailBitmap());
				image.setPadding(padding, padding, padding, padding);
				image.setAdjustViewBounds(true);
				image.setMaxWidth(maxSize);
				image.setMaxHeight(maxSize);
			} catch (Exception e) {}
			image.setTag(d.name);
		} else
			image.setImageResource(R.drawable.empty);
		return image;
	}

	protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 564865;
	protected static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 564867;
	
	protected static final int MEDIA_TYPE_IMAGE = 1;
	protected static final int MEDIA_TYPE_VIDEO = 2;

	protected File getOutputMediaFile(int type, String dataDir) {
		File storageDir = new File(getFilesDir(), dataDir);

		if (!storageDir.exists()) {
			if (!storageDir.mkdirs()) return null;
		}

		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		File file = null;
		if (type == MEDIA_TYPE_IMAGE) {
			file = new File(storageDir.getPath() + File.separator + "IMG" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			file = new File(storageDir.getPath() + File.separator + "VID" + timeStamp + ".mp4");
		}

		debug("FILE : " + file.getAbsolutePath());
		try { file.createNewFile(); } catch (IOException e) { return null; }
		
		return file;
	}
	
	/**
	 * <p>Set and show help text view</p>
	 * @param help Help text
	 */
	protected void setHelp(String help) {
		if (!AppSession.isEmpty(help)) {
			TextView helpView = (TextView)findViewById(R.id.simplicite_help);
			if (helpView != null) {
				helpView.setText(Html.fromHtml(help));
				helpView.setVisibility(TextView.VISIBLE);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		debug("create");
		super.onCreate(savedInstanceState);
		setTitle("");
	}
	
	@Override
	protected void onDestroy() {
		debug("destroy");
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		debug("start");
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		debug("stop");
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		debug("resume");
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		debug("restart");
		super.onResume();
	}
	
	private boolean afterActivityResult = false;
	
	/**
	 * <p>Is after activity result</p>
	 */
	public boolean isAfterActivityResult() {
		return afterActivityResult;
	}

	/**
	 * <p>Set after activity result</p>
	 * @param afterActivityResult
	 */
	public void setAfterActivityResult(boolean afterActivityResult) {
		this.afterActivityResult = afterActivityResult;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		debug("activity result");
		super.onActivityResult(requestCode, resultCode, data);
		afterActivityResult = true;
	}

	protected abstract class LoadingAsyncTask extends AsyncTask<Object, Void, Object> {
		private boolean displayProgress = true;
		private ProgressDialog progressDialog = null;
		private ViewGroup progressBarTarget = null;
		private ProgressBar progressBar = null;
		
		private Exception exception = null;
		private boolean displayError = true;
		
		/**
		 * <p>Default constructor (display progress dialog and errors)</p>
		 */
		public LoadingAsyncTask() {
			this(true, true);
		}
		
		/**
		 * <p>Constructor</p>
		 * @param displayProgress Display progress dialog ?
		 * @param displayError Display progress error for exception thrown by task() ?
		 */
		public LoadingAsyncTask(boolean displayProgress, boolean displayError) {
			this.displayProgress = displayProgress;
			this.displayError = displayError;
		}
		
		/**
		 * <p>Constructor</p>
		 * @param progressBarTarget Target view group where to display a progress bar (null will display a progress dialog)
		 * @param displayError Display progress error for exception thrown by task() ?
		 */
		public LoadingAsyncTask(ViewGroup progressBarTarget, boolean displayError) {
			this(true, displayError);
			this.progressBarTarget = progressBarTarget;
		}
		
		/**
		 * <p>Set exception during task execution</p>
		 */
		protected void setException(Exception e) {
			this.exception = e;
		}

		/**
		 * <p>Get exception that was set during task execution</p>
		 */
		public Exception getException() {
			return exception;
		}
		
		/**
		 * <p>Pre task execution (does nothing by default)</p>
		 */
		protected void preTask() {}
		
		/**
		 * <p>Task to execute</p>
		 * @return Object
		 * @throws Exception
		 */
		protected abstract Object task(Object[] params) throws Exception;
		
		/**
		 * <p>Post task execution (does nothing by default)</p>
		 */
		protected void postTask(Object result) {}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if (displayProgress) {
				if (progressBarTarget == null) {
					String loading = app != null && app.texts != null ? getText("LOADING", "Loading") : null;
					if (loading == null) loading = "Loading...";
					progressDialog = ProgressDialog.show(AppSessionCommon.this, "", loading);
				} else {
					progressBar = new ProgressBar(AppSessionCommon.this);
					progressBar.setIndeterminate(true);
					progressBar.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
					progressBarTarget.addView(progressBar);
				}
			}

			preTask();
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				return task(params);
			} catch (Exception e) {
				exception = e;
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		
			if (progressBarTarget != null) {
				progressBarTarget.removeView(progressBar);
				progressBar = null;
			}
			
			if (exception == null) {
				postTask(result);
			} else if (displayError) {
				error(exception);
			}
		}
	}
	
	public final int MENU_HOME = 99999;
	
	protected void goHome() {
	    Intent intent = new Intent(getApplicationContext(), AppSessionHome.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(intent);
	}
	
	protected void errorAndGoHome(Exception e) {
		okDialog("ERROR", e.getMessage(), android.R.drawable.ic_dialog_alert, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				goHome();
			}
		});
	}
	
	protected void goHomeOnLogo() {
		try {
			((ImageView)findViewById(R.id.simplicite_logo)).setOnClickListener(new ImageView.OnClickListener() {
				@Override
				public void onClick(View image) {
					goHome();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void addHomeMenuItem(Menu menu) {
		MenuItem item = menu.add(MENU_HOME, MENU_HOME, MENU_HOME, getText("HOME", "Home"));
		item.setIcon(R.drawable.menu_home);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getGroupId() == MENU_HOME && item.getItemId() == MENU_HOME) {
	    	goHome();
	    	return true;
	    } else
            return super.onOptionsItemSelected(item);
	}
}