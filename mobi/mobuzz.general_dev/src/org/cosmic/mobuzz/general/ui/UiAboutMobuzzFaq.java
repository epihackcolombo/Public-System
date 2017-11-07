package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

public class UiAboutMobuzzFaq extends Activity {

	Context context = UiAboutMobuzzFaq.this;
	
	private final String TAG = UiAboutMobuzzFaq.class.getName();
	private Typeface font;

	TextView txt_faq_mobuzz_para3_2_1, txt_faq_mobuzz_para3_3_1, txt_faq_mobuzz_para3_4_1, txt_faq_mobuzz_para13, txt_faq_mobuzz_para14, txt_faq_mobuzz_para10;
	TextView txt_faq_mobuzz_title1,
	txt_faq_mobuzz_title3,
	txt_faq_mobuzz_title4,
	txt_faq_mobuzz_title5,
	txt_faq_mobuzz_title6,
	txt_faq_mobuzz_title7,
	txt_faq_mobuzz_title8,
	txt_faq_mobuzz_title9,
	txt_faq_mobuzz_title10,
	txt_faq_mobuzz_title11,
	txt_faq_mobuzz_title12,
	txt_faq_mobuzz_title13,
	txt_faq_mobuzz_title14;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_about_mobuzz_faq);

		//Initialization of components
		txt_faq_mobuzz_para3_2_1 = (TextView) findViewById(R.id.txt_faq_mobuzz_para3_2_1);
		txt_faq_mobuzz_para3_3_1 = (TextView) findViewById(R.id.txt_faq_mobuzz_para3_3_1);
		txt_faq_mobuzz_para3_4_1 = (TextView) findViewById(R.id.txt_faq_mobuzz_para3_4_1);
		txt_faq_mobuzz_para13 = (TextView) findViewById(R.id.txt_faq_mobuzz_para13);
		txt_faq_mobuzz_para14 = (TextView) findViewById(R.id.txt_faq_mobuzz_para14);
		
		txt_faq_mobuzz_para10 = (TextView) findViewById(R.id.txt_faq_mobuzz_para10);

		txt_faq_mobuzz_para3_2_1.setText(Html.fromHtml(getString(R.string.faq_mobuzz_para3_2_1)));
		txt_faq_mobuzz_para3_3_1.setText(Html.fromHtml(getString(R.string.faq_mobuzz_para3_3_1)));
		txt_faq_mobuzz_para3_4_1.setText(Html.fromHtml(getString(R.string.faq_mobuzz_para3_4_1)));
	
		//font = GlobalMethods.getTypeface(context);
		
		//for language translation
		SharedPreferences pref = getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String pref_lng = pref.getString("language", "");
		
		if(("sinhala").equalsIgnoreCase(pref_lng))
		{
			font = GlobalMethods.getTypeface(context, "sinhala");
		}
		else if(("tamil").equalsIgnoreCase(pref_lng))
		{
			font = GlobalMethods.getTypeface(context, "tamil");
		}
		else
		{
			font = GlobalMethods.getTypeface(context);
		}

		
		ViewGroup view = (ViewGroup)getWindow().getDecorView();
		GlobalMethods.applyFontRecursively(view, font);

		txt_faq_mobuzz_title1 = (TextView) findViewById(R.id.txt_faq_mobuzz_title1);
		txt_faq_mobuzz_title3 = (TextView) findViewById(R.id.txt_faq_mobuzz_title3);
		txt_faq_mobuzz_title4 = (TextView) findViewById(R.id.txt_faq_mobuzz_title4);
		txt_faq_mobuzz_title5 = (TextView) findViewById(R.id.txt_faq_mobuzz_title5);
		txt_faq_mobuzz_title6 = (TextView) findViewById(R.id.txt_faq_mobuzz_title6);
		txt_faq_mobuzz_title7 = (TextView) findViewById(R.id.txt_faq_mobuzz_title7);
		txt_faq_mobuzz_title8 = (TextView) findViewById(R.id.txt_faq_mobuzz_title8);
		txt_faq_mobuzz_title9 = (TextView) findViewById(R.id.txt_faq_mobuzz_title9);
		txt_faq_mobuzz_title10 = (TextView) findViewById(R.id.txt_faq_mobuzz_title10);
		txt_faq_mobuzz_title11 = (TextView) findViewById(R.id.txt_faq_mobuzz_title11);
		txt_faq_mobuzz_title12 = (TextView) findViewById(R.id.txt_faq_mobuzz_title12);
		txt_faq_mobuzz_title13 = (TextView) findViewById(R.id.txt_faq_mobuzz_title13);
		txt_faq_mobuzz_title14 = (TextView) findViewById(R.id.txt_faq_mobuzz_title14);
		
		txt_faq_mobuzz_title1 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title3 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title4 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title5 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title6 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title7 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title8 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title9 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title10 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title11 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title12 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title13 .setTypeface(font, Typeface.BOLD);
		txt_faq_mobuzz_title14 .setTypeface(font, Typeface.BOLD);
		
		//Enable html links
		txt_faq_mobuzz_para13.setMovementMethod(LinkMovementMethod.getInstance());
		txt_faq_mobuzz_para14.setMovementMethod(LinkMovementMethod.getInstance());

		GlobalIO.writeLog(context, TAG, LogAction.START, true);
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
		super.onBackPressed();
		GlobalIO.writeLog(context, TAG, LogAction.END, true);
		finish();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
		GlobalMethods.cleanMemory();
	}
	

}
