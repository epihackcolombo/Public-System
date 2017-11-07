package org.cosmic.mobuzz.general.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.HttpUpload;
import org.cosmic.mobuzz.general.network.MakeJsonString;
import org.cosmic.mobuzz.general.pojo.ProfileLoginPojo;
import org.cosmic.mobuzz.general.pojo.ResponsePojo;
import org.cosmic.mobuzz.general.ui.support.PopuplistAdapter;
import org.cosmic.mobuzz.general.util.CMCRegions;
import org.cosmic.mobuzz.general.util.GPSTracker;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;
import org.cosmic.mobuzz.general.util.PopupDialogWidget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.CursorLoader;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

public class UiBreedingsiteSubpageReport extends android.support.v4.app.Fragment implements LocationListener, LocationSource {

	Button btn_gallery, btn_camera, btn_submit, btn_assessment;
	EditText addressET, remarksET;
	ImageView imv_image;
	TextView txt_cmc_message_title, txt_cmc_message_para, txt_submit;

	String mCurrentPhotoPath;
	String assessment = "";
	Bitmap bitmap;

	Dialog progressDialog;
	PopupDialogWidget pdw, pdw_gpscmc;

	private final String TAG = UiBreedingsiteSubpageReport.class.getName();
	private String url_report_submit;
	private Typeface font;

	Activity activity = this.getActivity();

	GPSTracker gps;
	private Double latitude;
	private Double longitude;

	private int noRetry = 0;
	private SupportMapFragment mMapFragment;
	private GoogleMap mMap;
	private LatLng latLng;
	private boolean isFirst = false;
	private CMCRegions cmcRegions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ui_breedingsite_subpage_report, container, false);

		//Initialization of components
		this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Hide action bar, configuration change here to have default theme.
		url_report_submit = getString(R.string.url_server_port) + getString(R.string.url_report_submit);
		
		btn_gallery = (Button) view.findViewById(R.id.but_gallery);
		btn_camera = (Button) view.findViewById(R.id.but_camera);
		btn_submit = (Button) view.findViewById(R.id.but_submit);
		btn_assessment = (Button) view.findViewById(R.id.but_asses);

		addressET = (EditText) view.findViewById(R.id.edt_address);
		remarksET = (EditText) view.findViewById(R.id.edt_remark);

		imv_image = (ImageView) view.findViewById(R.id.imv_image);

		txt_cmc_message_title = (TextView) view.findViewById(R.id.txt_cmc_message_title);
		txt_cmc_message_para = (TextView) view.findViewById(R.id.txt_cmc_message_para);
		txt_submit = (TextView) view.findViewById(R.id.txt_submit);

		txt_submit.setText(" ");
		
		font = GlobalMethods.getTypeface(this.getActivity());
		addressET.setTypeface(font);
		remarksET.setTypeface(font);
		txt_cmc_message_title.setTypeface(font);
		txt_cmc_message_para.setTypeface(font);

		btn_gallery.setTypeface(font);
		btn_camera.setTypeface(font);
		btn_submit.setTypeface(font);
		btn_assessment.setTypeface(font);

		setUploadEnable(false); //Disable until GPS location is available

		btn_gallery_Click();
		btn_camera_Click();
		btn_submit_Click();
		btn_assessment_Click();

		cmcRegions = new CMCRegions();
		latLng = GlobalVariables.GPS_COLOMBO; //Default GPS coordinates for Colombo

		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()); //Check the availability of service
		if (status == ConnectionResult.SUCCESS) {
			setUpMap();

		} else { //If map service is unavailable
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
			dialog.show();
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mMap != null) {
			mMap.setMyLocationEnabled(true); //To set the new location
		}

		GlobalIO.writeLog(getActivity(), TAG, LogAction.RESUME, true);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}

		if (mMap != null) {
			mMap.setMyLocationEnabled(false); //Disable location service to save power
		}

		GlobalIO.writeLog(getActivity(), TAG, LogAction.PAUSE, true);
	}
	

    @Override
    public void onSaveInstanceState(Bundle outState) {
 	
    	outState.putString("photopath", mCurrentPhotoPath);
		super.onSaveInstanceState(outState); //Save the path, to get the same image if device is rotating
    }  

    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
        	mCurrentPhotoPath = savedInstanceState.getString("photopath");
        	if (GlobalMethods.validateString(mCurrentPhotoPath)) { //Restore the image
        		galleryLoadCameraPic();
        	}
        }
    }


	@Override
	public void setUserVisibleHint(boolean isFragmentVisible) {
		super.setUserVisibleHint(isFragmentVisible);

		if (isFragmentVisible) {
			GlobalIO.writeLog(getActivity(), TAG, LogAction.SHOW, true);
		} else {
			if (mMap != null) {
				mMap.setMyLocationEnabled(false); //Disable location service, when fragment is hide
			}
			
			if(txt_submit!=null)
			{
				txt_submit.clearFocus(); //Clear error message pop-up
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case GlobalVariables.TAKE_PHOTO_CODE:
			onCameraPhoto(resultCode, data);
			break;
		case GlobalVariables.GALLERY_PHOTO_CODE:
			onGalleryPhoto(resultCode, data);
			break;
		default:
			break;
		}
	}

	//Control the upload of data based on location.
	private void setUploadEnable(boolean isEnable) {

	//Note: This section is removed because of reported issues in some devices
		//To do later
		/*
		isEnable = true;	
		
		//Update UI components
		if (isEnable) {
			btn_submit.setEnabled(true);
			btn_submit.setTextColor(getResources().getColor(R.color.mobuzz_gray));
			txt_submit.setError(null);

			txt_submit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//nothing to do.
				}
			});
			
		} 
		else 
		{
			btn_submit.setEnabled(false);
			btn_submit.setTextColor(getResources().getColor(R.color.red));
			
			//txt_submit.setText(" ");
			txt_submit.requestFocus();
			txt_submit.setError(getString(R.string.msg_gps_no_para2));
			
			txt_submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					txt_submit.requestFocus();
					txt_submit.setError(getString(R.string.msg_gps_no_para2));
				}
			});
		}
		*/
	}

	//Get user location
	private void getGPSCoordinates() {
		gps = new GPSTracker(getActivity());

		
		if (gps == null) {
			LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER); //Check the GPS service

			if (!enabled) {
				popdNoGPS(getString(R.string.msg_gps_no_para1), true);
			} else {
				popdNoGPS(getString(R.string.msg_gps_no_para2), true);
			}

		} else {
		
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			
			//latitude = GlobalVariables.GPS_COLOMBO.latitude;
			//longitude = GlobalVariables.GPS_COLOMBO.longitude;

			
			if (latitude == 0 || longitude == 0) {
				popdNoGPS(getString(R.string.msg_gps_no_para2), true);
			} else if (cmcRegions.isInCMC(new LatLng(latitude, longitude))) { //Check whether location is in CMC jurisdiction
				// CMC GPS coordinates
				setUploadEnable(true);
			} else {
				isFirst = true;
				popdUnclearGPS(getString(R.string.msg_gps_no_para3));
			}
			
		}
		
	}

	//-------------------- Setup the map -----------------------//

	@Override
	public void activate(OnLocationChangedListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
	}

	//Set up google map
	private void setUpMap() {

		if (mMap == null) {
			MapsInitializer.initialize(getActivity());
			
			mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map); //Get the fragment for google-map
			mMap = mMapFragment.getMap();

			CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).build();
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			mMap.setMyLocationEnabled(true);
			mMap.addPolygon(new PolygonOptions().addAll(cmcRegions.getCMCRegion()).fillColor(Color.parseColor(GlobalVariables.COLOR_REGION_OVERLAY))).setStrokeWidth(5); //Draw the CMC jurisdiction

			if (mMap != null) {

				mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

					@Override
					public void onMyLocationChange(Location arg0) {
						latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
						mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

						latitude = arg0.getLatitude();
						longitude = arg0.getLongitude();
						
						//latitude = GlobalVariables.GPS_COLOMBO.latitude;
						//longitude = GlobalVariables.GPS_COLOMBO.longitude;

						if (!isFirst) {
							isFirst = true;

							if (latitude == 0 || longitude == 0) {
								// nothing to do
							} else if (cmcRegions.isInCMC(latLng)) {
								//Warn user if location is not in CMC jurisdiction
								setUploadEnable(true);
							} else {
								popdUnclearGPS(getString(R.string.msg_gps_no_para3));
							}
						}

					}
				});
			}

		}

		getGPSCoordinates(); //Getting the current location

	}

	// Spinner:-----------------------------
	private void btn_assessment_Click() {
		btn_assessment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showAssessment(); //Pop-up self assessment options
			}
		});
	}

	private void showAssessment() {
		popdListrAssessment();
	}

	private void btn_submit_Click() {
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				attemptUpload();
			}
		});
	}

	protected void attemptUpload() {

		if (isNetworkAvailable()) { //Check network availability

			if (latitude == null || longitude == null || latitude == 0 || longitude == 0) {
				//Fix for location
				popdRequestFailed(getString(R.string.msg_gps_no_title1), getString(R.string.msg_gps_no_para4));
			} 
			else if(!cmcRegions.isInCMC(new LatLng(latitude, longitude)))
			{
				//In CMC region
				popdUnclearGPSUpload(getString(R.string.msg_gps_no_para3));
			}
			else {
				uploadTask();
			}
			
		} else {
			popdNoInternet();
		}
	}

	//Upload complaint data
	private void uploadTask() {

		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(GlobalVariables.MY_PREF, 0);

		String address = addressET.getText().toString();
		String remarks = remarksET.getText().toString();
		String timestamp = pref.getString("time_stamp", null);
		String user = pref.getString("username", null);
		String uudid = pref.getString("uudid", "");
		String location;

		remarksET.setError(null);
		addressET.setError(null);

		if (latitude != null && longitude != null) {
			location = latitude.toString() + "," + longitude.toString();
		} else {
			location = "0,0"; // not on sri lanka
		}

		if (location.length() <= 4) // at least four characters with comma
		{
			popupContent(getString(R.string.msg_gps_no_title1), getString(R.string.msg_gps_no_para1), 0);
			return;
		}

		if (!GlobalMethods.validateString(address)) {
			addressET.setError(getString(R.string.error_breedingsite_address));
			return;
		}

		if (!GlobalMethods.validateString(remarks)) {

			if (bitmap == null) {
				remarksET.requestFocus();
				remarksET.setError(getString(R.string.error_breedingsite_remark));
				return;
			} else {
				// Nothing to do.
			}
		}

		TelephonyManager tMngr = (TelephonyManager) this.getActivity().getSystemService(Context.TELEPHONY_SERVICE); //Get the user's actual phone number. This is taking as a measurement for misuse of the app. This is optional.

		String data[] = { tMngr.getLine1Number(), address, remarks, assessment, timestamp, location, user, uudid };

		new FormUpload(url_report_submit, bitmap, data).execute();

	}

	private void btn_camera_Click() {
		btn_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dispatchTakePictureIntent();
			}
		});
	}

	private void btn_gallery_Click() {
		btn_gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //Providing uri to get high resolution image
				startActivityForResult(intent, GlobalVariables.GALLERY_PHOTO_CODE);
			}
		});
	}

	/**
	 * @param resultCode
	 * @param data
	 * 
	 * Returns result from the gallery action.
	 */
	private void onGalleryPhoto(int resultCode, Intent data) {
		if ((resultCode == Activity.RESULT_OK) && (data != null)) {

			try {
				// Get the selected image's location.
				setGalleryImageUri(data.getData());
			} catch (Exception ex) {
				if (GlobalVariables.PRINT_DEBUG) {
					Log.e(TAG, "" + ex.toString());
				}
			}

		} else {
			if (GlobalVariables.PRINT_DEBUG) {
				Log.w(TAG, "No image from gallery action");
			}
		}
	}

	/**
	 * @param uri
	 * 
	 *            Assigning image uri to the interface image view.
	 */
	//Get the image from gallery
	private void setGalleryImageUri(Uri uri) {

		if (uri == null) {
			if (GlobalVariables.PRINT_DEBUG) {
				Log.i(TAG, "Invalid uri");
			}
			imv_image.setImageBitmap(null);
		} else {

			try {

				mCurrentPhotoPath = getRealPathFromURI(uri);

				if (GlobalMethods.validateString(mCurrentPhotoPath)) {
					Point ptImageSize = GlobalMethods.getImageSize(Uri.parse(mCurrentPhotoPath)); //Get the image's actual dimensions
					Point ptDisplaySize = GlobalMethods.getDisplaySize(this.getActivity()); //Get the device screen dimensions

					int inSampleSize = 1;

					if (ptImageSize != null && ptDisplaySize != null) {
						inSampleSize = GlobalMethods.getInSampleSize(ptImageSize.y, ptImageSize.x, ptDisplaySize.y, ptDisplaySize.x); //Get the image resize ratio
					}

					//Clear memory
					clearMemory();

					//Resize and rotate the actual image if necessary
					BitmapFactory.Options bmOptions = new BitmapFactory.Options();
					bmOptions.inSampleSize = inSampleSize;
					bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
					bitmap = rotateBitmap(bitmap, mCurrentPhotoPath);
					imv_image.setImageBitmap(bitmap);

				}

			} catch (Exception ex) {
				if (GlobalVariables.PRINT_DEBUG) {
					Log.e(TAG, " " + ex.getMessage());
				}
			}
		}

	}

	//Prepare the image for upload. Image is resized and rotated to optimize with device-memory and device-screen.
	private void setImageResize() {
		try {

			if (GlobalMethods.validateString(mCurrentPhotoPath)) {
				Point ptImageSize = GlobalMethods.getImageSize(Uri.parse(mCurrentPhotoPath));
				Point ptDisplaySize = GlobalMethods.getDisplaySize(this.getActivity());

				int inSampleSize = 1;

				if (ptImageSize != null) {
					inSampleSize = GlobalMethods.getInSampleSize(ptImageSize.y, ptImageSize.x, Math.round(ptDisplaySize.y / 4), Math.round(ptDisplaySize.x / 4));
				}

				//Clear memory
				clearMemory();

				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = inSampleSize;
				bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
				bitmap = rotateBitmap(bitmap, mCurrentPhotoPath);
				imv_image.setImageBitmap(bitmap);
				imv_image.refreshDrawableState();

				uploadTask();
			}

		} catch (Exception ex) {
			if (GlobalVariables.PRINT_DEBUG) {
				Log.e(TAG, " " + ex.getMessage());
			}
		}
	}

	// Take an image from camera
	private void dispatchTakePictureIntent() {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = createImageFile(); //Create a file to hold the photo

				if (photoFile != null) {
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile)); //File path is passed to get a high resolution photo
					startActivityForResult(takePictureIntent, GlobalVariables.TAKE_PHOTO_CODE);
				}
			} catch (IOException ex) {
				if (GlobalVariables.PRINT_DEBUG) {
					Log.e(TAG, " " + ex.getMessage());
				}
			}

		}
	}

	private void onCameraPhoto(int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			galleryAddCameraPic();
			galleryLoadCameraPic();
		}

	}

	//Create a file to hold the photo
	private File createImageFile() throws IOException {
		//Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = /* "file:" + */image.getAbsolutePath();
		return image;
	}

	//Add the photo to the device gallery
	private boolean galleryAddCameraPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.getActivity().sendBroadcast(mediaScanIntent);
		return true;
	}

	//Get the taken camera-photo from gallery, optimize and show
	private void galleryLoadCameraPic() {

		if (GlobalMethods.validateString(mCurrentPhotoPath)) {

			Point ptImageSize = GlobalMethods.getImageSize(Uri.parse(mCurrentPhotoPath));
			Point ptDisplaySize = GlobalMethods.getDisplaySize(this.getActivity());

			int inSampleSize = 1;

			if (ptImageSize != null && ptDisplaySize != null) {
				inSampleSize = GlobalMethods.getInSampleSize(ptImageSize.y, ptImageSize.x, ptDisplaySize.y, ptDisplaySize.x);
			}

			//Clear memory
			clearMemory();

			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = inSampleSize;
			bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			bitmap = rotateBitmap(bitmap, mCurrentPhotoPath);
			imv_image.setImageBitmap(bitmap);
		}

	}

	// END: Take an image from camera 

	//Rotate a bitmap, based on device and image orientation information
	private Bitmap rotateBitmap(Bitmap bitmap, String path) {

		try {
			ExifInterface exif = new ExifInterface(path);

			int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int rotationInDegrees = exifToDegrees(rotation);
			Matrix matrix = new Matrix();

			if (rotation != 0f) {
				matrix.preRotate(rotationInDegrees);
			}
			Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			return adjustedBitmap;

		} catch (IOException ex) {
			if (GlobalVariables.PRINT_DEBUG) {
				Log.e(TAG, " " + ex.getMessage());
			}
		}
		return null;
	}

	//Get the rotation degree
	private int exifToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	//Encode bitmap data as a string
	public String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	//Get the absolute path of the resource
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		CursorLoader cursorLoader = new CursorLoader(this.getActivity(), contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	//Send data to the server
	private class FormUpload extends AsyncTask<String, Void, String> {

		ResponsePojo responsePojo;
		String url_report_submit, imageString;
		Bitmap bitmap;
		String[] data;

		MakeJsonString mjs;

		public FormUpload(String url_report_submit, Bitmap bitmap, String[] data) {
			this.url_report_submit = url_report_submit;
			this.bitmap = bitmap;
			this.data = data;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new Dialog(getActivity());
			progressDialog.getWindow();
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.xml_popup_progressbar);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);

			TextView txt_msg_title = (TextView) progressDialog.findViewById(R.id.txt_msg_content);
			txt_msg_title.setText(getString(R.string.msg_uploading_para1));
			txt_msg_title.setTypeface(GlobalMethods.getTypeface(getActivity()));

			progressDialog.show();
			
		}

		@Override
		protected String doInBackground(String... formdata) {

			if (bitmap != null) {
				imageString = bitmapToString(bitmap);
			} else {
				imageString = "";
			}

			mjs = new MakeJsonString();
			String dataString = mjs.forFormSubmit(imageString, data);

			responsePojo = HttpUpload.uploadRequestData(dataString, url_report_submit);

			if (responsePojo != null) {

				return responsePojo.getResponseContent();
			} else {

				return "";
			}

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			//Check the server response regarding the data-upload
			if (responsePojo != null) {

				if (responsePojo != null && responsePojo.getResponseCode() == 200) { //OK response
					noRetry = 0;
					responseToResult(result);
				} else if (responsePojo != null && responsePojo.getResponseCode() == 413) { //Request interrupted, try again
					if (noRetry <= 0) {
						noRetry++;
						//Retry with a smaller image size
						popupContent(getActivity().getApplicationContext().getResources().getString(R.string.msg_request_rejected_title1), getActivity().getApplicationContext().getResources().getString(R.string.msg_request_rejected_para1), 1);
					} else {
						//Already tried, hence give up
						noRetry = 0;
						popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
					}
				} else if (GlobalVariables.CONN_EXCEPTION.equals(responsePojo.getResponseContent())) { //Connection exception occurred
					noRetry = 0;
					popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para1));
				} else if (GlobalVariables.OTHER_EXCEPTION.equals(responsePojo.getResponseContent())) { //Other exception occurred
					noRetry = 0;
					popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para2));
				} else { //Other
					noRetry = 0;
					popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
				}
			} else { //Null response
				popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			}

		}
	}

	public void responseToResult(String result) {

		try {
			if (GlobalMethods.validateString(result)) {

				//Validation block ------------------------
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
						} else if (("error_net_other").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							popdRequestFailed(getString(R.string.msg_connect_no_title1), getString(R.string.msg_connect_no_para2));
						} else if (("error_db_params").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							popdRequestFailed(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));
						} else if (("error_db_connect").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							popdRequestFailed(getString(R.string.msg_response_unexpected_title2), getString(R.string.msg_response_unexpected_para2));
						} else if (("authentication_required").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							popdReauth(getString(R.string.msg_response_reauthentication_title1), getString(R.string.msg_response_reauthentication_para1));
						} else if (("authentication_expired").equals(plp.getStatus().toLowerCase(Locale.ENGLISH))) {

							popdReauth(getString(R.string.msg_response_reauthentication_title2), getString(R.string.msg_response_reauthentication_para2));
						} else {

							popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
						}

					} else {

						popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
					}

				} //-----------------------------------------
				else if (result.contains(",")) { //Server response

					String[] parts = result.split(",");

					if (GlobalVariables.PRINT_DEBUG) {
						Log.d(TAG, " " + result);
					}

					if (!GlobalMethods.validateString(parts[0])) { //Null or empty response
						successUpload(false);
					} else if (parts[0].equals("ok")) { //Requested operation was succeed
						successUpload(true);
					} else if ((parts[0]).contains("Server is busy please try again later")) { //Requested couldn't processed
						popupContent(getString(R.string.msg_serverbusy_title1), getString(R.string.msg_serverbusy_para1), 0);
					} else { //Other issue
						successUpload(false);
					}

				} else { //Not the succeed response
					popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
				}

			} else { //Null or empty response
				popdRequestFailed(getString(R.string.msg_response_unexpected_title1), getString(R.string.msg_response_unexpected_para1));
			}
		} catch (Exception ex) {
			popdRequestFailed(getString(R.string.msg_request_fail_title1), getString(R.string.msg_request_fail_para1));
			if (GlobalVariables.PRINT_DEBUG) {
				Log.e(TAG, " " + ex.getMessage());
			}
		}

	}
	
	//Clear UI and memory
	private void clearData() {
		addressET.setText(null);
		remarksET.setText(null);
		btn_assessment.setText(R.string.msg_breedingsite_assessment);
		imv_image.setImageResource(R.drawable.bg_forms);

		clearMemory();
	}

	private void clearMemory() {
		//Clear memory
		System.gc();
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	//Check the network status
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	// --------------------------------------------------------
	//Show pop-up messages
	
	private void popdNoInternet() {

		pdw = new PopupDialogWidget(getActivity(), R.string.msg_internet_no_title1, R.string.msg_internet_no_para1, R.string.msg_common_ok, false);
		pdw.show();

	}

	private void successUpload(boolean isSuccess) {

		if (isSuccess) {

			pdw = new PopupDialogWidget(getActivity(), R.string.msg_report_success_title1, R.string.msg_report_success_para1, R.string.msg_common_ok, false);
		} else {

			pdw = new PopupDialogWidget(getActivity(), R.string.msg_report_fail_title1, R.string.msg_report_fail_para1, R.string.msg_common_ok, false);
		}

		pdw.show();

		if (isSuccess) {
			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					clearData();
				}
			});
		}

	}

	private void popupContent(String title, String content, int buttonAction) {

		pdw = new PopupDialogWidget(getActivity(), title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		switch (buttonAction) {

		case 1:
			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					setImageResize();
				}
			});
			break;

		default:
			break;

		}

	}

	private void popdNoGPS(String content, boolean isExit) {

		if (isExit) {

			pdw = new PopupDialogWidget(getActivity(), this.getString(R.string.msg_gps_no_title1), content, this.getString(R.string.msg_common_settings), this.getString(R.string.msg_common_later), false);
			pdw.show();

			pdw.okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
					getActivity().finish();
				}
			});

			pdw.noButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pdw.dismiss();
					setUploadEnable(false);
				}
			});

		} else {
			// Nothing for now
		}

	}

	private void popdUnclearGPS(String content) {

		pdw_gpscmc = new PopupDialogWidget(getActivity(), this.getString(R.string.msg_gps_no_title2), content, this.getString(R.string.msg_common_usecurrent), this.getString(R.string.msg_common_settings), false);
		pdw_gpscmc.show();

		pdw_gpscmc.okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw_gpscmc.dismiss();
				setUploadEnable(true);
			}
		});

		pdw_gpscmc.noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw_gpscmc.dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				getActivity().finish();
			}
		});

		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
	}
	
	private void popdUnclearGPSUpload(String content) { //Fix for location issue
		
		if(pdw_gpscmc.isShowing())
		{
			pdw_gpscmc.dismiss();
		}
		
		pdw_gpscmc = new PopupDialogWidget(getActivity(), this.getString(R.string.msg_gps_no_title2), content, this.getString(R.string.msg_common_usecurrent), this.getString(R.string.msg_common_ok), false);
		pdw_gpscmc.show();
		
		pdw_gpscmc.okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw_gpscmc.dismiss();
				uploadTask(); //Upload data
			}
		});
		
		pdw_gpscmc.noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw_gpscmc.dismiss();
			}
		});
		
		if (pdw != null && pdw.isShowing()) {
			pdw.dismiss();
		}
	}
	
	//Self assessment dialog
	private void popdListrAssessment() {

		final Dialog dialog = new Dialog(this.getActivity());
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_list_dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		TextView txt_msg_title = (TextView) dialog.findViewById(R.id.txt_msg_title);
		txt_msg_title.setText(R.string.msg_breedingsite_assessment);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(this.getActivity()), Typeface.BOLD);

		final ArrayList<String> list = new ArrayList<String>();
		for (String residence : getResources().getStringArray(R.array.user_assessment)) {
			list.add(residence);
		}

		PopuplistAdapter adapter = new PopuplistAdapter(this.getActivity(), list);
		ListView lstw_msg_content = (ListView) dialog.findViewById(R.id.lstw_msg_content);
		lstw_msg_content.setAdapter(adapter);
		lstw_msg_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (GlobalMethods.validateString(item)) {
					btn_assessment.setText(item);
					assessment = item;
				}

				dialog.dismiss();
			}
		});

		Button btn_msg_no = (Button) dialog.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText("Cancel");
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(this.getActivity()), Typeface.BOLD);

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	private void popdRequestFailed(String title, String content) {

		pdw = new PopupDialogWidget(getActivity(), title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

	}

	private void popdReauth(String title, String content) {

		pdw = new PopupDialogWidget(getActivity(), title, content, this.getString(R.string.msg_common_ok), null, false);
		pdw.show();

		pdw.okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdw.dismiss();
				startActivity(new Intent(getActivity(), UiProfileLogin.class));
				getActivity().finish();
			}
		});

	}
	
}
