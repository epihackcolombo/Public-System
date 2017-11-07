package org.cosmic.mobuzz.general.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.cosmic.mobuzz.general.phi.R;

//Show pop-up progress dialog
public class PopupProgressWidget extends Dialog {

	public Activity activity;
	public Button okButton, noButton;
	private TextView txt_msg_title;
	
	String titleMessage = "";
	boolean isCancelable = false;

	
	public PopupProgressWidget(Activity activity, String titleMessage, boolean isCancelable) {
		super(activity);
		this.activity = activity;
		this.titleMessage = titleMessage;
		this.isCancelable = isCancelable;
	}
	
	public PopupProgressWidget(Activity activity, int titleMessage, boolean isCancelable) {
		super(activity);
		this.activity = activity;
		this.titleMessage = activity.getString(titleMessage);
		this.isCancelable = isCancelable;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE); //Hide the action bar, configuration is changed here to have the default theme.
		setContentView(R.layout.xml_popup_progressbar);
		setCanceledOnTouchOutside(false);
		setCancelable(isCancelable);

		txt_msg_title = (TextView) findViewById(R.id.txt_msg_content);
		txt_msg_title.setTypeface(GlobalMethods.getTypeface(activity));
		
		txt_msg_title.setText(titleMessage);
	}

}
