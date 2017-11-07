package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class UiEducation extends Activity {

	private final String TAG = UiEducation.class.getName();

	ImageButton ibut_statistics, ibut_dengue, ibut_transmission, ibut_symptoms, ibut_treatment, ibut_prevention;
	RadioGroup radioGroup;

	Context context = UiEducation.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_education);

		//Initialization of components 
		ibut_statistics = (ImageButton) findViewById(R.id.ibut_statistics);
		ibut_dengue = (ImageButton) findViewById(R.id.ibut_dengue);
		ibut_transmission = (ImageButton) findViewById(R.id.ibut_transmission);
		ibut_symptoms = (ImageButton) findViewById(R.id.ibut_symptoms);
		ibut_treatment = (ImageButton) findViewById(R.id.ibut_treatment);
		ibut_prevention = (ImageButton) findViewById(R.id.ibut_prevention);

		EducationPages_Click();
		rdg_main_navigation();
		
		//for language translation
		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String pref_lng = pref.getString("language", "");
		
		//Set the language
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

		
		GlobalIO.writeLog(context, TAG, LogAction.START, true);
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
	
	//View specific pages when the buttons are clicked
	private void EducationPages_Click() {

		ibut_statistics.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UiEducation.this, UiEducationMainpage.class);
				intent.putExtra("startPage", 0);
				startActivity(intent);
			}
		});

		ibut_dengue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UiEducation.this, UiEducationMainpage.class);
				intent.putExtra("startPage", 1);
				startActivity(intent);
			}
		});

		ibut_transmission.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UiEducation.this, UiEducationMainpage.class);
				intent.putExtra("startPage", 2);
				startActivity(intent);
			}
		});

		ibut_symptoms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UiEducation.this, UiEducationMainpage.class);
				intent.putExtra("startPage", 3);
				startActivity(intent);
			}
		});

		ibut_treatment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UiEducation.this, UiEducationMainpage.class);
				intent.putExtra("startPage", 4);
				startActivity(intent);
			}
		});

		ibut_prevention.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UiEducation.this, UiEducationMainpage.class);
				intent.putExtra("startPage", 5);
				startActivity(intent);
			}
		});

	}

	//Manage main navigation buttons
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
					but_home();
					break;
				case R.id.rdb_profile:
					but_profile();
					break;
				case R.id.rdb_enquiry:
					but_info();
					break;
				default:
					break;
				}

			}
		});
	}


	private void but_home()
	{
		Intent intent = new Intent(UiEducation.this, UiMain.class);
		intent.putExtra("startNavigation", 1);
		startActivity(intent);
		finish();
	}

	private void but_profile()
	{
		Intent intent = new Intent(UiEducation.this, UiMain.class);
		intent.putExtra("startNavigation", 2);
		startActivity(intent);
		finish();
	}


	private void but_info()
	{
		Intent intent = new Intent(UiEducation.this, UiMain.class);
		intent.putExtra("startNavigation", 3);
		startActivity(intent);
		finish();
	}

}
