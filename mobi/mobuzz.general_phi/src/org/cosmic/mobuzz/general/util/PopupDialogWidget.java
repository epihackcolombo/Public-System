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

//Show pop-up message dialog
public class PopupDialogWidget extends Dialog implements android.view.View.OnClickListener {

	public Activity activity;
	public Button okButton, noButton;
	private TextView txt_msg_title, txt_msg_content;
	
	String titleMessage = "", bodyMessage = "", okMessage = "", noMessage = "";
	boolean isCancelable = false;

	
	public PopupDialogWidget(Activity activity, String titleMessage, String bodyMessage, String okMessage, String noMessage, boolean isCancelable) {
		super(activity);
		this.activity = activity;
		this.titleMessage = titleMessage;
		this.bodyMessage = bodyMessage;
		this.okMessage = okMessage;
		this.noMessage = noMessage;
		this.isCancelable = isCancelable;
	}


	public PopupDialogWidget(Activity activity, int titleMessage, int bodyMessage, String okMessage, String noMessage, boolean isCancelable) {
		super(activity);
		this.activity = activity;
		this.titleMessage = activity.getString(titleMessage);
		this.bodyMessage = activity.getString(bodyMessage);
		this.okMessage = okMessage;
		this.noMessage = noMessage;
		this.isCancelable = isCancelable;
	}


	public PopupDialogWidget(Activity activity, int titleMessage, int bodyMessage, int okMessage, int noMessage, boolean isCancelable) {
		super(activity);
		this.activity = activity;
		this.titleMessage = activity.getString(titleMessage);
		this.bodyMessage = activity.getString(bodyMessage);
		this.okMessage = activity.getString(okMessage);
		this.noMessage = activity.getString(noMessage);
		this.isCancelable = isCancelable;
	}

	
	public PopupDialogWidget(Activity activity, int titleMessage, int bodyMessage, int okMessage, boolean isCancelable) {
		super(activity);
		this.activity = activity;
		this.titleMessage = activity.getString(titleMessage);
		this.bodyMessage = activity.getString(bodyMessage);
		this.okMessage = activity.getString(okMessage);
		this.noMessage = null;
		this.isCancelable = isCancelable;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xml_popup_dialog);
		setCanceledOnTouchOutside(false);
		setCancelable(isCancelable);
			
		setContentView(R.layout.xml_popup_dialog);
		
		//Initialization of dialog components 
		
		okButton = (Button) findViewById(R.id.btn_msg_ok);
		noButton = (Button) findViewById(R.id.btn_msg_no);
		
		txt_msg_title = (TextView)findViewById(R.id.txt_msg_title);
		txt_msg_content = (TextView)findViewById(R.id.txt_msg_content);

		txt_msg_title.setTypeface(GlobalMethods.getTypeface(activity), Typeface.BOLD);
		txt_msg_content.setTypeface(GlobalMethods.getTypeface(activity));
		okButton.setTypeface(GlobalMethods.getTypeface(activity), Typeface.BOLD);
		noButton.setTypeface(GlobalMethods.getTypeface(activity), Typeface.BOLD);
		

		txt_msg_title.setText(titleMessage);
		txt_msg_content.setText(bodyMessage);
		
		
		if(GlobalMethods.validateString(okMessage))
		{
			okButton.setText(okMessage);
		}
		else
		{
			okButton.setVisibility(View.GONE);
		}
		
		
		if(GlobalMethods.validateString(noMessage))
		{
			noButton.setText(noMessage);
		}
		else
		{
			noButton.setVisibility(View.GONE);
		}
		

		okButton.setOnClickListener(this);
		noButton.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.btn_msg_ok:
			dismiss();
			break;
		case R.id.btn_msg_no:
			dismiss();
			break;
		default:
			break;
		}
		dismiss();
	}
}
