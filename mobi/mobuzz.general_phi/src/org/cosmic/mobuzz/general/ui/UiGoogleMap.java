package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.util.CMCRegions;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class UiGoogleMap extends android.support.v4.app.FragmentActivity {

	private GoogleMap mMap;
	private final Context context = UiGoogleMap.this;
	private final String TAG = UiGoogleMap.class.getName();
	private CMCRegions cmcRegions;

	Typeface font;

	private LatLng latLng;
	PopupDialogWidget pdw, pdw_gpscmc;
	private boolean isFirst = false;
	private Double latitude = 0.0;
	private Double longitude = 0.0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_google_map);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		Intent intent = getIntent();

		cmcRegions = new CMCRegions();
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
	public void onBackPressed() {
		super.onBackPressed();

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
		
		if (pdw_gpscmc != null && pdw_gpscmc.isShowing()) {
			pdw_gpscmc.dismiss();
		}	
		
		
		Intent returnIntent = new Intent();
		try
		{
			if(latitude == null || longitude == null || latitude == 0 || longitude == 0)
			{
				returnIntent.putExtra("newlocation", "");
			}
			else
			{
				returnIntent.putExtra("newlocation", latitude.toString()+","+longitude.toString());
			}
			setResult(RESULT_OK,returnIntent);
		}
		catch(Exception ex)
		{
			setResult(RESULT_CANCELED, returnIntent);
		}

		GlobalIO.writeLog(context, TAG, LogAction.END, true);
		finish();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void setUpMap() {
		
		if (mMap == null) {
			MapsInitializer.initialize(context);

			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map)).getMap();

			CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).build();
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			mMap.setMyLocationEnabled(true);
			mMap.addPolygon(new PolygonOptions().addAll(cmcRegions.getCMCRegion()).fillColor(Color.parseColor(GlobalVariables.COLOR_REGION_OVERLAY))).setStrokeWidth(3);

			if (mMap != null) {

				mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

					@Override
					public void onMyLocationChange(Location arg0) {
						
						latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
						mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

						latitude = arg0.getLatitude();
						longitude = arg0.getLongitude();

						if (!isFirst) {
							isFirst = true;

							if (latitude == 0 || longitude == 0) {
								// nothing to do
							} else if (cmcRegions.isInCMC(latLng)) { //CMC jurisdiction
								// nothing to do
							} else {
								popdUnclearGPS(getString(R.string.msg_gps_no_para3));
							}
						}
					}
				});
			}

		}	
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

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
		
		if (pdw_gpscmc != null && pdw_gpscmc.isShowing()) {
			pdw_gpscmc.dismiss();
		}

		if (mMap != null) {
			mMap.setMyLocationEnabled(false);
		}

		GlobalIO.writeLog(context, TAG, LogAction.PAUSE, true);
	}

	// --------------------------------------------------------
	//Show pop-up messages
	
	private void popdUnclearGPS(String content) {

		pdw_gpscmc = new PopupDialogWidget(this, this.getString(R.string.msg_gps_no_title2), content, this.getString(R.string.msg_common_usecurrent), this.getString(R.string.msg_common_settings), false);
		pdw_gpscmc.show();

		pdw_gpscmc.okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw_gpscmc.dismiss();
			}
		});

		pdw_gpscmc.noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw_gpscmc.dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);

				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}
		});

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
	}
}
