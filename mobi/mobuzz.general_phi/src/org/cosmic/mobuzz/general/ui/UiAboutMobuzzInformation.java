package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

public class UiAboutMobuzzInformation extends Activity {

	Context context = UiAboutMobuzzInformation.this;
	private Typeface font;

	TextView txt_about_mobuzz_title1, txt_about_mobuzz_title2;

	private final String TAG = UiAboutMobuzzInformation.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_about_mobuzz_information);
	

		//for language translation
		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String pref_lng = pref.getString("language", "");
		
		if(("sinhala").equalsIgnoreCase(pref_lng))
		{
			font = GlobalMethods.getTypeface(context, "sinhala");
		}
		else if(("tamil").equalsIgnoreCase(pref_lng))
		{
			font = GlobalMethods.getTypeface(context, "tamil");
		}
		else
		{
			font = GlobalMethods.getTypeface(context);
		}
		
		ViewGroup view = (ViewGroup)getWindow().getDecorView();
		GlobalMethods.applyFontRecursively(view, font);
		
		//Initialization of components
		txt_about_mobuzz_title1 = (TextView) findViewById(R.id.txt_about_mobuzz_title1); 
		//txt_about_mobuzz_title2 = (TextView) findViewById(R.id.txt_about_mobuzz_title2); 
		
		txt_about_mobuzz_title1.setTypeface(font, Typeface.BOLD);
		//txt_about_mobuzz_title2.setTypeface(font, Typeface.BOLD);
		
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
	
}
