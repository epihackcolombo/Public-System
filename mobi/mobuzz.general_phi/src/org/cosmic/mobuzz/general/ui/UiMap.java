package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;

import com.google.android.gms.maps.GoogleMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class UiMap extends Activity {


	private final String TAG = UiEducation.class.getName();

	ImageButton ibut_reports, ibut_hotspots;
	RadioGroup radioGroup;

	PopupDialogWidget pdw;
	
	Context context = UiMap.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_map);

		//Initialization of components
		ibut_hotspots = (ImageButton) findViewById(R.id.ibut_map_hotspots);
		ibut_reports = (ImageButton) findViewById(R.id.ibut_map_reports);

		MapPages_Click();
		rdg_main_navigation();
		
		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_ui_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
	
			case R.id.mapReminder:
				
				Intent intent = new Intent(context, UiNotification.class);
				startActivity(intent);
	
				return true;
	
			default:
				return false;

		}

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
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == 4) {
	    	RadioButton rdb_home = (RadioButton) findViewById(R.id.rdb_home);
	    	rdb_home.setChecked(true);
	    }
	}
	
	private void MapPages_Click() {

		ibut_hotspots.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (isNetworkAvailable()) {
					Intent intent = new Intent(context, UiMapHotspots.class);
					startActivity(intent);
				} else {
					popdNoInternet();
				}
			}
		});

		ibut_reports.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (isNetworkAvailable()) {
					Intent intent = new Intent(context, UiMapReports.class);
					startActivity(intent);
				} else {
					popdNoInternet();
				}
			}
		});

	}

	//Update navigation
	private void rdg_main_navigation()
	{
		radioGroup = (RadioGroup) findViewById(R.id.rdg_main_navigation);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				switch (checkedId)
				{
				case R.id.rdb_home:
					//but_home(); //Already in home
					break;
				case R.id.rdb_profile:
					but_profile();
					break;
				case R.id.rdb_enquiry:
					but_info();
					break;
				case R.id.rdb_settings:
					but_settings();
					break;
				default:
					break;
				}

			}
		});
	}

	//UI update
	private void but_home()
	{
		Intent intent = new Intent(context, UiMain.class);
		intent.putExtra("startNavigation", 1);
		startActivity(intent);
		finish();
	}

	//UI update
	private void but_profile()
	{
		Intent intent = new Intent(context, UiMain.class);
		intent.putExtra("startNavigation", 2);
		startActivity(intent);
		finish();
	}

	//UI update
	private void but_info()
	{
		Intent intent = new Intent(context, UiMain.class);
		intent.putExtra("startNavigation", 3);
		startActivity(intent);
		finish();
	}	
	
	//UI update
	private void but_settings()
	{
		Intent intent = new Intent(context, UiNotification.class);
		startActivityForResult(intent, 4);
	}	
	
	

	//-------------------- Supporting functions -----------------------//

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

}
