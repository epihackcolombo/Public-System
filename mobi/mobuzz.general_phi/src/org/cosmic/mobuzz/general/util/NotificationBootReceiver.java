package org.cosmic.mobuzz.general.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.Toast;

public class NotificationBootReceiver extends BroadcastReceiver{

	private SharedPreferences pref;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
        	
        	pref = context.getSharedPreferences("AppNotification", 0);
        	int hour = pref.getInt("hour", -1);
        	int minute = pref.getInt("minute", -1);
        	
        	if(hour < 0)
        	{
        		hour = 10; //10 o'clock in the morning
        	}
        	
        	if(minute < 0)
        	{
        		minute = 0;
        	}
        	
        	Toast.makeText(context, "Starting Mo-Buzz Notification Service!", Toast.LENGTH_LONG).show();
        	
        	
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTimeInMillis(System.currentTimeMillis());
        	calendar.set(Calendar.HOUR_OF_DAY, hour);
        	calendar.set(Calendar.MINUTE, minute);

        	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        	Intent startServiceIntent = new Intent(context, NotificationAlarmReceiver.class);
	       	PendingIntent pendingInt = PendingIntent.getBroadcast(context, 0, startServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        	
        	//Set to fire daily
	       	alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingInt);

        }
		
	}

}
