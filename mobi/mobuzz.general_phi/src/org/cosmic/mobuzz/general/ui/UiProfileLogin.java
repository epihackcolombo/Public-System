package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.network.MakeJsonString;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;

import android.annotation.SuppressLint;
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
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class UiProfileLogin extends Activity {

	private final Context context = UiProfileLogin.this;
	private String TAG = UiProfileLogin.class.getName();
	private EditText usernameET, passwordET;
	private Dialog progressDialog;
	private Button signinButton;
	private TextView registerTextView, forgotpwordTextView;
	private String url_profile_login, time_stamp="";
	MakeJsonString mjs;

	Typeface font;
	PopupDialogWidget pdw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_profile_login);

		//Initialization of components 
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		url_profile_login = getString(R.string.url_server_port) + getString(R.string.url_profile_login);

		font = GlobalMethods.getTypeface(context);

		usernameET = (EditText) findViewById(R.id.usernameEditText);
		passwordET = (EditText) findViewById(R.id.passwordEditText);
		signinButton = (Button) findViewById(R.id.signinButton);
		registerTextView = (TextView) findViewById(R.id.registerTextView);
		forgotpwordTextView = (TextView) findViewById(R.id.forgotpwordTextView);

		usernameET.setTypeface(font);
		passwordET.setTypeface(font);
		signinButton.setTypeface(font);
		registerTextView.setTypeface(font);
		forgotpwordTextView.setTypeface(font);

		registerTextView_Click();
		forgotpwordTextView_Click();
		signinButton_Click();

		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}

	@Override
	public void onBackPressed() {
		popdExit();
		GlobalMethods.cleanMemory();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		
		if (pdw!=null && pdw.isShowing()) {
			pdw.dismiss();
		}
		GlobalIO.writeLog(context, TAG, LogAction.PAUSE, true);
	}

	// ------- Start: Handle Click events -----------
	private void signinButton_Click() {
		signinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void forgotpwordTextView_Click() {
		forgotpwordTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isNetworkAvailable()) { //Check the network status
					startActivity(new Intent(context, UiProfileRecover.class));
				} else {
					popdNoInternet();
				}

			}
		});
	}

	private void registerTextView_Click() {
		registerTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isNetworkAvailable()) {
					startActivity(new Intent(context, UiProfileRegister.class));
				} else {
					popdNoInternet();
				}

			}
		});
	}

	//Validate parameters and login
	public void login() {
		usernameET.setError(null);
		passwordET.setError(null);

		String userName = usernameET.getText().toString();
		String password = passwordET.getText().toString();

		if (TextUtils.isEmpty(userName)) {
			usernameET.setError(getString(R.string.error_field_required));
			usernameET.requestFocus();
		} else if (userName.length() < 6) {
			usernameET.setError(getString(R.string.error_invalid_username));
			usernameET.requestFocus();
		} else if (TextUtils.isEmpty(password)) {
			passwordET.setError(getString(R.string.error_field_required));
			passwordET.requestFocus();
		} else if (password.length() < 6) {
			passwordET.setError(getString(R.string.error_incorrect_password));
			passwordET.requestFocus();
		} else {
			attemptLogin();
		}
	}

	private void attemptLogin() { //Login call if network connection is available

		if (isNetworkAvailable()) {
			loginTask();
		} else {
			popdNoInternet();
		}

	}

	//Make json string for the login, and request server
	private void loginTask() {

		String password = passwordET.getText().toString();
		String encryptPassword = GlobalMethods.makeMD5(password);
		
		time_stamp =  GlobalMethods.getTimestamp();

		mjs = new MakeJsonString();
		String jString = mjs.forLogin(context, usernameET.getText().toString(), encryptPassword, time_stamp);
		String[] loginData = { jString, url_profile_login };

		new LoginTask().execute(loginData);
	}

	//Requesting server
	private class LoginTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new Dialog(context);
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.login_progress_signing_in));
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));

			progressDialog.show();
			
		}

		@Override
		protected String doInBackground(String... loginData) {

			return HttpUpload.uploadData(loginData[0], loginData[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			responseToResult(result);
		}

	}

	public void responseToResult(String result) {

		try {
			if (GlobalMethods.validateString(result)) {
				
				try {
					
					//validation block ------------------------
					//error_net_connection - no route to host, can't find an active connection
					//error_net_other - network time-out or other connector exceptions
					//error_db_params - inappropriate parameters with the request
					//error_db_connect - database connection issue at server side
					//authentication_required - user need to be authenticate
					//authentication_expired - user session has expired

					Gson gson = new Gson();
					ProfileLoginPojo plp = gson.fromJson(result, ProfileLoginPojo.class);
					
					if(GlobalMethods.validateString(plp.getStatus()))
					{
						
						if(("ok").equals(plp.getStatus().toLowerCase()))
						{
							GlobalIO.setConfig(context, "[" + usernameET.getText().toString() + "_login]; ", true); //Refresh the log
							makeLogin(plp.getLanguage());
							
							//For language translation
							String pref_lng = plp.getLanguage(); //Setup the app's language based on profile language
							
							if(("sinhala").equalsIgnoreCase(pref_lng))
							{
								GlobalMethods.changeLang(this, "sn");
							}
							else if(("tamil").equalsIgnoreCase(pref_lng))
							{
								GlobalMethods.changeLang(this, "tm");
							}
							else
							{
								GlobalMethods.changeLang(this, "en");
							}

							startActivity(new Intent(context, UiMain.class));
							finish();
						}
						else if(("error_db_connect").equals(plp.getStatus().toLowerCase()))
						{
							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}
						else if(("error_db_update").equals(plp.getStatus().toLowerCase()))
						{
							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}
						else if(("authentication_failed").equals(plp.getStatus().toLowerCase()))
						{
							popdLogin(getString(R.string.msg_loginuser_error_title1), getString(R.string.msg_loginuser_error_para1), false);
							passwordET.setText(null);
						}
						else if(("authentication_blocked").equals(plp.getStatus().toLowerCase()))
						{
							startActivity(new Intent(context, UiProfileBlock.class));
							finish();
						}
						else if(("ward_required").equals(plp.getStatus().toLowerCase()))
						{
							popdLogin(getString(R.string.msg_loginuser_error_title1), getString(R.string.msg_loginuser_error_para3), false);
							usernameET.setText(null);
							passwordET.setText(null);
						}						
						else
						{
							popdLogin(getString(R.string.msg_loginuser_error_title1), getString(R.string.msg_loginuser_error_para1), false);
							passwordET.setText(null);
						}
						
					} //Null or empty response
					else
					{
						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				}
				catch(Exception ex)
				{
					popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
				}
				
			} else { //Null or empty response
				
				popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_title1));
			}

		} catch (Exception ex) {
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}

	}

	//Set client parameters
	@SuppressLint("SimpleDateFormat")
	private void makeLogin(String language) {

		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		Editor editor = pref.edit();

		TelephonyManager tMngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		editor.putBoolean("login", true);
		editor.putString("username", usernameET.getText().toString());
		editor.putString("time_stamp", time_stamp);
		editor.putString("uudid", tMngr.getDeviceId());
		editor.putString("language", language);
		editor.commit();

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

	// --------------------------------------------------------
	//Show pop-up messages	
	
	private void popdNoInternet() {

		pdw = new PopupDialogWidget(this, R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	private void popdExit() {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_dialog);
		dialog.setCanceledOnTouchOutside(false);

		TableRow tblr_msg_title = (TableRow) dialog.findViewById(R.id.tblr_msg_title);
		tblr_msg_title.setVisibility(View.GONE);

		TextView txt_msg_content = (TextView) dialog.findViewById(R.id.txt_msg_content);
		txt_msg_content.setText(getString(R.string.msg_exit_para1));
		txt_msg_content.setTypeface(GlobalMethods.getTypeface(context));

		Button btn_msg_ok = (Button) dialog.findViewById(R.id.btn_msg_ok);
		btn_msg_ok.setText(getString(R.string.msg_common_stayson));
		btn_msg_ok.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		Button btn_msg_no = (Button) dialog.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText(getString(R.string.msg_common_exit));
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		btn_msg_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GlobalIO.writeLog(context, TAG, LogAction.EXIT, true);
				moveTaskToBack(true);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		});

		dialog.show();
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

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}

}
