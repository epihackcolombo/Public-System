package org.cosmic.mobuzz.general.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.cosmic.mobuzz.general.SplashActivity;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.pojo.HistoryReportPojo;
import org.cosmic.mobuzz.general.pojo.HotspotsEpidPojo;
import org.cosmic.mobuzz.general.pojo.ImagePojo;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.ui.support.PopuplistAdapter;
import org.cosmic.mobuzz.general.util.CMCRegions;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

public class UiMapReports extends android.support.v4.app.FragmentActivity {

	private GoogleMap mMap;
	private LatLng latLng;
	private Dialog progressDialog;
	private final Context context = UiMapReports.this;
	final String TAG = UiMapReports.class.getName();

	private Typeface font;
	private String url_map_report_new, url_map_report_rejected, url_map_report_accepted, url_cmcf_server_hotspots;
	private String url_report_details;
	private String url_map_reports_update;
	
	CMCRegions cmcRegions;
	PopupDialogWidget pdw;
	Dialog dialog;
	
	CheckBox ckb_w1, ckb_w2, ckb_w3, ckb_hotspots;
	HistoryReportPojo[] hps_new, hps_rej, hps_acc;
	
	List<Marker> lstMarkers_new, lstMarkers_rejected, lstMarkers_accepted;
	int changeStatus = -1;
	int intAccept = 0, intNew = 0, intReject = 0;
	
	
	Button but_datefrom, but_dateto;
	TextView txtv_type, txtv_dates;
	String str_datefrom = null, str_dateto = null;
	
	ImageView imageViewBS;
	Bitmap decodedByte;
	ImagePojo imgp;
	
	private String[] cmcwards =  new String[]{"Other", "01. Mattakkuliya", "02. Modera", "03. Mahawatte", "04. Aluthmawatha", "05. Lunupokuna", "06. Bloemandhal", "07. Kotahena East", "08. Kotahena West", "09. Kochchikade North", "10. Gintupitiya", "11. Masangas Weediya", "12. New Bazaar", "13. Grandpass North", "14. Grandpass South", "15. Maligawatte West", "16. Aluthkade East", "17. Aluthkade West", "18. Kehelwatte", "19. Kochchikade South", "20. Fort", "21. Kopannaweediya", "22. Wekanda", "23. Hunupitiya", "24. Suduwella", "25. Panchikawatte", "26. Maradana", "27. Maligakanda", "28. Maligawatte East", "29. Dematagoda", "30. Wanathamulla", "31. Kuppiyawatte East", "32. Kuppiyawatte West", "33. Borella North", "34. Narahenpita", "35. Borella South", "36. Cinnamon Gardens", "37. Kollupitiya", "38. Bambalapitiya", "39. Milagiriya", "40. Thimbirigasyaya", "41. Kirula", "42. Havelok Twon", "43. Wellewatte North", "44. Kirillapone", "45. Pamankada East", "46. Pamankada West", "47. Wellewatte South"};
	
	//Note: Status message order matters, since some of the messages are used to block the user
	private String[] cmcmessages      = new String[]{"The CMC thanks you for your cooperation!", "This message confirms that CMC has investigated your complaint, and is grateful for your observation!", "Your breeding site complaint has been assigned to the following division: Drainage.", "Your breeding site complaint has been assigned to the following division: Solid Waste.", "Your breeding site complaint has been assigned to the following division: District Engineer.", "Thank you for your complaint. The CMC has checked the site you complained and found that problem has been rectified.", "Thank you for your complaint. The CMC has checked the site you complained and no problem was found.", "Thank you for your complaint. Unfortunately, we are unable to investigate it as it has been sent from a location outside CMC jurisdiction.", "The complainant was not reachable despite repeated attempts. The complainant has now been rejected.", "The information you sent has been found to be irrelevant. Please note that repeated reporting of irrelevant information will result in your account being blocked by the CMC.", "Your account has been blocked."};
	//messages with html style
	private String[] cmcmessagesstyle = new String[]{"<font color=\"red\"><b>[Status: New]</b><br/>The CMC thanks you for your cooperation!</font>", 
			"<font color=\"#088A08\"><br/><b>[Status: Accept]</b><br/>This message confirms that CMC has investigated your complaint, and is <b><u>grateful for your observation</u></b>!</font>", 
			"<font color=\"#088A08\">Your breeding site complaint has been assigned to the following division: <b><u>Drainage</u></b>.</font>", 
			"<font color=\"#088A08\">Your breeding site complaint has been assigned to the following division: <b><u>Solid Waste</u></b>.</font>", 
			"<font color=\"#088A08\">Your breeding site complaint has been assigned to the following division: <b><u>District Engineer</u></b>.</font>", 
			"<font color=\"#088A08\">Thank you for your complaint. The CMC has checked the site you complained and found that <b><u>problem has been rectified</u></b>.</font>", 
			"<font color=\"#088A08\">Thank you for your complaint. The CMC has checked the site you complained and <b><u>no problem was found</u></b>.</font>", 
			"<font color=\"#088A08\">Thank you for your complaint. Unfortunately, we are unable to investigate it as <b><u>it has been sent from a location outside CMC jurisdiction</u></b>.</font>", 
			"<font color=\"#736F6E\"><br/><b>[Status: Reject]</b><br/>The complainant was not reachable despite repeated attempts. <b><u>The complainant has now been rejected</u></b>.</font>", 
			"<font color=\"#736F6E\"><b><u>The information you sent has been found to be irrelevant</u></b>. Please note that repeated reporting of irrelevant information will result in your account being blocked by the CMC.</font>", 
			"<font color=\"#736F6E\">Your account has been blocked.</font>"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_map_reports);

		url_map_report_new = getString(R.string.url_server_port) + getString(R.string.url_map_reports_new);
		url_map_report_rejected = getString(R.string.url_server_port) + getString(R.string.url_map_reports_rejected);
		url_map_report_accepted = getString(R.string.url_server_port) + getString(R.string.url_map_reports_accepted);
		url_report_details = getString(R.string.url_server_port) + getString(R.string.url_report_details);
		url_map_reports_update = getString(R.string.url_server_port) + getString(R.string.url_map_reports_update);
		url_cmcf_server_hotspots = getString(R.string.url_server_port) + getString(R.string.url_cmcf_hotspots); 
		
		txtv_type = (TextView) findViewById(R.id.txtv_type);
		txtv_dates = (TextView) findViewById(R.id.txtv_dates);
		
		ckb_w1 = (CheckBox) findViewById(R.id.ckb_w1);
		ckb_w2 = (CheckBox) findViewById(R.id.ckb_w2);
		ckb_w3 = (CheckBox) findViewById(R.id.ckb_w3);
		ckb_hotspots = (CheckBox) findViewById(R.id.ckb_hotspots);

		font = GlobalMethods.getTypeface(context);
		
		txtv_type.setTypeface(font);
		txtv_dates.setTypeface(font);
		
		ckb_w1.setTypeface(font);
		ckb_w2.setTypeface(font);
		ckb_w3.setTypeface(font);
		
		but_datefrom = (Button) findViewById(R.id.but_datefrom);
		but_dateto = (Button) findViewById(R.id.but_dateto);
		
		but_datefrom.setTypeface(font);
		but_dateto.setTypeface(font);

		latLng = GlobalVariables.GPS_COLOMBO; // default GPS coordinates for Colombo
		cmcRegions = null;

		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		if (status == ConnectionResult.SUCCESS) {
			setUpMap();

		} else {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}
		
		manageCheckBoxes();
		
		GlobalIO.writeLog(context, TAG, LogAction.START, true);
		
		
		but_setDates();
		ckb_hotspots();
	}

	private void but_setDates()
	{
		
		but_datefrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupDatePicker(0);
			}
		});
		
		but_dateto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupDatePicker(1);
			}
		});

	}
	
	private void ckb_hotspots()
	{
		
		ckb_hotspots.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked)
				{
					//Call server for data
					if( GlobalMethods.validateString(but_datefrom.getText().toString()) && GlobalMethods.validateString(but_dateto.getText().toString()) &&
							(but_datefrom.getText().toString()).length()>8 && (but_dateto.getText().toString()).length()>8 ) //To avoid default text is passed
					{
						if(isValidDates())
						{
							if(ckb_hotspots.isChecked())
							{
								getHotspotData(but_datefrom.getText().toString(), but_dateto.getText().toString());
							}
						}
						else
						{
							ckb_hotspots.setChecked(false);
						}
					}
					else
					{
						but_datefrom.setError("error");//message is not shown
						but_dateto.setError("error");//message is not shown
						ckb_hotspots.setChecked(false);
					}
				}
				else
				{
					
				}
			}
			
		});

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_ui_map_reports, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.mapStreet:

			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			}
			return true;

		case R.id.mapSat:

			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			}
			return true;

		case R.id.mapHybrid:

			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			}
			return true;

		default:
			return false;

		}

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
		
		if(mMap != null)
		{
			mMap.setMyLocationEnabled(false);
		}
		
		GlobalIO.writeLog(context, TAG, LogAction.PAUSE, true);
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

	//-------------------- Setup the map -----------------------//

	private void setUpMap() {

		if (mMap == null) {
			MapsInitializer.initialize(context);
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemap)).getMap();

			mMap.setMyLocationEnabled(true);
			
			if (latLng != null) {
				CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).build();
				mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			}
			
			getMohs();
		}
	}
	
	//Draw MOH boundaries
	private void getMohs() {

		cmcRegions = new CMCRegions();

		for (int i = 1; i <= 6; i++) {
			List<LatLng> mohCoordinates = cmcRegions.getCMCMoh(i);

			if (mohCoordinates != null && mohCoordinates.size() > 0) {
				mMap.addPolygon(new PolygonOptions().addAll(mohCoordinates).fillColor(Color.parseColor("#00FBEFF2"))).setStrokeWidth(2);
			}
		}
	}

	private void getPublicReports(int action, String strfrom, String strto) {

		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String username = pref.getString("username", "defv");
		String time_stamp = pref.getString("time_stamp", "");
		String uudid = pref.getString("uudid", "");

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("user", username);
			obj.putOpt("time_stamp", time_stamp);
			obj.putOpt("uudid", uudid);
			obj.putOpt("action", action);
			obj.putOpt("strfrom", strfrom);
			obj.putOpt("strto", strto);

			String jString = obj.toString();

			String[] loginData;
			
			if(action<0)
			{
				loginData = new String[]{ jString, url_map_report_rejected };
			}
			else if(action==0)
			{
				loginData = new String[]{ jString, url_map_report_new };
			}
			else
			{
				loginData = new String[]{ jString, url_map_report_accepted };
			}

			new FetchMapData(action, loginData).execute();

		} catch (JSONException ex) {
			Log.e(TAG, " " + ex.getMessage());
		}
	}

	private void getWards() {

		if (cmcRegions == null) {
			
			cmcRegions = new CMCRegions();

			for (int i = 1; i <= 47; i++) {
				List<LatLng> wardCoordinates = cmcRegions.getCMCWard(i);

				if (wardCoordinates != null && wardCoordinates.size() > 0) {
					mMap.addPolygon(new PolygonOptions().addAll(wardCoordinates).fillColor(Color.parseColor(GlobalVariables.COLOR_WARD_OVERLAY))).setStrokeWidth(3);
				}
			}
		}
	}
	
	private boolean isValidDates()
	{
		but_datefrom.setError(null);
		but_dateto.setError(null);
		
		try
		{
			String strfrom = but_datefrom.getText().toString().trim();
			String strto = but_dateto.getText().toString().trim();
			
			if(strfrom.contains("-") && strto.contains("-"))
			{
				int intfrom = Integer.parseInt(strfrom.replace("-", ""));
				int intto = Integer.parseInt(strto.replace("-", ""));
				
				if(intfrom <= intto)
				{
					str_datefrom = strfrom + " 00:00:00";
					str_dateto = strto + " 23:59:59";

					return true;
				}
			}

			but_datefrom.setError("error");//message is not shown
			but_dateto.setError("error");//message is not shown
			return false;
		}
		catch(Exception ex)
		{
			but_datefrom.setError("error");//message is not shown
			but_dateto.setError("error");//message is not shown
			return false;
		}
	}

	private void manageCheckBoxes()
	{
		ckb_w1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked)
				{
					if(isValidDates())
					{
						getPublicReports(0, str_datefrom, str_dateto);
					}	
					else
					{
						ckb_w1.setText("New");
						ckb_w1.setChecked(false);
					}

				}else
				{
					ckb_w1.setText("New");
					
					if(lstMarkers_new != null)
					{
						for(Marker marker : lstMarkers_new)
						{
							marker.remove();
						}
						
						lstMarkers_new.clear();
						lstMarkers_new = null;
					}
				}
				
			}
		});
		
		ckb_w2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked)
				{
					if(isValidDates())
					{
						getPublicReports(1, str_datefrom, str_dateto);
					}	
					else
					{
						ckb_w2.setText("Accepted");
						ckb_w2.setChecked(false);
					}
					
				}else
				{
					ckb_w2.setText("Accepted");
					
					if(lstMarkers_accepted != null)
					{
						for(Marker marker : lstMarkers_accepted)
						{
							marker.remove();
						}
						
						lstMarkers_accepted.clear();
						lstMarkers_accepted = null;
					}
				}
				
			}
		});		
		
		ckb_w3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked)
				{
					if(isValidDates())
					{
						getPublicReports(-1, str_datefrom, str_dateto);
					}	
					else
					{
						ckb_w3.setText("Rejected");
						ckb_w3.setChecked(false);
						
					}
					
				}else
				{
					ckb_w3.setText("Rejected");
					
					if(lstMarkers_rejected != null)
					{
						for(Marker marker : lstMarkers_rejected)
						{
							marker.remove();
						}
						
						lstMarkers_rejected.clear();
						lstMarkers_rejected = null;
					}
				}
				
			}
		});		
	}

	//-------------------- Fetch map data -----------------------//

	private class FetchMapData extends AsyncTask<String, Void, String> {
		
		int action = 0;
		String[] loginData;
		
		public FetchMapData(int action, String[] loginData)
		{
			this.action = action;
			this.loginData = loginData;
		}

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
		protected String doInBackground(String... data) {

			return HttpUpload.uploadData(loginData[0], loginData[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			responseToResult(result, action);
		}
	}

	public void responseToResult(String result, int action) {
		
		System.out.println("result: "+result);
		

		if (GlobalMethods.validateString(result)) {

			try {

				//validation block ------------------------
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

				} //-----------------------------------------
				else {

					if (GlobalMethods.validateString(result)) {
						
						if(result.contains("[{'SQLSuccess': 'SQL_no_data'}]"))
						{
							Toast.makeText(context, getString(R.string.toast_report_data_no), Toast.LENGTH_LONG).show();
							return;
						}
						
						Gson gson = new Gson();
						
						if(action < 0)
						{
							hps_rej = gson.fromJson(result, HistoryReportPojo[].class);
							manageHotspots(hps_rej, -1);
						}
						else if(action == 0)
						{
							hps_new = gson.fromJson(result, HistoryReportPojo[].class);
							manageHotspots(hps_new, 0);
						}
						else
						{
							hps_acc = gson.fromJson(result, HistoryReportPojo[].class);
							manageHotspots(hps_acc, 1);
						}

						getWards();

					} 
					else 
					{
						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				}

			} catch (Exception ex) {
				Log.e(TAG, " " + ex.getMessage());
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} else {
			Toast.makeText(context, getString(R.string.toast_report_data_no), Toast.LENGTH_LONG).show();
		}

	}
	
	
	private void manageHotspots(HistoryReportPojo[] hps, int action)
	{
		if(action<0)
		{
			lstMarkers_rejected = new ArrayList<Marker>();
		}
		else if(action==0)
		{
			lstMarkers_new = new ArrayList<Marker>();
		}
		else
		{
			lstMarkers_accepted = new ArrayList<Marker>();
		}

		
		if (hps.length > 0) {
			
			if(action<0)
			{
				intReject = hps.length;
				ckb_w3.setText("Rejected("+String.valueOf(intReject)+")");
			}
			else if(action==0)
			{
				intNew = hps.length;
				ckb_w1.setText("New("+String.valueOf(intNew)+")");
			}
			else
			{
				intAccept = hps.length;
				ckb_w2.setText("Accepted("+String.valueOf(intAccept)+")");
			}
		
			
			LatLngBounds.Builder builder = new LatLngBounds.Builder(); //To auto-fit the map to show all the markers.

			for(int i=0; i<hps.length; i++)
			{
				
				try{
					
					if(hps[i].getGps().contains(","))
					{
						String[] strLatLng = hps[i].getGps().split(",");
						

						if(action<0)
						{
							Marker marker = mMap.addMarker( new MarkerOptions()
										.position(new LatLng(Double.parseDouble(strLatLng[0]), Double.parseDouble(strLatLng[1])))
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_icon_marker_public_reg))
										.draggable(true)
										.title(String.valueOf(i)+","+String.valueOf(action)) );				
							
							lstMarkers_rejected.add(marker);
							builder.include(marker.getPosition());
						}
						else if(action==0)
						{
							Marker marker = mMap.addMarker( new MarkerOptions()
										.position(new LatLng(Double.parseDouble(strLatLng[0]), Double.parseDouble(strLatLng[1])))
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_icon_marker_public_new))
										.draggable(true)
										.title(String.valueOf(i)+","+String.valueOf(action)) );		
							
							lstMarkers_new.add(marker);
							builder.include(marker.getPosition());
						}
						else
						{
							Marker marker = mMap.addMarker( new MarkerOptions()
										.position(new LatLng(Double.parseDouble(strLatLng[0]), Double.parseDouble(strLatLng[1])))
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_icon_marker_public_acc))
										.draggable(true)
										.title(String.valueOf(i)+","+String.valueOf(action)) );		
							
							lstMarkers_accepted.add(marker);
							builder.include(marker.getPosition());
						}
	
					}
				}
				catch(Exception ex)
				{
					// nothing to do.
				}				
				
			}
			
			//Show all the markers in the map for the selected category
			LatLngBounds bounds = builder.build();
			int padding = 0; //Offset from edges of the map in pixels
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
			mMap.animateCamera(cu); //Move the camera
			
			mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0) {
					if (GlobalMethods.validateString(arg0.getTitle())) // if marker source is clicked
					{
						try{
							
							String title = arg0.getTitle();
	
							String[] idActon = title.split(",");
							
							 int i = Integer.parseInt(idActon[0]);
							 int action = Integer.parseInt(idActon[1]);
							 
							 if(action<0)
							 {
								 popdMarkerInfo(i, action, hps_rej);
							 }
							 else if(action==0)
							 {
								 popdMarkerInfo(i, action, hps_new);
							 }
							 else
							 {
								 popdMarkerInfo(i, action, hps_acc);
							 }
								 
						}
						catch(Exception ex)
						{
							// do nothing
						}
	
					}

					return true;
				}

			});
								
			
		} else {
			Toast.makeText(context, getString(R.string.toast_report_data_no), Toast.LENGTH_LONG).show(); 
		}
		
	}
	
	
	
	
	//--------------------------------Pop-up actions----------------------------------//

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}
	
	
	private void popdRequestUpdate(String title, String content, final int i, boolean isUpdated) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		if (isUpdated) {

			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					if(dialog.isShowing())
					{
						dialog.dismiss();
					}
					
					lstMarkers_new.get(i).setVisible(false);
					ckb_w1.setText("New("+String.valueOf(--intNew)+")");
				}
			});

		}
	}	
	
	//------------------------------------------------------------------//
	//Note: Status message order matters, since some of the messages are used to block the user. And the same order is referring from the back-end.

	private void popdMarkerInfo(final int i, int action, final HistoryReportPojo[] hrps) {

		int cmcward;

		changeStatus = -1;

		try {

			cmcward = Integer.parseInt(hrps[i].getWard());
			if (cmcward < 0 || cmcward > 47) {
				cmcward = 0;
			}

			dialog = new Dialog(context);
			dialog.getWindow();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.xml_popup_markerreport);
			dialog.setCanceledOnTouchOutside(false);

			
			TextView txt_info_title = (TextView) dialog.findViewById(R.id.txt_info_title);
			TextView txt_gps = (TextView) dialog.findViewById(R.id.txt_msgd_gps);
			TextView txt_address = (TextView) dialog.findViewById(R.id.txt_msgd_address);
			TextView txt_date = (TextView) dialog.findViewById(R.id.txt_msgd_date);
			Button but_msgd_ward = (Button) dialog.findViewById(R.id.but_msgd_ward);
			TextView txt_remarks = (TextView) dialog.findViewById(R.id.txt_msgd_remark);
			TextView txt_assessment = (TextView) dialog.findViewById(R.id.txt_msgd_assessment);
			Button but_msgd_cmcmessage = (Button) dialog.findViewById(R.id.but_msgd_cmcmessage);
			ImageButton ibut_image = (ImageButton) dialog.findViewById(R.id.ibut_image);
			TextView txt_calluser = (TextView) dialog.findViewById(R.id.txt_msgd_calluser);
			TextView txt_callback = (TextView) dialog.findViewById(R.id.txt_callback);
			Button but_actimage = (Button) dialog.findViewById(R.id.but_actimage);
			Button but_saveimage = (Button) dialog.findViewById(R.id.but_saveimage);
			TableRow tblr_msg_title = (TableRow) dialog.findViewById(R.id.tblr_msg_title);

			txt_info_title.setText(hrps[i].getUsername());
			txt_gps.setText(hrps[i].getGps());
			txt_address.setText(hrps[i].getAddress());
			txt_date.setText(hrps[i].getDate());
			but_msgd_ward.setText(cmcwards[cmcward]);
			txt_remarks.setText(hrps[i].getRemarks());
			txt_assessment.setText(hrps[i].getAssessment());
			but_msgd_cmcmessage.setText(hrps[i].getCmcmessage());
			txt_calluser.setText(hrps[i].getFullname());

			txt_info_title.setTypeface(font);
			txt_gps.setTypeface(font);
			txt_address.setTypeface(font);
			txt_date.setTypeface(font);
			txt_remarks.setTypeface(font);
			txt_assessment.setTypeface(font);
			but_msgd_ward.setTypeface(font);
			but_msgd_cmcmessage.setTypeface(font);

			if (hrps[i].getImage() > 0) {
				ibut_image.setImageResource(R.drawable.logo_icon_download_enable);
				ibut_image.setEnabled(true);
			} else {
				ibut_image.setImageResource(R.drawable.logo_icon_download_disable);
				ibut_image.setEnabled(false);
			}

			ibut_image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					try {
						setImage(Integer.parseInt(hrps[i].getId()), "thumbnail");
					} catch (Exception ex) {
						// nothing to do
					}

				}
			});
			
			but_actimage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					try {
						setImage(Integer.parseInt(hrps[i].getId()), "actual");
					} catch (Exception ex) {
						// nothing to do
					}

				}
			});

			but_msgd_cmcmessage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					try {
						popdListAssessment();
					} catch (Exception ex) {
						// nothing to do
					}

				}
			});

			but_msgd_ward.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					try {
						popdListWards();
					} catch (Exception ex) {
						// nothing to do
					}

				}
			});
			
			LinearLayout lnlout_calluser = (LinearLayout) dialog.findViewById(R.id.lnlout_calluser);
			
			if( GlobalMethods.validateString(hrps[i].getFullname()) && GlobalMethods.validateString(hrps[i].getContnumber()) )
			{
				lnlout_calluser.setOnClickListener(new OnClickListener() {
				    @Override           
				    public void onClick(View v) {
				    	Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:"+hrps[i].getContnumber()));
						startActivity(callIntent);
				    }
				});	
			}
			else
			{
				lnlout_calluser.setVisibility(View.GONE);
				txt_callback.setVisibility(View.GONE);
			}


			but_saveimage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					try
					{

						String imagePath = getGalleryImage(imgp.getImg_name());
						
						if(GlobalMethods.validateString(imagePath))
						{
							//Image is already in the gallery.
							Toast.makeText(context, "Image is already in the gallery!", Toast.LENGTH_LONG).show();
						}
						else
						{
				    		 if(imageViewBS != null)
				    		 {
							    imageViewBS.setDrawingCacheEnabled(true);
							    decodedByte = imageViewBS.getDrawingCache();
							    String res = Images.Media.insertImage(context.getContentResolver(), decodedByte, imgp.getImg_name().substring(1), "");
							    
							    if(GlobalMethods.validateString(res))
							    {
							    	Toast.makeText(context, "Saved image to local storage.", Toast.LENGTH_LONG).show();
							    	v.setVisibility(View.GONE);
							    }
							    else
							    {
							    	Toast.makeText(context, "Image could not be saved locally!", Toast.LENGTH_LONG).show();
							    }
   			 
				    		 }
						}

					}
					catch(Exception ex)
					{
						if(GlobalVariables.PRINT_DEBUG)
						{
							Log.e(TAG, " " + ex.getMessage());
						}
					}

				}
			});
			
			
			//To get the image from the disc, if available
			try
			{

				if(GlobalMethods.validateString(hrps[i].getImagepath()))
				{
					
					//1. try to get full image
					//2. otherwise get the thumbnail
		
					String downloadImgName = hrps[i].getImagepath().replace(GlobalVariables.IMAGE_THUMB_POSTFIX, "");// remove thumbnail post
					
					String imagePath = getGalleryImage(downloadImgName);

					if(!GlobalMethods.validateString(imagePath))
					{
						downloadImgName = downloadImgName + GlobalVariables.IMAGE_THUMB_POSTFIX;
						imagePath = getGalleryImage(downloadImgName);
					}

					if(GlobalMethods.validateString(imagePath))
					{
		
			    		 imageViewBS = (ImageView) dialog.findViewById(R.id.imageViewBS);
			    		 imageViewBS.setVisibility(View.VISIBLE);
			    		 
			    		 setGalleryImage(imagePath); //since this is from the image database, path validation is ignored
			    		 
			    		 but_saveimage.setVisibility(View.GONE);
			    		 
			    		 if(downloadImgName.contains(GlobalVariables.IMAGE_THUMB_POSTFIX))
			    		 {
			    			 but_actimage.setVisibility(View.VISIBLE);
			    			 ibut_image.setVisibility(View.GONE);
			    		 }
			    		 else //full image is available
			    		 {
			    			 but_actimage.setVisibility(View.GONE);
			    			 ibut_image.setVisibility(View.GONE);
			    		 }
					}

				}

			}
			catch (Exception ex)
			{
				//Log.e(TAG, " " + ex.getMessage());
				if(GlobalVariables.PRINT_DEBUG)
				{
					Log.e(TAG, " " + ex.getMessage());
				}
			}
				
			Button btn_msg_cancel = (Button) dialog.findViewById(R.id.btn_msg_cancel);
			Button btn_msg_update = (Button) dialog.findViewById(R.id.btn_msg_update);
			btn_msg_cancel.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);
			btn_msg_update.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

			btn_msg_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// lstMarkers_new.get(i).setVisible(false);
					dialog.dismiss();
				}
			});

			btn_msg_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					try {
	
							Button but_msgd_ward = (Button) dialog.findViewById(R.id.but_msgd_ward);
							Button but_msgd_cmcmessage = (Button) dialog.findViewById(R.id.but_msgd_cmcmessage);

							int id = Integer.parseInt(hrps[i].getId());

							String ward = (String) but_msgd_ward.getText();
							if (GlobalMethods.validateString(ward) && ward.contains(".")) {
								String[] strWard = ward.split(". ");
								ward = strWard[0];
							} else {
								ward = "0";
							}


							String cmcmessage = (String) but_msgd_cmcmessage.getText();
							int misusecounter, blockuser;

							
							if ((!cmcmessage.endsWith(hrps[i].getCmcmessage())) && changeStatus == 9) { //9: "The information you sent has been found to be irrelevant. Please note that repeated reporting of irrelevant information will result in your account being blocked by the CMC.", in the message array
								misusecounter = 1;
								blockuser = 0;
							} else if ((!cmcmessage.endsWith(hrps[i].getCmcmessage())) && changeStatus == 10) { //10: "Your account has been blocked.", in the message array
								misusecounter = 1;
								blockuser = 1;
							} else {
								misusecounter = 0;
								blockuser = 0;
							}
							
							setChanges(id, ward, cmcmessage, misusecounter, blockuser, i, hrps[i].getUsername(), changeStatus); //i: check-box
							
					} catch (Exception ex) {
						// nothing to do
						if(GlobalVariables.PRINT_DEBUG)
						{
							Log.e(TAG, " " + ex.getMessage());
						}
						Toast.makeText(context, getString(R.string.toast_report_error), Toast.LENGTH_LONG).show();
					}

				}
			});

			if (action != 0) {
				but_msgd_ward.setEnabled(false);
				but_msgd_cmcmessage.setEnabled(false);
				btn_msg_update.setVisibility(View.GONE);
				btn_msg_cancel.setText(getString(R.string.msg_common_ok));
				
				but_msgd_cmcmessage.setBackgroundResource(R.drawable.bg_transparent);
				but_msgd_ward.setBackgroundResource(R.drawable.bg_transparent);
			}
			
			
			//change title-bar color
			if(action < 0)
			{
				tblr_msg_title.setBackgroundColor(Color.GRAY);
			}
			else if(action == 0)
			{
				tblr_msg_title.setBackgroundColor(Color.RED);
			}
			else
			{
				if(GlobalMethods.validateString(hrps[i].getUsername()) && (hrps[i].getUsername().contains("/")))
				{
					tblr_msg_title.setBackgroundColor(Color.BLUE);
				}
				else
				{
					tblr_msg_title.setBackgroundColor(getResources().getColor(R.color.mobuzz_yellow));
				}
			}
			
			dialog.show();

		} catch (Exception ex) {
			// nothin to do;
			return;
		}
		
	}

	private String getGalleryImage(String imageName)
	{

	     if( GlobalMethods.validateString(imageName))
	     {
		
			//Check whether the image had been download previously and in cache.
			try
			{
				
				String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.TITLE};
				Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				Cursor cursor = managedQuery( uri, projection, null, null, null);

				if(cursor.getCount()==0)
				{
				     cursor.close(); //No external storage card
				} 
				else if(cursor.getCount()>0)
				{    
					 
					 cursor.moveToFirst();
					 
					 do {

					     int columnIndex_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					     int columnIndex_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
					     
					     String imagePath = cursor.getString(columnIndex_data);
					     String imageTitle = cursor.getString(columnIndex_title);
					     
				    	 if(imageName.equals("/"+imageTitle)) // "/": in order to match with saving substring pattern. substring is not used here to avoid 1-length string errors.
				    	 {
				    		 cursor.moveToLast();
				    		 return imagePath;
				    	 }
						 
					 } while (cursor.moveToNext());

					 
					 if(GlobalVariables.PRINT_DEBUG)
					 {
						 Log.e(TAG, "cursor ends");
					 }

			    }
				
				return null;
			}
			catch(Exception ex)
			{
				 if(GlobalVariables.PRINT_DEBUG)
				 {
					 Log.e(TAG, " " + ex.getMessage());
				 }
				 
				 return null;
			}
			
	     }
	     else
	     {
	    	 return null;
	     }
	
	}
	
	
	private void setGalleryImage(String imagePath)
	{
		if (GlobalMethods.validateString(imagePath)) {

			imageViewBS.setImageURI(Uri.parse(imagePath));
		}
	}
	
	private void popdReauth(String title, String content) {

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
	
	
	
	//-------------------- Network communication -----------------------//
	
	
	//-------------------- Fetch image data -----------------------
	
	private void setImage(int id, String imagesize) {

		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String username = pref.getString("username", "defv");
		String time_stamp = pref.getString("time_stamp", "");
		String uudid = pref.getString("uudid", "");

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("user", username);
			obj.putOpt("time_stamp", time_stamp);
			obj.putOpt("uudid", uudid);
			
			obj.putOpt("id", id);
			obj.putOpt("imagesize", imagesize);

			String jString = obj.toString();
			String[] data = { jString, url_report_details };

			new downloadImage().execute(data);

		} catch (JSONException ex) {
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}
	}

	private class downloadImage extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			progressDialog = new Dialog(context);
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.msg_downloading_para2));
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));

			progressDialog.show();			
		}

		@Override
		protected String doInBackground(String... params) {
			return HttpUpload.uploadDataCache(params[0], params[1]);
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

				//validation block ------------------------
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

				} //-----------------------------------------
				else if (result.contains("{'img_type':")) {

					if(dialog.isShowing())
					{
						Gson gson = new Gson();
						imgp = gson.fromJson(result, ImagePojo.class);
						
						imageViewBS = (ImageView) dialog.findViewById(R.id.imageViewBS);
						byte[] decodedString = Base64.decode(imgp.getImg_data(), Base64.DEFAULT);
						decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
						imageViewBS.setImageBitmap(decodedByte);
						
						ImageButton ibut_image = (ImageButton) dialog.findViewById(R.id.ibut_image);
						ibut_image.setVisibility(View.GONE);
						
						decodedString =null; // to clear memory
						
						Button but_actimage = (Button) dialog.findViewById(R.id.but_actimage);
						Button but_saveimage = (Button) dialog.findViewById(R.id.but_saveimage);
						
						if( ("thumbnail").equals(imgp.getImg_type()) )
						{
							but_actimage.setVisibility(View.VISIBLE);
						}
						else
						{
							but_actimage.setVisibility(View.GONE);
						}	
						
						but_saveimage.setVisibility(View.VISIBLE);

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

	//-------------------- Upload changers -----------------------
	

	private void setChanges(int id, String ward, String cmcmessage, int misusecounter, int blockuser, int i, String publicuser, int changeStatus) {

		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String username = pref.getString("username", "defv");
		String time_stamp = pref.getString("time_stamp", "");
		String uudid = pref.getString("uudid", "");
		//i: check-box

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("user", username);
			obj.putOpt("time_stamp", time_stamp);
			obj.putOpt("uudid", uudid);
			
			obj.putOpt("id", id);
			obj.putOpt("ward", ward);
			obj.putOpt("cmcmessage", cmcmessage);
			obj.putOpt("misusecounter", misusecounter);
			obj.putOpt("blockuser", blockuser);
			obj.putOpt("publicuser", publicuser);
			obj.putOpt("status", changeStatus);

			String jString = obj.toString();
			String[] data = { jString, url_map_reports_update };

			new uploadChanges(data, i).execute();

		} catch (JSONException ex) {
			Log.e(TAG, " " + ex.getMessage());
		}
	}

	private class uploadChanges extends AsyncTask<String, Void, String> {

		String[] data;
		int i;
		
		public uploadChanges(String[] data, int i)
		{
			this.data = data;
			this.i = i;
		}
		
		@Override
		protected void onPreExecute() {
			progressDialog = new Dialog(context);
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.msg_uploading_para1));
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));

			progressDialog.show();			
		}

		@Override
		protected String doInBackground(String... params) {
			return HttpUpload.uploadData(data[0], data[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);



			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			responseToUpload(result, i);
		}
	}

	public void responseToUpload(String result, int i) {

		try {
			if (GlobalMethods.validateString(result)) {

				//validation block ------------------------
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

				} //-----------------------------------------
				else {
					
					if(result.contains("action#ok"))
					{
						popdRequestUpdate(getString(R.string.msg_report_update_ok_title1), getString(R.string.msg_report_update_ok_para1), i, true);
					}
					else
					{
						popdRequestUpdate(getString(R.string.msg_report_update_no_title1), getString(R.string.msg_report_update_no_para1), i, false);
					}

				}

			} else {
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} catch (Exception ex) {
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			Log.e(TAG, " " + ex.getMessage());
		}

	}

		
	//-------------------------------------------------------------//

	
	//--------------------- Pop-up lists --------------------------//
	
	//-------------------- Phi assessment -----------------------
	
	private void popdListAssessment() {

		final Dialog dialogList = new Dialog(context);
		dialogList.getWindow();
		dialogList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogList.setContentView(R.layout.xml_popup_list_dialog);
		dialogList.setCanceledOnTouchOutside(false);
		dialogList.setCancelable(false);

		TextView txt_msg_title = (TextView) dialogList.findViewById(R.id.txt_msg_title);
		txt_msg_title.setText(R.string.msg_breedingsite_phi_assessment);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		final ArrayList<String> list = new ArrayList<String>();

		for (String cmcmessage : cmcmessagesstyle) {
			list.add(cmcmessage);
		}

		PopuplistAdapter adapter = new PopuplistAdapter(context, list);
		ListView lstw_msg_content = (ListView) dialogList.findViewById(R.id.lstw_msg_content);
		lstw_msg_content.setAdapter(adapter);
		lstw_msg_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (GlobalMethods.validateString(item)) {
					
					if(dialog.isShowing())
					{
						Button but_msgd_cmcmessage = (Button) dialog.findViewById(R.id.but_msgd_cmcmessage);
						//but_msgd_cmcmessage.setText(item);
						but_msgd_cmcmessage.setText(cmcmessages[position]);
						changeStatus = position;
					}

				}

				dialogList.dismiss();
			}
		});

		Button btn_msg_no = (Button) dialogList.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText(getString(R.string.msg_common_cancel));
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogList.dismiss();
			}
		});

		dialogList.show();

	}

	//-------------------- Cmc wards -----------------------

	private void popdListWards(){

		final Dialog dialogList = new Dialog(context);
		dialogList.getWindow();
		dialogList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogList.setContentView(R.layout.xml_popup_list_dialog);
		dialogList.setCanceledOnTouchOutside(false);
		dialogList.setCancelable(false);

		TextView txt_msg_title = (TextView) dialogList.findViewById(R.id.txt_msg_title);
		txt_msg_title.setText(R.string.msg_breedingsite_phi_ward);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		final ArrayList<String> list = new ArrayList<String>();
		for (String ward : cmcwards) {
			list.add(ward);
		}

		PopuplistAdapter adapter = new PopuplistAdapter(context, list);
		ListView lstw_msg_content = (ListView) dialogList.findViewById(R.id.lstw_msg_content);
		lstw_msg_content.setAdapter(adapter);
		lstw_msg_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (GlobalMethods.validateString(item)) {
					
					if(dialog.isShowing())
					{
						Button but_msgd_ward = (Button) dialog.findViewById(R.id.but_msgd_ward);
						but_msgd_ward.setText(item);
					}

				}

				dialogList.dismiss();
			}
		});

		Button btn_msg_no = (Button) dialogList.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText(getString(R.string.msg_common_cancel));
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogList.dismiss();
			}
		});

		dialogList.show();

	}
	
	//-----------------------------------------------------------//
	
	
	//---------------------------Picker--------------------------------//	
	
	
	//---------------------------Date time picker----------------------
	// custom popup date picker
    Calendar mobiCalendar = Calendar.getInstance();   
    final public String DATE_FORMAT = "yyyy-MM-dd";
	public void popupDatePicker(final int picker)
	{
		DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() 
		{
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
			{
				mobiCalendar.set(Calendar.YEAR, year);
				mobiCalendar.set(Calendar.MONTH, monthOfYear);
				mobiCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				DateFormat fmtDateAndTime = new SimpleDateFormat(DATE_FORMAT);
				String curDate = fmtDateAndTime.format(mobiCalendar.getTime());

				if(GlobalMethods.validateString(curDate))
				{
					if(picker>0)
					{
						but_dateto.setText(curDate);
					}
					else
					{
						but_datefrom.setText(curDate);
					}
						
					ckb_w1.setChecked(false);
					ckb_w2.setChecked(false);
					ckb_w3.setChecked(false);
					ckb_hotspots.setChecked(false);
					
					//Clear everything except boundaries 
					if(mMap != null)
					{
						mMap.clear();
						getMohs();
					}

				}
			}

		};
		
		new DatePickerDialog(context, d, mobiCalendar.get(Calendar.YEAR),mobiCalendar.get(Calendar.MONTH),mobiCalendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	

	
	//-----------------------------------------------------------//
	
	//Note: data is directly getting from the CMC-PHI-Epid server
	//--------------------------------------------------------------------------
	// TASK: Get Hotspot Data //////////////////////////////////////////////////
	
	
	private void getHotspotData(String fromdate, String todate) {

		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String username = pref.getString("username", "defv");

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("username", username); //This will ignore by the server
			obj.putOpt("strfrom", fromdate);
			obj.putOpt("strto", todate);

			String jString = obj.toString();
			
			if(GlobalMethods.validateString(url_cmcf_server_hotspots))
			{
				String[] data = { jString, url_cmcf_server_hotspots };

				new downloadHotspots(data).execute();
			}

		} catch (JSONException ex) {
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}
	}

	private class downloadHotspots extends AsyncTask<String, Void, String> {

		String[] data;
		
		public downloadHotspots(String[] data)
		{
			this.data = data;
		}
		
		@Override
		protected void onPreExecute() {

			progressDialog = new Dialog(context);
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText("Downloading hotspots reports...");
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));
			
			progressDialog.show();	
		}

		@Override
		protected String doInBackground(String... params) {

			return HttpUpload.uploadData(data[0], data[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
	 		try 
			{  
	 		  	int idxEqual = result.indexOf(":");
		        if(idxEqual >= 0)
		        {
		            String strName = result.substring(0, idxEqual);
		            String strJson = result.substring(idxEqual+1, result.length()-1);

	            	Gson gson = new Gson();
	            	HotspotsEpidPojo[] hps = gson.fromJson(strJson, HotspotsEpidPojo[].class);
		            	
	            	 if(hps != null && hps.length>0)
	            	 {
	            		 //Commented to give priority to the reports
	            		 LatLngBounds.Builder builder = new LatLngBounds.Builder(); //To auto-fit the map to show all hotspots.
	            		 
	            		 for(HotspotsEpidPojo hp : hps)
	            		 {
		            		// Add circles to the map //////////////////
	            			 mMap.addCircle(new CircleOptions().center(
		            				new LatLng(hp.getLat(), hp.getLng()))
		            				.radius(hp.getRad())
		            				.strokeColor(Color.parseColor(hp.getStroke())).strokeWidth(0)
		            				.fillColor(Color.parseColor(hp.getFill())));
		            			            		
	            			 builder.include(new LatLng(hp.getLat(), hp.getLng()));
	            		 }
	            		 
	            		//Show all circles in the map
	         			LatLngBounds bounds = builder.build();
	         			int padding = 0; //Offset from edges of the map in pixels
	         			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
	         			mMap.animateCamera(cu); //Move the camera
	         			
	         			Toast.makeText(context, hps.length+" hotspot reports.", Toast.LENGTH_LONG).show();
	            	 }
	            	 else
	            	 {
	            		 Toast.makeText(context, "No hotspot data to show!", Toast.LENGTH_LONG).show();
	            	 }
		        }
			
			} 
			catch (Exception ex) 
			{
				Log.e(TAG, " " + ex.getMessage());
			}
			
		}
	}
	
	//--------------------------------------------------------------------------------------
	
}
