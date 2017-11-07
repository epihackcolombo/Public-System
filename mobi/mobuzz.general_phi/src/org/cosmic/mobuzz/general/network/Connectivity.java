package org.cosmic.mobuzz.general.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {
	
	//check whether at least one connection mode (wifi, 3g, etc) is enable
	public static boolean NetworkConnectivity(Context context, boolean popup)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		
		if(netInfo != null && netInfo.isConnected()){
			return true;
		}
		else
		{		
			if(popup)
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("No active network connection");
				alertDialogBuilder.setMessage("Please check your mobile data or Wi-Fi network status and try again");
				alertDialogBuilder.setNeutralButton("OK",new DialogInterface.OnClickListener() 	{
					public void onClick(DialogInterface dialog,int id) {
						
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
			
			return false;
		}
	}
	
	// Note(optional): add a hello call to the server to make sure that connectivity is available

}
