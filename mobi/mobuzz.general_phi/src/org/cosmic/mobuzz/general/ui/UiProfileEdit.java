package org.cosmic.mobuzz.general.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.network.MakeJsonString;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.ui.support.PopuplistAdapter;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

public class UiProfileEdit extends Activity {

	private final Context context = UiProfileEdit.this;
	private EditText edt_email, edt_fullname, edt_contactno, edt_currentpassword, edt_newpassword, edt_retypenewpassword;
	private Button but_edit, but_submit, but_language, but_residence, but_logout, but_chpasswd;
	private Dialog progressDialog;
	private TextView txt_username, txt_ward;
	private Typeface font;
	private View fv;
	private boolean signout;
	private String[] languageArray, residenceArray;
	TableLayout tbl_password;

	String userName, name, email, cNomber, language, residence, oldPsswrd, newPasswrd, retypedPasswrd;

	private final String TAG = UiProfileEdit.class.getName();
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern;
	private Matcher matcher;
	//private String url_profile_logout;
	private String url_profile_edit;
	private String url_profile_update;
	MakeJsonString mjs;

	SharedPreferences pref;
	PopupDialogWidget pdw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //Hide the action bar, configuration is changed here to have the default theme.
		setContentView(R.layout.activity_ui_profile_edit);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		url_profile_edit = getString(R.string.url_server_port) + getString(R.string.url_profile_edit);
		url_profile_update = getString(R.string.url_server_port) + getString(R.string.url_profile_update);
		pref = this.getSharedPreferences(GlobalVariables.MY_PREF, 0);

		//Initialization of components 
		fv = this.findViewById(android.R.id.content);

		font = GlobalMethods.getTypeface(context);
		edt_email = (EditText) fv.findViewById(R.id.edt_email);
		edt_fullname = (EditText) fv.findViewById(R.id.edt_fullname);
		edt_contactno = (EditText) fv.findViewById(R.id.edt_contactno);
		edt_currentpassword = (EditText) fv.findViewById(R.id.edt_currentpassword);
		edt_newpassword = (EditText) fv.findViewById(R.id.edt_newpassword);
		edt_retypenewpassword = (EditText) fv.findViewById(R.id.edt_retypenewpassword);
		but_edit = (Button) fv.findViewById(R.id.but_edit);
		but_submit = (Button) fv.findViewById(R.id.but_submit);
		but_language = (Button) fv.findViewById(R.id.but_language);
		but_residence = (Button) fv.findViewById(R.id.but_residence);
		but_logout = (Button) fv.findViewById(R.id.but_logout);
		but_chpasswd = (Button) fv.findViewById(R.id.but_chpasswd);

		txt_username = (TextView) fv.findViewById(R.id.txt_username);
		txt_ward = (TextView) fv.findViewById(R.id.txt_ward);

		languageArray = getResources().getStringArray(R.array.user_language);
		residenceArray = getResources().getStringArray(R.array.user_residence);

		tbl_password = (TableLayout) fv.findViewById(R.id.tbl_password);

		//Set the font
		edt_email.setTypeface(font);
		edt_fullname.setTypeface(font);
		edt_contactno.setTypeface(font);
		edt_currentpassword.setTypeface(font);
		edt_newpassword.setTypeface(font);
		edt_retypenewpassword.setTypeface(font);

		but_edit.setTypeface(font);
		but_submit.setTypeface(font);
		but_language.setTypeface(font);
		but_residence.setTypeface(font);
		but_logout.setTypeface(font);
		but_chpasswd.setTypeface(font);

		txt_username.setTypeface(font);
		txt_ward.setTypeface(font);

		enableDisableView(fv, false);
		tbl_password.setVisibility(View.GONE);
		but_edit.setEnabled(false);

		but_edit_Click();
		but_submit_Click();
		but_logout_Click();
		but_residence_Click();
		but_language_Click();
		but_chpasswd_Click();

		getProfieData();

		txt_username.setText(pref.getString("username", ""));

		GlobalIO.writeLog(context, TAG, LogAction.START, true);
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

	private void but_language_Click() {

		but_language.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialogLanguage();
			}
		});

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

	private void but_logout_Click() {

		but_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				logout(); //Only client logout.

			}
		});
	}

	
	private void but_chpasswd_Click()
	{
		but_chpasswd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				edt_newpassword.setVisibility(View.VISIBLE);
				edt_retypenewpassword.setVisibility(View.VISIBLE);
				
				but_chpasswd.setVisibility(View.GONE);
			}
		});
	}
	
	//Create request string
	public String getJsongString() {

		JSONObject obj = new JSONObject();

		try {
			obj.put("user", pref.getString("username", ""));
			obj.put("time_stamp", pref.getString("time_stamp", ""));
			obj.put("uudid", pref.getString("uudid", ""));
			//obj.put("log_data", getLogData()); //This data is for research purpose only. Disable for now to study already collected data, and then decide whether collecting more or not.
			obj.put("log_data", "");

		} catch (JSONException ex) {
			Log.e(TAG, ex.getMessage());
		}

		return obj.toString();

	}

	//Encode the activity log-data
	private String getLogData() {
		try {

			StringBuilder logContent = new StringBuilder(GlobalIO.getConfig(context));
			logContent.append(GlobalMethods.getTimestamp() + ",LOGOUT_REQUEST," + TAG + ";" + "[" + pref.getString("username", "") + "_logout_server]; ");
			String finalLogContentString = logContent.toString();

			if (GlobalMethods.validateString(finalLogContentString)) {
				byte[] data = finalLogContentString.getBytes("UTF-8");
				String encodedLog = Base64.encodeToString(data, Base64.DEFAULT);
				return encodedLog;
			} else {
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void getProfieData() {

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("user", pref.getString("username", "defv"));
			obj.putOpt("time_stamp", pref.getString("time_stamp", ""));
			obj.putOpt("uudid", pref.getString("uudid", ""));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String jString = obj.toString();
		String[] data = { jString, url_profile_edit };

		if (isNetworkAvailable()) { //Check the network status
			new downloadData().execute(data);
		} else {
			popdNoInternet();
		}
	}

	private void but_edit_Click() {

		but_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Update UIs
				enableDisableView(fv, true);
				tbl_password.setVisibility(View.VISIBLE);
				but_edit.setVisibility(View.GONE);
				but_submit.setVisibility(View.VISIBLE);
				
				but_logout.setVisibility(View.GONE);
				but_chpasswd.setVisibility(View.VISIBLE);

				but_residence.setTextColor(getResources().getColor(R.color.black));
				but_language.setTextColor(getResources().getColor(R.color.black));
			}
		});
	}

	private void but_submit_Click() {

		but_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (v.getId() == but_submit.getId()) {
					getNewValues();
				}
			}
		});

	}

	//Apply data to UI components
	protected void getNewValues() {

		userName = pref.getString("username", "");

		edt_email.setError(null);
		edt_fullname.setError(null);
		edt_contactno.setError(null);

		but_language.setError(null);
		but_residence.setError(null);

		edt_currentpassword.setError(null);
		edt_newpassword.setError(null);
		edt_retypenewpassword.setError(null);

		name = edt_fullname.getText().toString();
		email = edt_email.getText().toString();
		cNomber = edt_contactno.getText().toString();

		language = but_language.getText().toString();
		residence = but_residence.getText().toString();

		oldPsswrd = edt_currentpassword.getText().toString();
		newPasswrd = edt_newpassword.getText().toString();
		retypedPasswrd = edt_retypenewpassword.getText().toString();

		if (validate()) {
			updateProfile();
		} else {
			Log.w(TAG, "Parameter validation failed");
		}

	}

	//Collect edited data
	private void updateProfile() {

		if (GlobalMethods.validateString(oldPsswrd)) {
			oldPsswrd = GlobalMethods.makeMD5(oldPsswrd);
		}

		if (GlobalMethods.validateString(newPasswrd)) {
			newPasswrd = GlobalMethods.makeMD5(newPasswrd);
		}

		String time_stamp = pref.getString("time_stamp", "");
		String uudid = pref.getString("uudid", "");

		String userdata[] = { userName, name, email, cNomber, language, "cmc", oldPsswrd, newPasswrd, time_stamp, uudid };

		mjs = new MakeJsonString();
		String jString = mjs.forProfileUpdate(userdata);

		String editdata[] = { jString, url_profile_update };

		if (isNetworkAvailable()) { //Check the network status
			new downloadData().execute(editdata);
		} else {
			popdNoInternet();
		}
	}

	//Validate new values
	private boolean validate() {
		if (nameValidate(name, edt_fullname) && emailValidate(email, edt_email) && contactValidate(cNomber, edt_contactno)) {
			if (spinnerValidate(language, but_language)) {
				
				if (passwordValidate(oldPsswrd, edt_currentpassword) )
				{
					if(GlobalMethods.validateString(newPasswrd)){
						return passwordValidate(newPasswrd, retypedPasswrd, edt_newpassword) ;
					}
					else
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	//Update UI groups
	private void enableDisableView(View view, boolean enabled) {
		view.setEnabled(enabled);

		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;

			for (int idx = 0; idx < group.getChildCount(); idx++) {
				enableDisableView(group.getChildAt(idx), enabled);
			}
		}

		but_edit.setEnabled(true);
		but_submit.setEnabled(true);
		but_logout.setEnabled(true);
	}
	
	//Update UIs based on server response
	public void updateViews(String result) {

		try {
			if (GlobalMethods.validateString(result)) {

				//validation block ------------------------
				//error_net_connection - no route to host, can't find an active connection
				//error_net_other - network time-out or other connector exceptions
				//error_db_params - inappropriate parameters with the request
				//error_db_connect - database connection issue at server side
				//authentication_required - user need to be authenticate
				//authentication_expired - user session has expired
				
				if (result.contains("{'status':")) { //In order to support backward-compatibility

					Gson gson = new Gson();
					ProfileLoginPojo plp = gson.fromJson(result, ProfileLoginPojo.class);

					if (GlobalMethods.validateString(plp.getStatus())) {

						if (("error_net_connection").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para1));
						}
						else if (("error_net_other").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
						
							popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para2));
						}
						else if (("error_db_params").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdRequestFailed(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));
						} else if (("error_db_connect").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdRequestFailed(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));
						} else if (("authentication_required").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdReauth(getString(R.string.msg_response_reauthentication_title1), getString(R.string.msg_response_reauthentication_para1));
						} else if (("authentication_expired").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdReauth(getString(R.string.msg_response_reauthentication_title2), getString(R.string.msg_response_reauthentication_para2));
						} else {
							
							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}
						
					} else { //Null or empty response
						
						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				} //-----------------------------------------
				else {

					String[] data = result.split(","); //Server response

					if (data[0].equals("profiledata")) { //Get server profile data
						edt_email.setText(data[2].trim());
						edt_fullname.setText(data[1].trim());
						edt_contactno.setText(data[3].trim());
						but_language.setText(data[6].trim());

						but_language.setTextColor(getResources().getColor(R.color.theme_ash));
						
						but_edit.setTextColor( getResources().getColor(R.color.mobuzz_gray) );
						but_edit.setEnabled(true);

					} else if (data[0].equals("edited")) { //Server was updated with new data

						if (data[1].equals("success")) {
							enableDisableView(fv, false);
							tbl_password.setVisibility(View.GONE);
							
							but_edit.setVisibility(View.VISIBLE);
							but_submit.setVisibility(View.GONE);
							but_logout.setVisibility(View.VISIBLE);
							but_chpasswd.setVisibility(View.GONE);

							edt_currentpassword.setText(null);
							edt_newpassword.setText(null);
							edt_retypenewpassword.setText(null);

							but_language.setTextColor(getResources().getColor(R.color.theme_ash));

							Editor editor = pref.edit();
							editor.putString("language", language);
							editor.commit();

						} else if ((data[1].trim()).contains("Invalid Password")) { //Incorrect password
							popdLogin(getString(R.string.msg_loginuser_error_title1), getString(R.string.msg_loginuser_error_para2), false);
							edt_currentpassword.setText(null);
						}

					} else if (data[0].equals("logout")) { //Logout response

						if (data[0].equals("logout")) {
							if (data[1].equals("ok")) {

								GlobalIO.setConfig(context, "[" + pref.getString("username", "") + "_logout_server_succeed]; ", false); //Refresh the log
								logout(); //server logout
							} else {
								GlobalIO.setConfig(context, "[" + pref.getString("username", "") + "_logout_client]; ", true);
								logout(); //client logout
							}
						}

					} else {
						//Unknown response
						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				}

			} else { //Null or empty response
				Log.w(TAG, "Receive null response");
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} catch (Exception ex) { //Exception response
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			Log.e(TAG, " " + ex.getMessage());
		}

	}

	//Clear client variables
	private void logout() {

		Editor editor = pref.edit();

		editor.putBoolean("login", false);
		editor.putString("username", null);
		editor.putString("time_stamp", null);
		editor.commit();

		startActivity(new Intent(context, UiProfileLogin.class));
		finish();
	}

	// Spinner:Residence----------------------------------
	int result_residence = 0;

	private void showDialogResidence() {
		popdListrResidence();
	}

	int result_language = 0;

	private void showDialogLanguage() {
		popdListLanguage();
	}

	//Validate user password
	private boolean passwordValidate(String value, EditText view) {

		if (TextUtils.isEmpty(value)) {
			view.setError(getString(R.string.error_field_required));
			view.requestFocus();
		} else if (value.length() < 6) {
			view.setError(getString(R.string.error_invalid_password));
		} else {
			return true;
		}
		return false;
	}
	
	//Validate retyped password
	private boolean passwordValidate(String password1, String password2, EditText view) {
		

		if (password1.equals(password2)  ) 
		{
			 return passwordValidate(password1, view);
		} 
		else 
		{
			view.setError(getString(R.string.error_mismatch_password));
			view.requestFocus();
		}
		return false;
	}

	//Validate mobile number
	private boolean contactValidate(String value, EditText view) {
		if (value.length() == 10) {
			return true;
		} else {
			view.setError(getString(R.string.error_invalid_mobileno));
			view.requestFocus();
		}
		return false;
	}

	//Validate mobile number
	private boolean spinnerValidate(String value, Button but) {

		if (value == null) {
			but.setError(getString(R.string.error_field_required));
		} else if (value.equals("residence") || value.equals("preferred language") || value.equals("gender")) {
			but.setError(getString(R.string.error_field_required));
		} else {
			return true;
		}

		return false;
	}

	//Validate email
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
			view.setError(getString(R.string.error_incorrect_textlength));
		} else {
			return true;
		}
		return false;
	}
	


	// -------------------- Supporting functions -----------------------//

	//Check the network status
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	//Shared server requests
	private class downloadData extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new Dialog(context);
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);

			if (signout) {
				txt_msg_title.setText(getString(R.string.msg_common_signout));
			} else {
				txt_msg_title.setText(getString(R.string.msg_common_loading));
			}

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
			updateViews(result);

		}

	}


	// --------------------------------------------------------
	//Show pop-up messages		
	
	private void popdNoInternet() {

		pdw = new PopupDialogWidget(this, R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	private void popdListLanguage() {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_list_dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

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

	private void popdListrResidence() {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_list_dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

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

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}

	private void popdLogin(String title, String content, boolean resetdata) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		if (resetdata) {

			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					startActivity(new Intent(context, UiMain.class));
					finish();
				}
			});

		}

	}

	private void popdReauth(String title, String content) {

		if (signout) {

			startActivity(new Intent(context, UiProfileLogin.class));
			finish();

		} else {
			pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
			pdw.show();

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

}
