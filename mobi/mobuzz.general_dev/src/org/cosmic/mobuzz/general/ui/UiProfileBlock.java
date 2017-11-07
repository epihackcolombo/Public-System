package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class UiProfileBlock extends Activity {

	private final String TAG = UiProfileBlock.class.getName();
	Context context = UiProfileBlock.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_profile_block);
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
		
		Intent intent = new Intent(context, UiProfileLogin.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
		GlobalMethods.cleanMemory();
	}
	
}
