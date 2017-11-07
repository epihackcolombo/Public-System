package org.cosmic.mobuzz.general;

import org.cosmic.mobuzz.general.ui.UiMain;
import org.cosmic.mobuzz.general.ui.UiProfileLogin;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class SplashActivity extends Activity {

	private String TAG = SplashActivity.class.getName();
	Context context = SplashActivity.this;
	boolean loginStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		//display splash screen
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				setSettings();
			}
		}, GlobalVariables.SPLASH_DISPLAY_LENGHT);

	
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
	
	//check whether the user is log-in
	//if not, go to login page. otherwise go to the main-navigation page
	private void setSettings() {
		// Set the log-in flag
		SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalVariables.MY_PREF, 0);
		loginStatus = pref.getBoolean("login", false);

		if (loginStatus) { //already login
			Intent intent = new Intent(context, UiMain.class);
			startActivity(intent);
			
			GlobalIO.writeLog(context, TAG, " [" + pref.getString("username", "") + "_relogin_auto_succeed]; ", true);	
		} else {
			Intent intent = new Intent(context, UiProfileLogin.class);
			startActivity(intent);
			
			GlobalIO.writeLog(context, TAG, " [" + pref.getString("username", "") + "_relogin_redirect]; ", true);	
		}

		finish();
	}

}
