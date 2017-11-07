package org.cosmic.mobuzz.general.util;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.ui.UiMap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationAlarmReceiver extends BroadcastReceiver{

	//Show a notification for check complaints and link the notification to the report-map activity	
	@Override
    public void onReceive(Context context, Intent intent) {
        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_bw)
                .setContentTitle("Mo-Buzz Notification")
                .setContentText("Please check for new complaints!");
        
        Intent resultIntent = new Intent(context, UiMap.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
	    stackBuilder.addParentStack(UiMap.class);
	    stackBuilder.addNextIntent(resultIntent);
	    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
	    mBuilder.setContentIntent(resultPendingIntent);
	    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    mNotificationManager.notify(9011, mBuilder.build());
    }
	
}
