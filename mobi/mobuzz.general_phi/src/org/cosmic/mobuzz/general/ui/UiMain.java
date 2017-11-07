package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.TextView;

public class UiMain extends Activity {

	Context context = UiMain.this;

	UiProfile uiProfile;
	UiAbout uiAbout;
	UiHome uiHome;
	FragmentTransaction ft;
	AlertDialog.Builder buildermsg;
	RadioGroup radioGroup;

	private final String TAG = UiMain.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //Hide action bar, configuration change in here to have the default theme.
		setContentView(R.layout.activity_ui_main);

		//Initialization of components
		rdg_main_navigation();

		GlobalIO.writeLog(context, TAG, LogAction.START, true);

		//Check whether login session is exist
		SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalVariables.MY_PREF, 0);
		if (!pref.getBoolean("login", false)) { //already login
			Intent intent = new Intent(context, UiProfileLogin.class);
			startActivity(intent);
			GlobalIO.writeLog(context, TAG, " [" + pref.getString("username", "") + "_relogin_redirect]; ", true);
			finish();
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
		GlobalIO.writeLog(context, TAG, LogAction.PAUSE, true);
	}

	@Override
	public void onBackPressed() {
		popdExit();
	}

	//Update navigation
	private void rdg_main_navigation() {

		try {

			radioGroup = (RadioGroup) findViewById(R.id.rdg_main_navigation);

			radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.rdb_home:
						but_home();
						break;
					case R.id.rdb_profile:
						but_profile();
						break;
					case R.id.rdb_enquiry:
						but_info();
						break;
					default:
						break;
					}

				}
			});

			int startPage = R.id.rdb_home;
			Bundle extras = getIntent().getExtras();
			
			if (extras != null) {
				startPage = extras.getInt("startNavigation", R.id.rdb_home);

				switch (startPage) {
				case 1:
					radioGroup.check(R.id.rdb_home);
					break;
				case 2:
					radioGroup.check(R.id.rdb_profile);
					break;
				case 3:
					radioGroup.check(R.id.rdb_enquiry);
					break;
				default:
					break;
				}

			} else {
				radioGroup.check(R.id.rdb_home);
			}

		} catch (Exception ex) {
			// nothing to do, continue
		}

	}

	//UI update
	private void but_home() {

		uiHome = new UiHome();
		ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_main, uiHome);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	//UI update
	private void but_profile() {
		uiProfile = new UiProfile();
		ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_main, uiProfile);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	//UI update
	private void but_info() {
		uiAbout = new UiAbout();
		ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_main, uiAbout);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	// --------------------------------------------------------
	//Show pop-up messages
	
	private void popdExit() {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xml_popup_dialog);
		dialog.setCanceledOnTouchOutside(false);

		TableRow tblr_msg_title = (TableRow) dialog.findViewById(R.id.tblr_msg_title);
		tblr_msg_title.setVisibility(View.GONE);

		TextView txt_msg_content = (TextView) dialog.findViewById(R.id.txt_msg_content);
		txt_msg_content.setText(R.string.msg_exit_para1);
		txt_msg_content.setTypeface(GlobalMethods.getTypeface(context));

		Button btn_msg_ok = (Button) dialog.findViewById(R.id.btn_msg_ok);
		btn_msg_ok.setText(R.string.msg_common_stayson);
		btn_msg_ok.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		Button btn_msg_no = (Button) dialog.findViewById(R.id.btn_msg_no);
		btn_msg_no.setText(R.string.msg_common_exit);
		btn_msg_no.setTypeface(GlobalMethods.getTypeface(context), Typeface.BOLD);

		btn_msg_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_msg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GlobalIO.writeLog(context, UiMain.class.getName(), LogAction.EXIT, true);
				moveTaskToBack(true);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		});

		dialog.show();
	}

}
