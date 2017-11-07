package org.cosmic.mobuzz.general.ui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.network.MakeJsonString;
import org.cosmic.mobuzz.general.ui.support.PopuplistAdapter;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UiProfileRegister extends Activity {

	private final Context context = UiProfileRegister.this;

	private EditText edt_email, edt_fullname, edt_contactno, edt_newpassword, edt_retypenewpassword, edt_username, edt_ward;
	private Button but_submit, but_gender, but_language, but_residence;
	private Typeface font;
	private Dialog progressDialog;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern;
	private Matcher matcher;

	private String[] residenceArray, languageArray, genderArray;

	private String name, email, contact, residence, language, gender, userName, password, rePassword, ward;
	private String TAG = UiProfileRegister.class.getName();
	private String url_profile_register;// = "http://pubbapp.comze.com/reg_users.php";
	MakeJsonString mjs;
	PopupDialogWidget pdw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_profile_register);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		url_profile_register = getString(R.string.url_server_port) + getString(R.string.url_profile_register);

		//Initialization of components 
		residenceArray = getResources().getStringArray(R.array.user_residence);
		languageArray = getResources().getStringArray(R.array.user_language);
		genderArray = getResources().getStringArray(R.array.user_gender);

		font = GlobalMethods.getTypeface(context);

		edt_email = (EditText) findViewById(R.id.edt_email);
		edt_fullname = (EditText) findViewById(R.id.edt_fullname);
		edt_contactno = (EditText) findViewById(R.id.edt_contactno);
		edt_ward = (EditText) findViewById(R.id.edt_ward);

		but_language = (Button) findViewById(R.id.but_language);
		but_residence = (Button) findViewById(R.id.but_residence);
		but_gender = (Button) findViewById(R.id.but_gender);

		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_newpassword = (EditText) findViewById(R.id.edt_newpassword);
		edt_retypenewpassword = (EditText) findViewById(R.id.edt_retypenewpassword);

		but_submit = (Button) findViewById(R.id.but_submit);

		edt_email.setTypeface(font);
		edt_fullname.setTypeface(font);
		edt_contactno.setTypeface(font);
		edt_ward.setTypeface(font);
		edt_username.setTypeface(font);
		edt_newpassword.setTypeface(font);
		edt_retypenewpassword.setTypeface(font);

		but_submit.setTypeface(font);
		but_gender.setTypeface(font);
		but_language.setTypeface(font);
		but_residence.setTypeface(font);

		LinearLayout myLayout = (LinearLayout) findViewById(R.id.lnrlayout);
		myLayout.requestFocus();

		but_gender_Click();
		but_submit_Click();
		but_language_Click();
		but_residence_Click();

		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_ui_profile_register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;

		switch (item.getItemId()) {

		case R.id.aboutCosmic:

			intent = new Intent(context, UiAboutMobuzzCosmic.class);
			startActivity(intent);
			return true;

		case R.id.aboutFaq:

			intent = new Intent(context, UiAboutMobuzzFaq.class);
			startActivity(intent);
			return true;

		case R.id.aboutMobuzz:

			intent = new Intent(context, UiAboutMobuzzInformation.class);
			startActivity(intent);
			return true;

		default:
			return false;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		GlobalIO.writeLog(context, TAG, LogAction.END, true);
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		GlobalMethods.cleanMemory();
	}
	
	private void but_residence_Click() {

		but_residence.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showDialogResidence();
			}
		});
	}

	private void but_language_Click() {

		but_language.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialogLanguage();
			}
		});
	}

	private void but_submit_Click() {

		but_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (validate()) {

					if (isNetworkAvailable()) {
						regTask();
					} else {
						popdNoInternet();
					}
				}
			}
		});
	}

	//Register task call
	private void regTask() {

		password = GlobalMethods.makeMD5(password);

		String[] userData = { name, email, contact, residence, language, gender, userName, password, ward };
		mjs = new MakeJsonString();
		String jString = mjs.forRegister(context, userData);
		String[] regData = { jString, url_profile_register };

		new RegisterTask().execute(regData);
	}

	//Validate user values
	protected boolean validate() {

		edt_email.setError(null);
		edt_fullname.setError(null);
		edt_contactno.setError(null);

		but_language.setError(null);
		but_residence.setError(null);
		edt_ward.setError(null);

		edt_username.setError(null);
		edt_newpassword.setError(null);
		edt_retypenewpassword.setError(null);

		but_gender.setError(null);
		but_language.setError(null);
		but_residence.setError(null);

		name = edt_fullname.getText().toString();
		email = edt_email.getText().toString();
		contact = edt_contactno.getText().toString();
		residence = but_residence.getText().toString();
		language = but_language.getText().toString();
		gender = but_gender.getText().toString();

		userName = edt_username.getText().toString();
		password = edt_newpassword.getText().toString();
		rePassword = edt_retypenewpassword.getText().toString();

		ward = edt_ward.getText().toString(); //Note: age field is used as ward

		//Validate parameters according to the UI order
		if (nameValidate(name, edt_fullname) && emailValidate(email, edt_email) && contactValidate(contact, edt_contactno)) {
			if (spinnerValidate(language, but_language) && wardtValidate(ward, edt_ward) && usernameValidate(userName, edt_username)) {
				if (passwordValidate(password, userName, edt_newpassword) && passwordsValidate(password, rePassword, edt_retypenewpassword)) {

					return true;
				}
			}
		}

		return false;
	}

	//Validate username
	private boolean usernameValidate(String value, EditText view) {

		if (TextUtils.isEmpty(value)) {
			view.setError(getString(R.string.error_field_required));
			view.requestFocus();
		} else if (value.length() < 6) {
			view.requestFocus();
			view.setError(getString(R.string.error_invalid_username));
		} 
		else if (!value.contains("/")) {
			view.requestFocus();
			view.setError(getString(R.string.error_invalid_username2));
		}
		else {
			return true;
		}
		return false;
	}

	//Validate password
	private boolean passwordValidate(String value, String username, EditText view) {

		if (TextUtils.isEmpty(value)) {
			view.setError(getString(R.string.error_field_required));
			view.requestFocus();
		} else if (value.length() < 6) {
			view.requestFocus();
			view.setError(getString(R.string.error_invalid_password));
		} 
		else if (value.equals(username)) {
			view.requestFocus();
			view.setError(getString(R.string.error_similar_userpass));
		}
		else {
			return true;
		}
		return false;
	}

	//Validate retype password
	private boolean passwordsValidate(String password1, String password2, EditText view) {
		if (password1.equals(password2)) {
			return true;
		} else {
			view.setError(getString(R.string.error_mismatch_password));
			view.requestFocus();
		}
		return false;
	}

	//Validate telephone number
	private boolean contactValidate(String value, EditText view) {
		if (value.length() == 10) {
			return true;
		} else {
			view.setError(getString(R.string.error_invalid_mobileno));
			view.requestFocus();
		}
		return false;
	}

	//Validate string ward list
	private boolean wardtValidate(String value, EditText view) {

		if (GlobalMethods.validateString(value)) {
			int intValue = 0;

			if (value.contains(",")) { //more than one ward allocation
				if (value.lastIndexOf(",") == value.length() - 1) {
					// it,s error
				} else {

					String[] strValues = value.split(",");

					for (String strValue : strValues) {
						intValue = 0;

						try {
							intValue = Integer.parseInt(strValue);

							if (intValue <= 0 || intValue > 47) {
								break;
							}
						} catch (Exception ex) {
							// nothing to do
						}
					}

					if (intValue <= 0 || intValue > 47) { //correct ward
					} else {
						return true;
					}
				}

			} else {
				try {
					intValue = Integer.parseInt(value);

					if (intValue <= 0 || intValue > 47) 
					{
					} else { //correct ward
						return true;
					}
				} catch (Exception ex) {
					// nothing to do
				}
			}

			view.setError(getString(R.string.error_invalid_ward));
			view.requestFocus();
			return false;

		} else {
			view.setError(getString(R.string.error_field_required));
			view.requestFocus();
			return false;
		}

	}

	//Validate spinners: residence, language or gender
	private boolean spinnerValidate(String value, Button but) {

		if (value == null) {
			but.requestFocus();
			but.setError(getString(R.string.error_field_required));
		} else if (value.equals("residence") || value.equals("preferred language") || value.equals("gender")) {
			but.requestFocus();
			but.setError(getString(R.string.error_field_required));
		} else {
			return true;
		}

		return false;
	}

	//Validate email address
	private boolean emailValidate(String value, EditText view) {

		if (TextUtils.isEmpty(value)) {
			view.setError(getString(R.string.error_field_required));
			view.requestFocus();
		} else {
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(value);
			if (matcher.matches()) {
				return true;
			} else {
				view.requestFocus();
				view.setError(getString(R.string.error_invalid_email));
			}
		}
		return false;
	}

	//Validate name
	private boolean nameValidate(String value, EditText view) {

		if (TextUtils.isEmpty(value)) {
			view.setError(getString(R.string.error_field_required));
			view.requestFocus();
		} else if (value.length() < 6) {
			view.requestFocus();
			view.setError(getString(R.string.error_incorrect_textlength));
		} else {
			return true;
		}
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalIO.writeLog(context, TAG, LogAction.RESUME, true);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
		GlobalIO.writeLog(context, TAG, LogAction.PAUSE, true);
	}

	private void but_gender_Click() {

		but_gender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialogGender();
			}
		});

	}


	// Spinner:Residence----------------------------------
	int result_residence = 0;

	private void showDialogResidence() {

		popdListResidence();
	}

	// Spinner:Gender-----------------------------
	int result_gender = 0;

	private void showDialogGender() {

		popdListGender();
	}

	// Spinner:Language-----------------------------
	int result_language = 0;

	private void showDialogLanguage() {

		popdListLanguage();
	}

	// -----------------------------------
	//Register server call
	private class RegisterTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			progressDialog = new Dialog(context);
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.msg_common_signup));
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));

			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return HttpUpload.uploadData(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			responseToResult(result);
		}

		public void responseToResult(String result) {

			try {
				if (GlobalMethods.validateString(result)) {

					String[] parts = result.split(",");

					if (parts[0].equals("ok")) { //Registration success
						popdRegister(getString(R.string.msg_registeruser_ok_title1), getString(R.string.msg_registeruser_ok_para1), true);
					} 
					else if (parts[0].contains("User name already exists")) {//Username already taken
						popdRegister(getString(R.string.msg_registeruser_no_title2), getString(R.string.msg_registeruser_no_para2), false);
					} 
					else if (parts[0].contains(GlobalVariables.CONN_EXCEPTION)) { //Connection exception
						popdRegister(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para1), false);
					} 
					else { //Other response
						popdRegister(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1), false);
					}
				} else { //Null or empty response
					popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
				}
			} catch (Exception ex) {
				popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
				Log.e(TAG, " " + ex.getMessage());
			}
		}
	}

	// -------------------- Supporting functions -----------------------//

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	// --------------------------------------------------------
	//Show pop-up messages	
	
	private void popdNoInternet() {

		pdw = new PopupDialogWidget(this, R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	private void popdRegister(String title, String content, boolean resetdata) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		if (resetdata) {

			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					startActivity(new Intent(context, UiProfileLogin.class));
					finish();
				}
			});
		}

	}

	//Language selection
	private void popdListLanguage() {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_list_dialog);
		dialog.setCanceledOnTouchOutside(false);
		// dialog.setCancelable(false);

		TextView txt_msg_title = (TextView) dialog.findViewById(R.id.txt_msg_title);
		txt_msg_title.setText(R.string.msg_select_language);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		final ArrayList<String> list = new ArrayList<String>();
		for (String language : languageArray) {
			list.add(language);
		}

		PopuplistAdapter adapter = new PopuplistAdapter(context, list);
		ListView lstw_msg_content = (ListView) dialog.findViewById(R.id.lstw_msg_content);
		lstw_msg_content.setAdapter(adapter);
		lstw_msg_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (GlobalMethods.validateString(item)) {
					but_language.setText(item);
					result_language = position;

					but_language.setTextColor(getResources().getColor(R.color.black));
				}

				dialog.dismiss();
			}
		});

		Button btn_msg_no = (Button) dialog.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText(getString(R.string.msg_common_cancel));
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	//Residence selection
	private void popdListResidence() {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_list_dialog);
		dialog.setCanceledOnTouchOutside(false);
		// dialog.setCancelable(false);

		TextView txt_msg_title = (TextView) dialog.findViewById(R.id.txt_msg_title);
		txt_msg_title.setText(R.string.msg_select_city);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		final ArrayList<String> list = new ArrayList<String>();
		for (String residence : residenceArray) {
			list.add(residence);
		}

		PopuplistAdapter adapter = new PopuplistAdapter(context, list);
		ListView lstw_msg_content = (ListView) dialog.findViewById(R.id.lstw_msg_content);
		lstw_msg_content.setAdapter(adapter);
		lstw_msg_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (GlobalMethods.validateString(item)) {
					but_residence.setText(item);
					result_residence = position;

					but_residence.setTextColor(getResources().getColor(R.color.black));
				}

				dialog.dismiss();
			}
		});

		Button btn_msg_no = (Button) dialog.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText(getString(R.string.msg_common_cancel));
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	//Gender selection
	private void popdListGender() {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_list_dialog);
		dialog.setCanceledOnTouchOutside(false);
		// dialog.setCancelable(false);

		TextView txt_msg_title = (TextView) dialog.findViewById(R.id.txt_msg_title);
		txt_msg_title.setText(R.string.msg_select_gender);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		final ArrayList<String> list = new ArrayList<String>();
		for (String residence : genderArray) {
			list.add(residence);
		}

		PopuplistAdapter adapter = new PopuplistAdapter(context, list);
		ListView lstw_msg_content = (ListView) dialog.findViewById(R.id.lstw_msg_content);
		lstw_msg_content.setAdapter(adapter);
		lstw_msg_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (GlobalMethods.validateString(item)) {
					but_gender.setText(item);
					result_gender = position;

					but_gender.setTextColor(getResources().getColor(R.color.black));
				}

				dialog.dismiss();
			}
		});

		Button btn_msg_no = (Button) dialog.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText(getString(R.string.msg_common_cancel));
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}

}
