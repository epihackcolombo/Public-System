package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UiEducationSubpageSpread extends Fragment {

	Typeface font;
	private TextView txt_dengue_spread_title1, txt_dengue_spread_title2, txt_dengue_spread_title3, txt_dengue_spread_title4, txt_dengue_spread_title5, txt_dengue_spread_para1, txt_dengue_spread_para2, txt_dengue_spread_para3, txt_dengue_spread_para4, txt_dengue_spread_para5, txt_dengue_spread_para6;

	private final String TAG = UiEducationSubpageSpread.class.getName();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ui_education_subpage_spread, container, false);

		//Initialization of components
		//font = GlobalMethods.getTypeface(getActivity());
		
		//for language translation
		SharedPreferences pref = this.getActivity().getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String pref_lng = pref.getString("language", "");
		
		if(("sinhala").equalsIgnoreCase(pref_lng))
		{
			font = GlobalMethods.getTypeface(this.getActivity(), "sinhala");
		}
		else if(("tamil").equalsIgnoreCase(pref_lng))
		{
			font = GlobalMethods.getTypeface(this.getActivity(), "tamil");
		}
		else
		{
			font = GlobalMethods.getTypeface(this.getActivity());
		}
		

		txt_dengue_spread_title1 = (TextView) view.findViewById(R.id.txt_dengue_spread_title1);
		txt_dengue_spread_title2 = (TextView) view.findViewById(R.id.txt_dengue_spread_title2);
		txt_dengue_spread_title3 = (TextView) view.findViewById(R.id.txt_dengue_spread_title3);
		txt_dengue_spread_title4 = (TextView) view.findViewById(R.id.txt_dengue_spread_title4);
		txt_dengue_spread_title5 = (TextView) view.findViewById(R.id.txt_dengue_spread_title5);
		txt_dengue_spread_para1 = (TextView) view.findViewById(R.id.txt_dengue_spread_para1);
		txt_dengue_spread_para2 = (TextView) view.findViewById(R.id.txt_dengue_spread_para2);
		txt_dengue_spread_para3 = (TextView) view.findViewById(R.id.txt_dengue_spread_para3);
		txt_dengue_spread_para4 = (TextView) view.findViewById(R.id.txt_dengue_spread_para4);
		txt_dengue_spread_para5 = (TextView) view.findViewById(R.id.txt_dengue_spread_para5);
		txt_dengue_spread_para6 = (TextView) view.findViewById(R.id.txt_dengue_spread_para6);
		
		//Enable html-features for text-view
		txt_dengue_spread_para2.setText(Html.fromHtml(getString(R.string.dengue_spread_para2)));
		txt_dengue_spread_para3.setText(Html.fromHtml(getString(R.string.dengue_spread_para3)));

		//Set the font
		txt_dengue_spread_title1.setTypeface(font, Typeface.BOLD);
		txt_dengue_spread_title2.setTypeface(font, Typeface.BOLD);
		txt_dengue_spread_title3.setTypeface(font, Typeface.BOLD);
		txt_dengue_spread_title4.setTypeface(font, Typeface.BOLD);
		txt_dengue_spread_title5.setTypeface(font, Typeface.BOLD);
		txt_dengue_spread_para1.setTypeface(font);
		txt_dengue_spread_para2.setTypeface(font);
		txt_dengue_spread_para3.setTypeface(font);
		txt_dengue_spread_para4.setTypeface(font);
		txt_dengue_spread_para5.setTypeface(font);
		txt_dengue_spread_para6.setTypeface(font);

		return view;
	}

	@Override
	public void onResume(){
	    super.onResume();
	    GlobalIO.writeLog(getActivity(), TAG, LogAction.RESUME, true);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    GlobalIO.writeLog(getActivity(), TAG, LogAction.PAUSE, true);
	}
		
	@Override
	public void setUserVisibleHint(boolean isFragmentVisible) {
		super.setUserVisibleHint(isFragmentVisible);

		if (isFragmentVisible) {
			GlobalIO.writeLog(getActivity(), TAG, LogAction.SHOW, true);
		}
	}

}
