package org.cosmic.mobuzz.general;

import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Context context = MainActivity.this;

	Button but_activate;
	EditText edt_code;
	TextView txt_activate_msg;
	Typeface font;

	private SharedPreferences pref; //For activation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		//Initialization of components 
		font = GlobalMethods.getTypeface(context);
		
		ViewGroup view = (ViewGroup)getWindow().getDecorView();
		GlobalMethods.applyFontRecursively(view, font);
		
		
		but_activate = (Button) findViewById(R.id.but_activate);
		edt_code = (EditText) findViewById(R.id.edt_code);
		txt_activate_msg = (TextView) findViewById(R.id.txt_activate_msg);

		font = GlobalMethods.getTypeface(context);
		txt_activate_msg.setTypeface(font);
		edt_code.setTypeface(font);
		but_activate.setTypeface(font);

		
		// Activate the app, temparary lock, hence hardcord value
		/*
		pref = getSharedPreferences("ActivateApp", 0);

		if (pref.getBoolean("activatedapp", false)) {
		
			Intent intent = new Intent(context, SplashActivity.class);
			startActivity(intent);
			finish();

		}
		else {

			but_activate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String str_code = edt_code.getText().toString().trim();

					if ((GlobalVariables.ACTIVATION_CODE).equals(str_code)) {
						// Setup any variables for the application.
						Intent intent = new Intent(context, SplashActivity.class);
						startActivity(intent);

						Editor editor = pref.edit();
						editor.putBoolean("activatedapp", true);
						editor.commit();

						finish();
					} else {
						edt_code.setError("Wrong activation code!");
					}
				}
			});
		}
		*/
		//Login without activating, please comment this part and un-comment above part to go through activation
		Intent intent = new Intent(context, SplashActivity.class);
		startActivity(intent);
		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onBackPressed() {
		//quit
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
