package org.cosmic.mobuzz.general.ui;

import java.util.Locale;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.pojo.ImagePojo;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;

import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.gson.Gson;

public class UiBreedingsiteHistoryDetails extends android.support.v4.app.FragmentActivity {

	private GoogleMap mMap;
	private final Context context = UiBreedingsiteHistoryDetails.this;
	private final String TAG = UiBreedingsiteHistoryDetails.class.getName();
	private String url_report_details;
	private Dialog progressDialog;
	Typeface font;

	String id, gps, address, date, ward, remarks, assessment, cmcmessage, imagepath;
	TextView txt_gps, txt_address, txt_date, txt_ward, txt_remarks, txt_assessment, txt_cmcmessage;
	ImageView imageViewBS;
	Button but_actimage, but_saveimage;
	int image = 0;
	LatLng latLng;
	Bitmap decodedByte;
	
	ImagePojo imgp;
	PopupDialogWidget pdw;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ui_breedingsite_history_details);

		//Initialization of components 
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		url_report_details = getString(R.string.url_server_port) + getString(R.string.url_report_details);

		font = GlobalMethods.getTypeface(context);
		
		ViewGroup view = (ViewGroup)getWindow().getDecorView();
		GlobalMethods.applyFontRecursively(view, font);
		
		Intent intent = getIntent();

		image = intent.getIntExtra("image", 0);

		id = intent.getStringExtra("id");
		gps = intent.getStringExtra("gps");
		address = intent.getStringExtra("address");
		date = intent.getStringExtra("date");
		ward = intent.getStringExtra("ward");
		remarks = intent.getStringExtra("remarks");
		assessment = intent.getStringExtra("assessment");
		cmcmessage = intent.getStringExtra("cmcmessage");
		imagepath = intent.getStringExtra("imagepath");

		txt_gps = (TextView) findViewById(R.id.txt_msgd_gps);
		txt_address = (TextView) findViewById(R.id.txt_msgd_address);
		txt_date = (TextView) findViewById(R.id.txt_msgd_date);
		txt_ward = (TextView) findViewById(R.id.txt_msgd_ward);
		txt_remarks = (TextView) findViewById(R.id.txt_msgd_remark);
		txt_assessment = (TextView) findViewById(R.id.txt_msgd_assessment);
		txt_cmcmessage = (TextView) findViewById(R.id.txt_msgd_cmcmessage);
		
		but_actimage = (Button) findViewById(R.id.but_actimage);
		but_saveimage = (Button) findViewById(R.id.but_saveimage);

		imageViewBS = (ImageView) findViewById(R.id.imageViewBS);

		//Apply data to the UI components
		setData();

		//If image is available, check whether image is already saved to the disk
		if (image > 0) {

			getDiskImage(imagepath);
		}

		if (GlobalMethods.validateString(gps)) {
			String[] gps_location = gps.split(",");

			if (gps_location.length >= 2) {
				float gps_lat = Float.parseFloat(gps_location[0].trim());
				float gps_lng = Float.parseFloat(gps_location[1].trim());

				if (gps_lat > 0.0 && gps_lng > 0.0) {
					latLng = new LatLng(gps_lat, gps_lng); // default GPS coordinates for Colombo

					int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
					if (status == ConnectionResult.SUCCESS) {
						setUpMap();

					} else {
						int requestCode = 10;
						Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
						dialog.show();
					}

				}

			}

		}
		
		but_actimage_Click();
		but_saveimage_Click();
		
		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		GlobalIO.writeLog(context, TAG, LogAction.END, true);
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		GlobalMethods.cleanMemory();
	}

	private void but_actimage_Click()
	{
		but_actimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setImage("actual");
			}
		});
	}
	
	
	//To get the image from the disc, if available
	private void getDiskImage(String imagepath)
	{
		try
		{

			if(GlobalMethods.validateString(imagepath))
			{
				
				//1. try to get full image
				//2. otherwise get the thumbnail
	
				String downloadImgName = imagepath.replace(GlobalVariables.IMAGE_THUMB_POSTFIX, "");// remove thumbnail post
				
				String imagePath = getGalleryImage(downloadImgName);
				
				if(!GlobalMethods.validateString(imagePath))
				{
					downloadImgName = downloadImgName + GlobalVariables.IMAGE_THUMB_POSTFIX;
					imagePath = getGalleryImage(downloadImgName);
				}
				
				if(GlobalMethods.validateString(imagePath))
				{
	
		    		 imageViewBS.setVisibility(View.VISIBLE);
		    		 
		    		 setGalleryImage(imagePath); //since this is from the image database, path validation is ignored
		    		 
		    		 but_saveimage.setVisibility(View.GONE);
		    		 
		    		 if(downloadImgName.contains(GlobalVariables.IMAGE_THUMB_POSTFIX))
		    		 {
		    			 but_actimage.setVisibility(View.VISIBLE);
		    		 }
		    		 else //full image is available
		    		 {
		    			 but_actimage.setVisibility(View.GONE);
		    		 }
				}
				else
				{
					setImage("thumbnail");
				}

			}
			else
			{
				//get the original image.
				setImage("thumbnail");
			}

		}
		catch (Exception ex)
		{
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}

	}

	private void setGalleryImage(String imagePath)
	{
		if (GlobalMethods.validateString(imagePath)) {

			//imageViewBS.setImageURI(Uri.parse(imagePath)); //this will resize the image & for xhdp. This image is too small
			
			decodedByte = BitmapFactory.decodeFile(imagePath);
			imageViewBS.setImageBitmap(decodedByte);
		}
	}
	
	
	private String getGalleryImage(String imageName)
	{

	     if( GlobalMethods.validateString(imageName))
	     {
		
			//Check whether the image had been download previously and in cache.
			try
			{
				
				String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.TITLE};
				Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				Cursor cursor = managedQuery( uri, projection, null, null, null);

				if(cursor.getCount()==0)
				{
				     cursor.close(); //No external storage card
				} 
				else if(cursor.getCount()>0)
				{    
					 
					 cursor.moveToFirst();
					 
					 do {

					     int columnIndex_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					     int columnIndex_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
					     
					     String imagePath = cursor.getString(columnIndex_data);
					     String imageTitle = cursor.getString(columnIndex_title);
					     
				    	 if(imageName.equals("/"+imageTitle)) // "/": in order to match with saving substring pattern. substring is not used here to avoid 1-length string errors.
				    	 {
				    		 cursor.moveToLast();
				    		 return imagePath;
				    	 }
						 
					 } while (cursor.moveToNext());

					 
					 if(GlobalVariables.PRINT_DEBUG)
					 {
						 Log.e(TAG, "cursor ends");
					 }

			    }
				
				return null;
			}
			catch(Exception ex)
			{
				 if(GlobalVariables.PRINT_DEBUG)
				 {
					 Log.e(TAG, " " + ex.getMessage());
				 }
				 
				 return null;
			}
			
	     }
	     else
	     {
	    	 return null;
	     }
	}
	
	private void but_saveimage_Click()
	{

		but_saveimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				try
				{
					
					if (imgp==null)
					{
						return;
					}
					
					String imagePath = getGalleryImage(imgp.getImg_name());
					
					if(GlobalMethods.validateString(imagePath))
					{
						//Image is already in the gallery.
						Toast.makeText(context, "Image is already in the gallery!", Toast.LENGTH_LONG).show();
					}
					else
					{
						//If image is available, save the showing image to the storage 
			    		 if(imageViewBS != null)
			    		 {
						    imageViewBS.setDrawingCacheEnabled(true);
						    decodedByte = imageViewBS.getDrawingCache();
						    String res = Images.Media.insertImage(context.getContentResolver(), decodedByte, imgp.getImg_name().substring(1), "");
						    
						    if(GlobalMethods.validateString(res))
						    {
						    	Toast.makeText(context, "Image was saved locally.", Toast.LENGTH_LONG).show();
						    	v.setVisibility(View.GONE);
						    }
						    else
						    {
						    	Toast.makeText(context, "Image could not be saved locally!", Toast.LENGTH_LONG).show();
						    }
			 
			    		 }
					}

				}
				catch(Exception ex)
				{
					if(GlobalVariables.PRINT_DEBUG)
					{
						Log.e(TAG, " " + ex.getMessage());
					}
				}

			}
		});
	}
	
	//Download image from the server. Send the size-flag to get the correct image.
	private void setImage(String imagesize) {


		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String username = pref.getString("username", "defv");
		String time_stamp = pref.getString("time_stamp", "");
		String uudid = pref.getString("uudid", "");

		JSONObject obj = new JSONObject();

		try {
			obj.putOpt("user", username);
			obj.putOpt("id", id);
			obj.putOpt("time_stamp", time_stamp);
			obj.putOpt("uudid", uudid);
			obj.putOpt("imagesize", imagesize);

			String jString = obj.toString();
			String[] data = { jString, url_report_details };

			new downloadImage().execute(data);

		} catch (JSONException ex) {
			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage());
			}
		}
	}

	//Set data to the UI components.
	private void setData() {

		txt_gps.setText(gps);
		txt_address.setText(address);
		txt_date.setText(date);
		txt_ward.setText(ward);
		txt_remarks.setText(remarks);
		txt_assessment.setText(assessment);
		txt_cmcmessage.setText(cmcmessage);
	}

	//Set up google-map fragment in UI
	private void setUpMap() {

		if (mMap == null) {
			MapsInitializer.initialize(context);
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map)).getMap();

			if (latLng != null) {
				CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).build();
				mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

				//mMap.addMarker(new MarkerOptions().position(latLng));
				mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_icon_marker_loc)));

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalIO.writeLog(context, TAG, LogAction.RESUME, true);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
		GlobalIO.writeLog(context, TAG, LogAction.PAUSE, true);
	}



	//---------------------- Network supporting function ----------------------//
	
	//Download the image form server.
	private class downloadImage extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			progress(getString(R.string.msg_downloading_para2));
		}

		@Override
		protected String doInBackground(String... params) {
			return HttpUpload.uploadDataCache(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
			
			responseToResult(result);
		}
	}

	public void responseToResult(String result) {

		try {
			if (GlobalMethods.validateString(result)) {

				//validation block ------------------------
				//error_net_connection - no route to host, can't find an active connection
				//error_net_other - network time-out or other connector exceptions
				//error_db_params - inappropriate parameters with the request
				//error_db_connect - database connection issue at server side
				//authentication_required - user need to be authenticate
				//authentication_expired - user session has expired
				
				if (result.contains("{'status':")) { // in order to support previous and new communication

					Gson gson = new Gson();
					ProfileLoginPojo plp = gson.fromJson(result, ProfileLoginPojo.class);

					if (GlobalMethods.validateString(plp.getStatus())) {

						if (("error_net_connection").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para1));
						}
						else if (("error_net_other").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
						
							popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para2));
						}
						else if (("error_db_params").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdRequestFailed(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));
						} else if (("error_db_connect").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdRequestFailed(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));
						} else if (("authentication_required").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdReauth(getString(R.string.msg_response_reauthentication_title1), getString(R.string.msg_response_reauthentication_para1));
						} else if (("authentication_expired").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {
							
							popdReauth(getString(R.string.msg_response_reauthentication_title2), getString(R.string.msg_response_reauthentication_para2));
						} else { //Any other error response
							
							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}

					} else { //Null or empty responses
						
						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				} //-----------------------------------------
				else if (result.contains("{'img_type':")) { //Received image data
					
					Gson gson = new Gson();
					imgp = gson.fromJson(result, ImagePojo.class);
					
					//Decode image and show
					byte[] decodedString = Base64.decode(imgp.getImg_data(), Base64.DEFAULT);
					decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
					imageViewBS.setImageBitmap(decodedByte);
					
					//Set UI components
					if( ("thumbnail").equals(imgp.getImg_type()) )
					{
						but_actimage.setVisibility(View.VISIBLE);
					}
					else
					{
						but_actimage.setVisibility(View.GONE);
					}
					
					if(GlobalMethods.validateString(imgp.getImg_data()) && imgp.getImg_data().length()>50) //just check whether image is there
					{
						but_saveimage.setVisibility(View.VISIBLE);
					}
					
				}


			} else { //Null or empty responses
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}

		} catch (Exception ex) {
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));

			if(GlobalVariables.PRINT_DEBUG)
			{
				Log.e(TAG, " " + ex.getMessage()); //Log actual error
			}

		}

	}

	// --------------------------------------------------------
	//Show pop-up messages
	
	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}

	private void popdReauth(String title, String content) {

		pdw = new PopupDialogWidget(this, title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		pdw.okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw.dismiss();
				startActivity(new Intent(context, UiProfileLogin.class));
				finish();
			}
		});

	}
	
	private void progress(String message) {

		progressDialog = new Dialog(context);
		progressDialog.getWindow();
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setContentView(R.layout.xml_popup_progressbar);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(true);

		TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
		txt_msg_title.setText(message);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(context));

		progressDialog.show();

	}

}
