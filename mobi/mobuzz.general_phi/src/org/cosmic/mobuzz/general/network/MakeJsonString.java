package org.cosmic.mobuzz.general.network;

import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;

public class MakeJsonString {

	//create json string for user log in
	public String forLogin(Context context,String userName, String password, String time_stamp){

		TelephonyManager tMngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		JSONObject obj = new JSONObject();

		try {
			obj.put("user",userName);
			obj.put("password", password);
			obj.put("imei",tMngr.getSimSerialNumber());
			obj.put("m_mobile",tMngr.getLine1Number());
			obj.put("uudid", tMngr.getDeviceId());
			obj.put("time_stamp", time_stamp);
			obj.put("model", GlobalMethods.getDeviceName());
		} catch (JSONException e) {
			return null; //"ERRO in JSON";
		}
		return obj.toString();

	}

	//create json string for user registration
	public String forRegister(Context context, String[] userData){

		TelephonyManager tMngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		JSONObject obj = new JSONObject();

		try {
			obj.put("user", userData[6]); // user name
			obj.put("email", userData[1]);
			obj.put("p_mobile",userData[2]); // mobile number user entered
			obj.put("residence",userData[3]);
			obj.put("language",userData[4]);
			obj.put("gender",userData[5]);
			obj.put("name",userData[0]); // first name ,last name
			obj.put("password",userData[7] );
			
			obj.put("ward",userData[8] );

			obj.put("imei",tMngr.getSimSerialNumber());
			obj.put("mobile",tMngr.getLine1Number()); // sim number
			obj.put("uudid", tMngr.getDeviceId());
			obj.put("time_stamp", getTimestamp());
			obj.put("model", GlobalMethods.getDeviceName());
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}

		return obj.toString();

	}

	//create json string for user details update
	public String forProfileUpdate(String[] userData){

		JSONObject obj = new JSONObject();

		try {
			obj.put("user", userData[0]); // user name
			obj.put("email", userData[2]);
			obj.put("p_mobile",userData[3]); // mobile number user entered
			obj.put("residence",userData[5]);
			obj.put("language",userData[4]);
			obj.put("name",userData[1]); // first name ,last name
			obj.put("password",userData[6] );
			obj.put("newpassword", userData[7]);
			obj.put("time_stamp", userData[8]);
			obj.put("uudid", userData[9]);

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}

		return obj.toString();

	}

	//create json string for complaint without image
	public String forFormSubmit(String[] userData){

		JSONObject obj = new JSONObject();

		try {
			obj.put("image",userData[0]);
			obj.put("user",null);
			obj.put("mobile", null);
			obj.put("discption", null);
			obj.put("location", userData[5]);
			obj.put("date", null);
			obj.put("address",userData[1]);
			obj.put("remarks", userData[2]);
			obj.put("assess", userData[3]);
			obj.put("time_stamp", userData[4]);

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}

		return obj.toString();

	}

	private String getTimestamp() {
		return GlobalMethods.getTimestamp();
	}

	//create json string for complaint with image
	public String forFormSubmit(String strImage, String[] userData){

		JSONObject obj = new JSONObject();

		try {
			obj.put("image",strImage);
			obj.put("user",userData[6]);
			obj.put("mobile", userData[0]);
			obj.put("discption", null);
			obj.put("location", userData[5]);
			obj.put("date", getTimestamp());
			obj.put("address",userData[1]);
			obj.put("remarks", userData[2]);
			obj.put("assess", userData[3]);
			obj.put("time_stamp", userData[4]);
			obj.put("uudid", userData[7]);

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}

		return obj.toString();

	}



}
