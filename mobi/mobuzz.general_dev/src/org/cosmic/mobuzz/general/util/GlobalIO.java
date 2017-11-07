package org.cosmic.mobuzz.general.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

/**
 * Handle data read/write to the device storage
 */
//This data is for research purpose only. Disable for now to study already collected data, and then decide whether collecting more or not.
public class GlobalIO {

	//Write data-line to the device's storage
	public static void writeLog(Context context, String className, String logAction, boolean isAppend) {

		/*
		String writingText = logAction + "," + className + "; ";
		
		try {
			setConfig(context, writingText, isAppend);
		} catch (IOException e) {
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e("GlobalIO", e.getMessage());
			}
		}
		*/
	}

	//Check whether configuration file exists
	public static boolean isConfig(Context context) {
		/*
		File confFile = context.getFileStreamPath(GlobalVariables.FILE_CONF_FILE);
		return confFile.exists();
		*/
		return false;
	}

	//Get configuration file content
	public static String getConfig(Context context) throws IOException {
		String confData = "";
		
		/*
		FileInputStream fis = context.openFileInput(GlobalVariables.FILE_CONF_FILE);
		InputStreamReader in = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(in);
		String thisLine = "";
		while ((thisLine = br.readLine()) != null) {
			confData = confData + thisLine;
		}
		*/
		return confData;
	}

	//Write to the configuration file
	public static void setConfig(Context context, String strData, boolean isAppend) throws IOException {
		/*
		if (strData != null && strData.length() > 1) {

			FileOutputStream fos;
			
			String strDate = GlobalMethods.getTimestamp();

			if (isAppend) {
				fos = context.openFileOutput(GlobalVariables.FILE_CONF_FILE, Context.MODE_APPEND);
			} else {
				fos = context.openFileOutput(GlobalVariables.FILE_CONF_FILE, Context.MODE_PRIVATE);
			}

			fos.write((strDate+","+strData).getBytes());

			fos.flush();
			fos.close();
		}
		*/
	}

}
