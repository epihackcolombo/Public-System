package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

public class UiAboutMobuzzLicense extends Activity {

	Context context = UiAboutMobuzzLicense.this;
	private Typeface font;
	private final String TAG = UiAboutMobuzzLicense.class.getName();

	TextView txt_pp_mobuzz_title1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_about_mobuzz_license);

		//Initialization of components 
		font = GlobalMethods.getTypeface(context);
		
		ViewGroup view = (ViewGroup)getWindow().getDecorView();
		GlobalMethods.applyFontRecursively(view, font);
		
		txt_pp_mobuzz_title1 = (TextView) findViewById(R.id.txt_pp_mobuzz_title1);
		txt_pp_mobuzz_title1.setTypeface(font, Typeface.BOLD);


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
