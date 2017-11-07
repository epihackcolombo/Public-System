package org.cosmic.mobuzz.general.ui;

import java.io.IOException;
import java.util.Locale;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class UiProfile extends Fragment {

	private View fv;
	private Dialog progressDialog;
	ImageButton profile_logout, profile_edit, profile_logout2;
	TableRow tblr_profileLogoutEdit;
	LinearLayout ll_profileLogout;
	private final String TAG = UiProfile.class.getName();

	private String url_profile_logout;
	SharedPreferences pref;
	PopupDialogWidget pdw;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fv = inflater.inflate(R.layout.fragment_ui_profile, container, false);

		pref = getActivity().getSharedPreferences(GlobalVariables.MY_PREF, 0);

		url_profile_logout = getString(R.string.url_server_port) + getString(R.string.url_profile_logout);

		//Initialization of components 
		profile_logout = (ImageButton) fv.findViewById(R.id.ibut_profile_logout);
		profile_edit = (ImageButton) fv.findViewById(R.id.ibut_form_profile_edit);
		profile_logout2 = (ImageButton) fv.findViewById(R.id.ibut_profile_logout2);
		
		
		
		ll_profileLogout = (LinearLayout) fv.findViewById(R.id.ll_profileLogout);
		tblr_profileLogoutEdit = (TableRow) fv.findViewById(R.id.tblr_profileLogoutEdit);
		
		//Check whether login session is exist	
		if (pref.getBoolean("login", false)) { //already login		
			tblr_profileLogoutEdit.setVisibility(View.VISIBLE);
			ll_profileLogout.setVisibility(View.GONE);
		} 
		else
		{
			tblr_profileLogoutEdit.setVisibility(View.GONE);
			ll_profileLogout.setVisibility(View.VISIBLE);
		}

		but_edit_Click();
		but_logout_Click();
		but_logout_Click2();

		GlobalIO.writeLog(getActivity(), TAG, LogAction.SHOW, true);

		return fv;
	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalIO.writeLog(getActivity(), TAG, LogAction.RESUME, true);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
		GlobalIO.writeLog(getActivity(), TAG, LogAction.PAUSE, true);
	}

	private void but_edit_Click() {

		profile_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (isNetworkAvailable()) { //Check the network status
					Intent intent = new Intent(getActivity(), UiProfileEdit.class);
					startActivity(intent);
				} else {
					popdNoInternet();
				}

			}
		});

	}

	private void but_logout_Click() {

		profile_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				logout();
				
				/*//Note: Logout from server by uploading user activity log. Used only in research
				if (isNetworkAvailable()) { //Check the network status
					String ldata[] = { getJsongString(), url_profile_logout };
					new downloadData().execute(ldata);

				} else {
					popdNoInternet();
				}*/
			}
		});

	}
	
	private void but_logout_Click2() {

		profile_logout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				logout();
			}
		});

	}

	public String getJsongString() {

		pref = getActivity().getSharedPreferences(GlobalVariables.MY_PREF, 0);

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

			StringBuilder logContent = new StringBuilder(GlobalIO.getConfig(getActivity()));
			logContent.append(GlobalMethods.getTimestamp() + ",LOGOUT_REQUEST," + TAG + ";" + " [" + pref.getString("username", "") + "_logout_server]; ");
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

	// -------------------- Supporting functions -----------------------//
	//Get the profile data from the server
	
	private class downloadData extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new Dialog(getActivity());
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.msg_common_signout));
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(getActivity()));

			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return HttpUpload.uploadData(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			updateViews(result);
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

	}

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
							
							popdRequestFailedLogout(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));

						} else if (("error_db_connect").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdRequestFailedLogout(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));

						} else if (("authentication_required").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdReauth(getString(R.string.msg_response_reauthentication_title1), getString(R.string.msg_response_reauthentication_para1));
						} else if (("authentication_expired").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdReauth(getString(R.string.msg_response_reauthentication_title2), getString(R.string.msg_response_reauthentication_para2));
						} else {
							
							popdRequestFailedLogout(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}
						
					} else {
						
						popdRequestFailedLogout(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				} //-----------------------------------------
				else {
					
					
					if(result.contains("logout,ok")) //Success response
					{
						GlobalIO.setConfig(getActivity(), "[" + pref.getString("username", "") + "_logout_server_succeed]; ", false); //Refresh the log
						logout(); //server logout
					}
					else if(result.contains("logout,")) //Success response
					{
						GlobalIO.setConfig(getActivity(), "[" + pref.getString("username", "") + "_logout_client]; ", true);
						logout(); //client logout						
					}
					else
					{
						String[] data = result.split(","); //In order to support backward-compatibility with earlier version
						
						if (data[0].equals("logout")) {
							if (data[1].equals("ok")) {
	
								GlobalIO.setConfig(getActivity(), "[" + pref.getString("username", "") + "_logout_server_succeed]; ", false); //Refresh the log
								logout(); //server logout
							} else {
	
								GlobalIO.setConfig(getActivity(), "[" + pref.getString("username", "") + "_logout_client]; ", true);
								logout(); //client logout
							}
						} else { //Logout failed
							popdRequestFailedLogout(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}
					}
				}

			} else { //Other response
				popdRequestFailedLogout(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}
			
		} catch (Exception ex) { //Error response
			popdRequestFailedLogout(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));

			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}

	}
	
	//Exit the current view
	private void logout() { 

		Editor editor = pref.edit();

		editor.putBoolean("login", false);
		editor.putString("username", null);
		editor.putString("time_stamp", null);
		editor.commit();

		startActivity(new Intent(this.getActivity(), UiProfileLogin.class));
		getActivity().finish();
	}

	//Check the network status
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

		pdw = new PopupDialogWidget(getActivity(), R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(getActivity(), title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();
	}
	
	private void popdRequestFailedLogout(String title, String content) {
		
		pdw = new PopupDialogWidget(getActivity(), title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		pdw.okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw.dismiss();
				try
				{
					GlobalIO.setConfig(getActivity(), "[" + pref.getString("username", "") + "_logout_client]; ", true);
					logout(); //client logout	
				}
				catch(Exception ex1)
				{
					//nothing to do
				}
				startActivity(new Intent(getActivity(), UiProfileLogin.class));
				getActivity().finish();
			}
		});
		
	}

	private void popdReauth(String title, String content) {

		startActivity(new Intent(getActivity(), UiProfileLogin.class));
		getActivity().finish();

	}

}
