package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class UiAbout extends Fragment {

	private String TAG = UiAbout.class.getName();
	ImageButton ibut_mobuzz_about, ibut_mobuzz_using, ibut_mobuzz_faq;
	View fv;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	//Inflate the layout for this fragment
    	//Initialization of components 
    	fv = inflater.inflate(R.layout.fragment_ui_about, container, false);
    	
    	ibut_mobuzz_about = (ImageButton) fv.findViewById(R.id.ibut_mobuzz_about);
    	ibut_mobuzz_faq = (ImageButton) fv.findViewById(R.id.ibut_mobuzz_faq);
    	ibut_mobuzz_using = (ImageButton) fv.findViewById(R.id.ibut_mobuzz_using);
    	
    	ibut_mobuzz_about_Click();
    	ibut_mobuzz_faq_Click();
    	ibut_mobuzz_using_Click();
    	
    	GlobalIO.writeLog(getActivity(), TAG, LogAction.SHOW, true);
    	
		//For language translation
		SharedPreferences pref = this.getActivity().getSharedPreferences(GlobalVariables.MY_PREF, 0);
		String pref_lng = pref.getString("language", "");
		
		if(("sinhala").equalsIgnoreCase(pref_lng))
		{
			GlobalMethods.changeLang(this.getActivity(), "sn", "LK");
		}
		else if(("tamil").equalsIgnoreCase(pref_lng))
		{
			GlobalMethods.changeLang(this.getActivity(), "tm");
		}
		else
		{
			GlobalMethods.changeLang(this.getActivity(), "en");
		}
    	
        return fv;
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
	
    
	private void ibut_mobuzz_about_Click() {

		ibut_mobuzz_about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), UiAboutMobuzzCosmic.class);
				startActivity(intent);
			}
		});
	}
	
	private void ibut_mobuzz_faq_Click() {

		ibut_mobuzz_faq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), UiAboutMobuzzFaq.class);
				startActivity(intent);
			}
		});
	}	
	
	private void ibut_mobuzz_using_Click() {

		ibut_mobuzz_using.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), UiAboutMobuzzInformation.class);
				startActivity(intent);
			}
		});
	}
	
}
