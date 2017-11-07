package org.cosmic.mobuzz.general;

import java.util.Locale;

import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends Activity {

	Context context = MainActivity.this;

	private Dialog progressDialog;
	private SharedPreferences pref;
	private final String TAG = MainActivity.class.getName();	
	private String url_activation_code;
	
	Button but_activate;
	EditText edt_code;
	TextView txt_activate_msg;
	Typeface font;

	PopupDialogWidget pdw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		url_activation_code = getString(R.string.url_server_port) + getString(R.string.url_activation_code);
		
		//Initialization of components 
		font = GlobalMethods.getTypeface(context);
		
		ViewGroup view = (ViewGroup)getWindow().getDecorView();
		GlobalMethods.applyFontRecursively(view, font);
		

		but_activate = (Button) findViewById(R.id.but_activate);
		edt_code = (EditText) findViewById(R.id.edt_code);
		txt_activate_msg = (TextView) findViewById(R.id.txt_activate_msg);

		font = GlobalMethods.getTypeface(context);
		txt_activate_msg.setTypeface(font);
		edt_code.setTypeface(font);
		but_activate.setTypeface(font);

		// --------------------- Activate ---------------------//

		// Activate the app, temparary lock, hence hardcord value
		pref = getSharedPreferences("ActivateApp", 0);

		if (pref.getBoolean("activatedapp", false)) {

			Intent intent = new Intent(context, SplashActivity.class);
			startActivity(intent);
		}
		else {

			but_activate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String str_code = edt_code.getText().toString().trim();
					
					if(GlobalMethods.validateString(str_code))
					{
						if (isNetworkAvailable()) { // Check the network status
							// Activate the app, unlock with the server
							activationcode(str_code);
						} else {
							popdNoInternet();
						}

					}

				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onBackPressed() {
		//quit
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	//Jason for unlock the app
	private void activationcode(String key)
	{
		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("key", key);

			String jString = obj.toString();
			String[] data = { jString, url_activation_code };

			new activateApp().execute(data);

		} catch (JSONException ex) {
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}
	}
	
	// --------------------------------------------------------

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private void popdNoInternet() {

		pdw = new PopupDialogWidget(this, R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	
	@Override
	public void onPause() {
		super.onPause();

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
	}
	
	
	//---------------------- Network supporting function ----------------------//
	
	//Getting the unlock information
	private class activateApp extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			progressDialog = new Dialog(context);
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.msg_downloading_para1));
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

			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			responseToResult(result);
		}
	}

	public void responseToResult(String result) {

		try {
			if (GlobalMethods.validateString(result)) {

				//validation block ------------------------
				//error_net_connection - no route to host, can't find an active connection
				//error_net_other - network time-out or other connector exceptions
				//error_db_params - inappropriate parameters with the request
				//error_db_connect - database connection issue at server side
				//authentication_required - user need to be authenticate
				//authentication_expired - user session has expired
				
				if (result.contains("{'status':")) { // in order to support previous and new communication

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
						} else {
							
							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}

					} else {
						
						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				} //-----------------------------------------
				else {

					if(result.contains("action#ok")) //In order for backward-compatibility with older version of the app
					{
						Intent intent = new Intent(context, SplashActivity.class);
						startActivity(intent);

						Editor editor = pref.edit();
						editor.putBoolean("activatedapp", true);
						editor.commit();

						finish();
					}
					else
					{
						popdRequestFailed(getString(R.string.msg_common_key_no_title1), getString(R.string.msg_common_key_no_para1));
					}
				}

			} else {
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} catch (Exception ex) {
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}

	}

	//Show pop-up messages
	private void progress(String message) {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_progressbar);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		TextView txt_msg_title = (TextView) dialog.findViewById(R.id.txt_msg_content);
		txt_msg_title.setText(message);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));

		dialog.show();

	}
	
}
