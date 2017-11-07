package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UiProfileRecover extends Activity {

	private final Context context = UiProfileRecover.this;
	private final String TAG = UiProfileRecover.class.getName();
	private EditText usernameET, emailET;
	private Button resetBT;
	private Dialog progressDialog;
	private Typeface font;
	private String url_profile_recover;
	PopupDialogWidget pdw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_profile_recover);
		url_profile_recover =  getString(R.string.url_server_port)+getString(R.string.url_profile_recover);

		//Initialization of components 
		usernameET = (EditText) findViewById(R.id.usernameRET);
		emailET = (EditText) findViewById(R.id.emailRET);
		resetBT = (Button) findViewById(R.id.resetButton);

		font = GlobalMethods.getTypeface(context);
		usernameET.setTypeface(font);
		emailET.setTypeface(font);
		resetBT.setTypeface(font);

		resetBT_Click();
		
		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		GlobalIO.writeLog(context, TAG, LogAction.END, true);
		GlobalMethods.cleanMemory();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onResume(){
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
	
	private void resetBT_Click() {
		resetBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isNetworkAvailable()) {
					doReset();
				} else {
					popdNoInternet();
				}
			}
		});

	}

	protected void doReset() {

		if (validate()) {
			String jString = makeJstring();
			String data[] = { jString, url_profile_recover };

			new ResetTask().execute(data);
		}

	}

	//Validate user values
	private boolean validate() {
		usernameET.setError(null);
		emailET.setError(null);

		String userName = usernameET.getText().toString();
		String email = emailET.getText().toString();

		if (TextUtils.isEmpty(userName)) {
			usernameET.setError(getString(R.string.error_field_required));
			usernameET.requestFocus();
		} else if (userName.length() < 6) {
			usernameET.setError(getString(R.string.error_invalid_username));
			usernameET.requestFocus();
		} else if (TextUtils.isEmpty(email)) {
			emailET.setError(getString(R.string.error_field_required));
			emailET.requestFocus();
		} else {
			return true;
		}

		return false;
	}

	private void clear() {
		usernameET.setText(null);
		emailET.setText(null);
	}

	//Make json string
	private String makeJstring() {
		JSONObject obj = new JSONObject();

		String userName = usernameET.getText().toString();
		String hUserName = GlobalMethods.makeMD5(userName); //Create the MD5 for the username to use as password
		String email = emailET.getText().toString();

		try {
			obj.put("user", userName);
			obj.put("h_user", hUserName);
			obj.put("email", email);
		} catch (JSONException ex) {
			Log.e(TAG, " " + ex.getMessage());
			return null;
		}

		return obj.toString();
	}

	//Send details to the server
	private class ResetTask extends AsyncTask<String, Void, String> {

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
			txt_msg_title.setText(getString(R.string.msg_common_data_get));
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));

			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpUpload.uploadData(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			doOnResult(result);
		}

	}

	//Reset response from server
	public void doOnResult(String result) {

		try {
			if (GlobalMethods.validateString(result)) {
				String[] data = result.split(",");

				if (data[0].equals("ok")) {
					resetPassword(getString(R.string.msg_resetpassword_ok_title1), getString(R.string.msg_resetpassword_ok_para1), false);
				} else if (data[0].equals("no")) {
					resetPassword(getString(R.string.msg_resetpassword_error_title1), getString(R.string.msg_resetpassword_error_para1), true);
				} else {
					resetPassword(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1), true);
				}
			}
			else
			{
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} catch (Exception ex) {
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			Log.e(TAG, " " + ex.getMessage());
		}
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
	//Show pop-up error messages	
	
	private void popdNoInternet() {
		
		pdw = new PopupDialogWidget(this, R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	private void resetPassword(String title, String content, boolean resetdata) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();
		
		if (resetdata) {
			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					clear();
				}
			});
		}else
		{
			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
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
