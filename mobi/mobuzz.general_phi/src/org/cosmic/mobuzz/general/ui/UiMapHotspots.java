package org.cosmic.mobuzz.general.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.pojo.HotspotsClusterPojo;
import org.cosmic.mobuzz.general.pojo.MOHPojo;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.util.CMCRegions;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

public class UiMapHotspots extends android.support.v4.app.FragmentActivity implements OnItemSelectedListener, OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener {

	private GoogleMap mMap;
	private LatLng latLng;
	private Dialog progressDialog;
	private final Context context = UiMapHotspots.this;
	final String TAG = UiMap.class.getName();
	private TableRow tblr_colorcode;
	private TextView txt_left, txt_right;
	private final String hotspotColor[] = { "#88B40404", "#7FFF0000", "#88F78181", "#88F6CECE", "#88F8E0E0", "#00FBEFF2" };
	private Typeface font;
	private String url_map;// = "http://pubbapp.comze.com/public_map.php";
	CMCRegions cmcRegions;
	PopupDialogWidget pdw;
	MOHPojo[] mohp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_map_hotspots);

		url_map = getString(R.string.url_server_port) + getString(R.string.url_map_data);

		//Initialization of components
		txt_left = (TextView) findViewById(R.id.txt_left);
		txt_right = (TextView) findViewById(R.id.txt_right);
		tblr_colorcode = (TableRow) findViewById(R.id.tblr_colorcode);

		font = GlobalMethods.getTypeface(context);
		txt_left.setTypeface(font);
		txt_right.setTypeface(font);

		latLng = GlobalVariables.GPS_COLOMBO; //Default GPS coordinates for Colombo

		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext()); //Check google play availability
		if (status == ConnectionResult.SUCCESS) {
			setUpMap();

		} else {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode); //Show error message if google play is not available 
			dialog.show();
		}

		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_ui_map_hotspot, menu);
		return true;
	}

	//Change the map types: NORMAL, SATELLITE or HYBRID
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
		
		if(progressDialog!=null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		if(mMap != null)
		{
			mMap.setMyLocationEnabled(false); //Disable to save power
		}
		
		GlobalIO.writeLog(context, TAG, LogAction.PAUSE, true);
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

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

			if (latLng != null) {
				CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).build();
				mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

				mMap.setOnMarkerClickListener(this);
				mMap.setOnInfoWindowClickListener(this);
				mMap.setOnMarkerDragListener(this);

				mMap.setMyLocationEnabled(true);

				getMohInfo(); //Manage MOH details
				getHotspots(); //Get hotspots details from server

			}
		}
	}

	//Get hotspots details from server
	private void getHotspots() {

		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String username = pref.getString("username", "defv");
		String time_stamp = pref.getString("time_stamp", "");
		String uudid = pref.getString("uudid", "");

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("user", username);
			obj.putOpt("time_stamp", time_stamp);
			obj.putOpt("uudid", uudid);

			String jString = obj.toString();

			String[] loginData = { jString, url_map };
			new FetchMapData().execute(loginData);

		} catch (JSONException ex) {
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}
	}

	//Draw ward boundaries
	private void getWards() {

		cmcRegions = new CMCRegions();

		for (int i = 1; i <= 47; i++) {
			List<LatLng> wardCoordinates = cmcRegions.getCMCWard(i);

			if (wardCoordinates != null && wardCoordinates.size() > 0) {
				mMap.addPolygon(new PolygonOptions().addAll(wardCoordinates).fillColor(Color.parseColor(hotspotColor[5]))).setStrokeWidth(1);
			}
		}
	}

	//Draw MOH boundaries
	private void getMohs() {

		cmcRegions = new CMCRegions();

		for (int i = 1; i <= 6; i++) {
			List<LatLng> mohCoordinates = cmcRegions.getCMCMoh(i);

			if (mohCoordinates != null && mohCoordinates.size() > 0) {
				mMap.addPolygon(new PolygonOptions().addAll(mohCoordinates).fillColor(Color.parseColor(hotspotColor[5]))).setStrokeWidth(2);
			}
		}
	}

	//Manage MOH pop-up details
	private void getMohInfo() {
		cmcRegions = new CMCRegions();

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				if (GlobalMethods.validateString(arg0.getTitle())) // if marker source is clicked
				{
					popdMarkerInfo(arg0.getTitle()); // if marker source is clicked
				}

				return true;
			}

		});

		getMohs();
	}

	//-------------------- Fetch map data -----------------------//
	//Get data from server
	private class FetchMapData extends AsyncTask<String, Void, String> {

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

		if (GlobalMethods.validateString(result)) {

			try {

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
						}else if (("sql_no_data").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							Toast.makeText(context, getString(R.string.toast_hotspot_data_no), Toast.LENGTH_LONG).show(); 
						} else {

							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}

					} else {

						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				} //-----------------------------------------
				else {

					String[] jsons = result.split("&"); //Split hotspot and MOH data

					//Showing hotspots
					try
					{
						if (GlobalMethods.validateString(jsons[1])) {
	
							Gson gson = new Gson();
							HotspotsClusterPojo[] hscp = gson.fromJson(jsons[1], HotspotsClusterPojo[].class);
	
							if (hscp.length > 0) {
	
								if (GlobalMethods.validateString(hscp[0].getFromdate()) && hscp[0].getToyear() > 0 && hscp[0].getToweek() >= 0) {
									String date_to = "";
	
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

									Calendar cal = Calendar.getInstance();
	
									int this_week = cal.get(Calendar.WEEK_OF_YEAR);
									int this_year = cal.get(Calendar.YEAR);
	
									if (this_year == hscp[0].getToyear() && this_week == hscp[0].getToweek())//cluster is created for this week
									{
										date_to = sdf.format(cal.getTime());
									} else {
	
										if (GlobalMethods.validateString(hscp[0].getTodate())) {
											date_to = hscp[0].getTodate();
										}
									}
	
									if (GlobalMethods.validateString(date_to)) {
										txt_left.setText("From: " + hscp[0].getFromdate()); 
										txt_right.setText("To: " + date_to);

										tblr_colorcode.setVisibility(View.VISIBLE);
									}
	
								}

								
								// Add circles to the map
								for (HotspotsClusterPojo hp : hscp) {
									mMap.addCircle(new CircleOptions()
													.center(new LatLng(hp.getLat(), hp.getLng()))
													.radius(hp.getRad())
													.strokeWidth(0)
													.strokeColor(Color.parseColor(hp.getColor()))
													.fillColor(Color.parseColor(hp.getColor())));
								
								}
	
							} else {
								//No relevant data, nothing to do
							}

	
						} 
						else //Unexpected data
						{
							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}
					}
					catch(Exception ex) //Exception from data
					{
						if(GlobalVariables.PRINT_DEBUG)
						{
							Log.e(TAG, " " + ex.getMessage());
						}
					}
					
					//Showing MOHs
					try
					{
						if (GlobalMethods.validateString(jsons[0])) {
	
							Gson gson = new Gson();
							mohp = gson.fromJson(jsons[0], MOHPojo[].class);
	
							if (mohp.length > 0) {
	
								//Add MOH details to the map
								for (MOHPojo mph : mohp) {

									mMap.addMarker(new MarkerOptions().position(cmcRegions.getMohLocation(mph.getMoh_location())).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_icon_marker_moh)).title(String.valueOf(mph.getId())));

								}
	
							} 
	
						} 
						else 
						{
							///No relevant data, nothing to do
						}
					}
					catch(Exception ex) //Exception from data
					{
						if(GlobalVariables.PRINT_DEBUG)
						{
							Log.e(TAG, " " + ex.getMessage());
						}
					}
					
					
				}

			} catch (Exception ex) { //Response exception
				
				if(GlobalVariables.PRINT_DEBUG)
				{
					Log.e(TAG, " " + ex.getMessage());
				}
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} else {
			//No data, nothing to do
		}

	}

	// --------------------------------------------------------
	//Show pop-up messages		
	
	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}

	private String callofficenum = "", calldoctornum = "";
	private void popdMarkerInfo(String title) {

		int i = -1;
		MOHPojo moh = null;
		
		try
		{
			i = Integer.parseInt(title); //Array location of the marker-details, passed from the google-marker (passed as text) click event

			if( i>-1 )
			{
				final Dialog dialog = new Dialog(context);
				dialog.getWindow();
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.xml_popup_markerinfo);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
		
				TextView txt_moh_title = (TextView) dialog.findViewById(R.id.txt_moh_title);
				TextView txt_moh_address = (TextView) dialog.findViewById(R.id.txt_moh_address);
				TextView txt_moh_tel = (TextView) dialog.findViewById(R.id.txt_moh_tel);
				TextView txt_moh_doc_name = (TextView) dialog.findViewById(R.id.txt_moh_doc_name);
				TextView txt_moh_doc_tel = (TextView) dialog.findViewById(R.id.txt_moh_doc_tel);
				ImageView imgv_moh = (ImageView) dialog.findViewById(R.id.imgv_moh);
		
				//Apply data to the UI components
				txt_moh_title.setText("MOH office - " + mohp[i].getDistrict_moh());
				txt_moh_address.setText(mohp[i].getMoh_address());
				txt_moh_tel.setText(mohp[i].getMoh_telephone());
				txt_moh_doc_name.setText(mohp[i].getMoh_doctor());
				txt_moh_doc_tel.setText(mohp[i].getMoh_doctor_telephone());
				imgv_moh.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(cmcRegions.getMohDetItem(i, 7), "drawable", getPackageName())));
		
				callofficenum = (mohp[i].getMoh_telephone()).replace(" ", "").replace("-", ""); //Remove spaces and "-" to pass the telephone-number to dialer 
				calldoctornum = (mohp[i].getMoh_doctor_telephone()).replace(" ", "").replace("-", ""); //Remove spaces and "-" to pass the telephone-number to dialer 
				
				LinearLayout lnlout_moh = (LinearLayout) dialog.findViewById(R.id.lnlout_moh);
				lnlout_moh.setOnClickListener(new OnClickListener() {
				    @Override           
				    public void onClick(View v) {
				    	Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:"+callofficenum)); //Pass the telephone-number to dialer
						startActivity(callIntent);
				    }
				});
				
				LinearLayout lnlout_moh_doc = (LinearLayout) dialog.findViewById(R.id.lnlout_moh_doc);
				lnlout_moh_doc.setOnClickListener(new OnClickListener() {
				    @Override           
				    public void onClick(View v) {
				    	Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:"+calldoctornum)); //Pass the telephone-number to dialer
						startActivity(callIntent);
				    }
				});				

				Button btn_msg_ok = (Button) dialog.findViewById(R.id.btn_msg_ok);
				btn_msg_ok.setText("OK");
				btn_msg_ok.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);
		
				btn_msg_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		
				dialog.show();
			}

		}
		catch(Exception ex)
		{
			//Nothing to do
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
}
