package org.cosmic.mobuzz.general.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

//Methods that used globally inside the project
public class GlobalMethods {

	//Set the font
	public static Typeface getTypeface(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "gill_sans_mt_regular.ttf");
	}
	
	//Set font for different languages
	public static Typeface getTypeface(Context context, String lang) {
		
		if(("sinhala").equalsIgnoreCase(lang))
		{
			return Typeface.createFromAsset(context.getAssets(), "iskolapota.ttf");
		}
		else if(("tamil").equalsIgnoreCase(lang))
		{
			return Typeface.createFromAsset(context.getAssets(), "thibus24s.ttf");
		}
		else
		{
			return Typeface.createFromAsset(context.getAssets(), "gill_sans_mt_regular.ttf");
		}
		
	}

	//Null or empty string
	public static boolean validateString(String value) {
		if ((value != null) && (!("").equals(value))) {
			return true;
		} else {
			return false;
		}
	}

	//Get the device screen size
	public static Point getDisplaySize(Activity activity) {
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point(display.getWidth(), display.getHeight());
		return size;
	}

	/**
	 * @param image
	 *            -Height
	 * @param imgage
	 *            -Width
	 * @param view
	 *            -Height
	 * @param view
	 *            -Width
	 * @return in-sample-size
	 * 
	 *         Calculate the in-sample-size for the image and view
	 */
	//Get the image resize ratio
	public static int getInSampleSize(int imgHeight, int imgWidth, int viewHeight, int viewWidth) {
		int inSampleSize = 1;

		viewHeight = viewHeight > GlobalVariables.MAX_IMAGE_DIMENSIONS ? GlobalVariables.MAX_IMAGE_DIMENSIONS : viewHeight;
		viewWidth = viewWidth > GlobalVariables.MAX_IMAGE_DIMENSIONS ? GlobalVariables.MAX_IMAGE_DIMENSIONS : viewWidth;
		
		// Do only if image and view has visible parameters.
		if ((imgHeight > 0) && (imgWidth > 0) && (viewHeight > 0) && (viewWidth > 0)) {
			if (imgHeight > viewHeight || imgWidth > viewWidth) {

				// Calculate ratios of height and width to requested height and width
				final int heightRatio = (int) Math.ceil((float) imgHeight / (float) viewHeight);
				final int widthRatio = (int) Math.ceil((float) imgWidth / (float) viewWidth);

				// Choose the smallest ratio as inSampleSize value, this will guarantee a final image with both dimensions larger than or equal to the requested height and width.
				inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
			}
		}

		return inSampleSize;
	}

	/**
	 * @param imgUri
	 * @return image height and width
	 * 
	 *         Returns image dimensions from a absolute path
	 */
	//Get the uri image size
	public static Point getImageSize(Uri imgUri) {
		if (imgUri == null) {
			return null;
		} else {
			// Read image dimensions without loading it.
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imgUri.getPath(), options);
			int imgWidth = options.outWidth;
			int imgHeight = options.outHeight;

			return new Point(imgHeight, imgWidth);
		}
	}

	//Create MD5 code
	public static String makeMD5(String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex(md.digest((message.trim()).getBytes(System.getProperty("file.encoding"))));
		} catch (Exception e) {
			return null;
		}
	}

	//Convert byte array to hexadecimal string
	public static String hex(byte[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
		}
		return sb.toString();
	}

	//Get the current time stamp
	public static String getTimestamp() {
		DateFormat fmtDateAndTime = new SimpleDateFormat(GlobalVariables.SIMPLE_DATE_FORMAT);
		Date date = new Date();
		return fmtDateAndTime.format(date.getTime());
	}

	//Clear memory
	public static void cleanMemory() {
		System.gc();
	}

	//Apply font for components in a view group
	public static void applyFontRecursively(ViewGroup parent, Typeface font) {

		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ViewGroup) {
				applyFontRecursively((ViewGroup) child, font);
			} else if (child != null) {

				if (child instanceof TextView) {
					((TextView) child).setTypeface(font);
				} else if (child instanceof Button) {
					((Button) child).setTypeface(font);
				}
			}
		}
	}

	//Get unique id for the device & android-installation
	public static String getUniqueId(TelephonyManager tm, Context context) {
		String deviceId = "dev_uuid";

		String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		deviceId = deviceUuid.toString();

		return deviceId;
	}

	//Get the device's model name
	public static String getDeviceName() {
		try {
			String manufacturer = Build.MANUFACTURER;
			String model = Build.MODEL;
			if (model.startsWith(manufacturer)) {
				return model;
			} else {
				return manufacturer + " " + model;
			}
		} catch (Exception ex) {
			return "";
		}
	}
	
	//Change the divice's locale
	public static Locale locale;
	public static void changeLang(Activity activity, String lang)
	{
	    if (lang.equalsIgnoreCase(""))
	     return;
	    locale = new Locale(lang);
	    Locale.setDefault(locale);
	    android.content.res.Configuration config = new android.content.res.Configuration();
	    config.locale = locale;
	    activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
	}
	
}
