package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.NotificationBootReceiver;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class UiNotification extends android.support.v4.app.FragmentActivity {

	
	private final Context context = UiNotification.this;
	final String TAG = UiNotification.class.getName();
	private Typeface font;

	TimePicker timePicker;
	private SharedPreferences pref;
	
	TextView txtv_title1, txtv_title2, txtv_title3;
	
	PopupDialogWidget pdw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_notification);

		timePicker = (TimePicker) findViewById(R.id.timp_notification);
		
		
		font = GlobalMethods.getTypeface(context);
		
		ViewGroup view = (ViewGroup)getWindow().getDecorView();
		GlobalMethods.applyFontRecursively(view, font);

		txtv_title1 = (TextView) findViewById(R.id.txtv_title1);
		txtv_title2 = (TextView) findViewById(R.id.txtv_title2);
		txtv_title3 = (TextView) findViewById(R.id.txtv_title3);
		
		txtv_title1 .setTypeface(font, Typeface.BOLD);
		txtv_title2 .setTypeface(font, Typeface.BOLD);
		txtv_title3 .setTypeface(font, Typeface.BOLD);
		
		checkAlarm();
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return false;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		if (pdw!=null && pdw.isShowing()) {
			pdw.dismiss();
		}
		
		finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (pdw!=null && pdw.isShowing()) {
			pdw.dismiss();
		}
	}
	
	private void checkAlarm()
	{
		pref = getSharedPreferences("AppNotification", 0);
		int hour = pref.getInt("hour", -1);
		int minute = pref.getInt("minute", -1);
		
		if(hour>=0 && minute>=0)
		{
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(minute);
		}
	}

	
	public void startAlarm(View view) {
		
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();

		pref = getSharedPreferences("AppNotification", 0);
		
		Editor editor = pref.edit();
		editor.putInt("hour", hour);
		editor.putInt("minute", minute);
		editor.commit();
		
		
		ComponentName receiver = new ComponentName(context, NotificationBootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
		
		pdw = new PopupDialogWidget(this, "Please reboot your device!", "Notification will start, after rebooting your device.", this.getString(R.string.msg_common_ok), null, false);
		pdw.show();
	}

	
	public void cancelAlarm(View view) {

		ComponentName receiver = new ComponentName(context, NotificationBootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);

		pdw = new PopupDialogWidget(this, "Please reboot your device!", "Notification will stop, after rebooting your device.", this.getString(R.string.msg_common_ok), null, false);
		pdw.show();
	}

	
}
