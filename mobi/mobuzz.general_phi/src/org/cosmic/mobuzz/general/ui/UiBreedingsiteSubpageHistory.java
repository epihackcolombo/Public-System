package org.cosmic.mobuzz.general.ui;

import java.util.ArrayList;
import java.util.Locale;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.pojo.HistoryPojo;
import org.cosmic.mobuzz.general.pojo.ImagePojo;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.ui.support.BreedingsiteHistoryAdapter;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class UiBreedingsiteSubpageHistory extends Fragment {

	private Dialog progressDialog;
	public ArrayList<HistoryPojo> CustomListViewValuesArr = new ArrayList<HistoryPojo>();
	final String TAG = UiBreedingsiteSubpageHistory.class.getName();
	private String url_report_history;
	private Typeface font;

	HistoryPojo[] historyPojo;
	ListView list;
	BreedingsiteHistoryAdapter adapter;
	Activity activity = null;
	PopupDialogWidget pdw;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ui_breedingsite_subpage_history, container, false);

		//Initialization of components
		this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		url_report_history = getString(R.string.url_server_port) + getString(R.string.url_report_history);
		activity = this.getActivity();

		font = GlobalMethods.getTypeface(activity);
		adapter = new BreedingsiteHistoryAdapter(activity);

		list = (ListView) view.findViewById(R.id.list);
		list.setAdapter(adapter);

		return view;
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

	@Override
	public void setUserVisibleHint(boolean isFragmentVisible) {
		super.setUserVisibleHint(isFragmentVisible);

		if (isFragmentVisible) {
			GlobalIO.writeLog(getActivity(), TAG, LogAction.SHOW, true);
			getHistory(); //Refresh history when the page is showing 
		}
	}

	public BreedingsiteHistoryAdapter getAdapter() {
		return adapter;
	}

	//Get user reported data-summary from the server
	private void getHistory() {

		SharedPreferences pref = this.getActivity().getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String username = pref.getString("username", "defv"); //Insert a dummy-name to avoid passing null value. 4-character username is not possible in registration.
		String time_stamp = pref.getString("time_stamp", "");
		String uudid = pref.getString("uudid", "");

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("user", username);
			obj.putOpt("time_stamp", time_stamp);
			obj.putOpt("uudid", uudid);

			String jString = obj.toString();
			String[] data = { jString, url_report_history };

			if (isNetworkAvailable()) {
				new downloadData().execute(data);
			} else {
				popdNoInternet();
			}

		} catch (JSONException ex) {
			Log.e(TAG, " " + ex.getMessage());
		}
	}

	// -------------------- Supporting functions -----------------------//

	private class downloadData extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			//Custom progress dialog
			progressDialog = new Dialog(getActivity());
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.msg_common_loading));
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


			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			responseToResult(result);

		}
	}

	public void responseToResult(String result) {

		try {

			if (GlobalMethods.validateString(result)) {

				// validation block ------------------------
				//error_net_connection - no route to host, can't find an active connection
				//error_net_other - network time-out or other connector exceptions
				//error_db_params - inappropriate parameters with the request
				//error_db_connect - database connection issue at server side
				//authentication_required - user need to be authenticate
				//authentication_expired - user session has expired	
				
				if (result.contains("{'status':")) { //In order for backward-compatibility

					Gson gson = new Gson();
					ProfileLoginPojo plp = gson.fromJson(result, ProfileLoginPojo.class);

					if (GlobalMethods.validateString(plp.getStatus())) {

						if (("error_net_connection").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para1));
						} else if (("error_net_other").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para2));
						} else if (("error_db_params").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

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

					} else {
						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				} // -----------------------------------------
				else {
					adapter.notifyDataSetChanged(); //Update the adapter 
					decodeResult(result);
					list.invalidateViews();
				}

			} else {
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} 
		catch(JsonParseException ex) //Json exception
		{
			popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
		}
		catch (Exception ex1) //Error in response
		{
			popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
		}

	}

	//Examine result data and set values to the adapter
	public void decodeResult(String result) {

		try {

			if (GlobalMethods.validateString(result)) {

				if ((result.trim()).indexOf("[") < 1) {
					Gson gson = new Gson();
					historyPojo = gson.fromJson(result, HistoryPojo[].class);

					if (historyPojo != null && historyPojo.length > 0) {
						adapter.clear();
						for (int i = historyPojo.length - 2; i >= 0; i--)// last item is a dummy item
						{
							adapter.add(historyPojo[i]);
						}
					}

				} else {
					popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
				}

			} else {
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		}
		catch(JsonParseException ex) //Json exception
		{
			popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
		}
		catch (Exception ex1) { //Error in response
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			Log.e(TAG, " " + ex1.getMessage());
		}
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
	//Show pop-up messages	
	
	private void popdNoInternet() {

		pdw = new PopupDialogWidget(getActivity(), R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(getActivity(), title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}

	private void popdReauth(String title, String content) {

		pdw = new PopupDialogWidget(getActivity(), title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		pdw.okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw.dismiss();
				startActivity(new Intent(getActivity(), UiProfileLogin.class));
				getActivity().finish();
			}
		});

	}

}
