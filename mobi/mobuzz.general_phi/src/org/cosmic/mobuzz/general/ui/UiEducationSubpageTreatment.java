package org.cosmic.mobuzz.general.ui;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.network.Connectivity;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;
import org.cosmic.mobuzz.general.util.LogAction;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class UiEducationSubpageTreatment extends Fragment {
	
	Typeface font;
	private TextView txt_dengue_treatment_title1,
	txt_dengue_treatment_title2,
	txt_dengue_treatment_para1,
	txt_dengue_treatment_para2,
	txt_dengue_treatment_para3,
	txt_dengue_treatment_para4,
	txt_dengue_treatment_para5,
	txt_dengue_treatment_para6,
	txt_link_hospital;
	
	private WebView webv_dengue_treatment;
	private String web_url = "https://maps.google.lk/maps?t=m&sll=6.9215305,79.8648533&sspn=0.2617848,0.351645&q=colombo+hospital&output=classic&dg=ntvb"; //use the web_data instead
	private String web_data = "<html><body><div style='width: 100%; overflow: hidden; height: 100%; text-align: center;'>Connecting...<iframe width='100%' height='126%' frameborder='0' scrolling='no' style='border:0; margin-top: -150px;' src='https://maps.google.lk/maps?t=m&amp;q=colombo+hospital&amp;ie=UTF8&amp;hq=hospital&amp;hnear=Colombo,+Western+Province&amp;ll=6.916856,79.866862&amp;spn=0.048534,0.032677&amp;output=embed&amp;z=14'></iframe></div></body></html>";
	private final String TAG = UiEducationSubpageTreatment.class.getName();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ui_education_subpage_treatment, container, false);
		
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
		
		//Initialization of components
		txt_dengue_treatment_title1 = (TextView)view.findViewById(R.id.txt_dengue_treatment_title1);
		txt_dengue_treatment_title2 = (TextView)view.findViewById(R.id.txt_dengue_treatment_title2);
		txt_dengue_treatment_para1 = (TextView)view.findViewById(R.id.txt_dengue_treatment_para1);
		txt_dengue_treatment_para2 = (TextView)view.findViewById(R.id.txt_dengue_treatment_para2);
		txt_dengue_treatment_para3 = (TextView)view.findViewById(R.id.txt_dengue_treatment_para3);
		txt_dengue_treatment_para4 = (TextView)view.findViewById(R.id.txt_dengue_treatment_para4);
		txt_dengue_treatment_para5 = (TextView)view.findViewById(R.id.txt_dengue_treatment_para5);
		txt_dengue_treatment_para6 = (TextView)view.findViewById(R.id.txt_dengue_treatment_para6);
		txt_link_hospital = (TextView)view.findViewById(R.id.txt_link_hospital);
				
		webv_dengue_treatment = (WebView)view.findViewById(R.id.webv_dengue_treatment);

		//Set the font
		txt_dengue_treatment_title1.setTypeface(font, Typeface.BOLD);
		txt_dengue_treatment_title2.setTypeface(font, Typeface.BOLD);
		txt_dengue_treatment_para1.setTypeface(font);
		txt_dengue_treatment_para2.setTypeface(font);
		txt_dengue_treatment_para3.setTypeface(font);
		txt_dengue_treatment_para4.setTypeface(font);
		txt_dengue_treatment_para5.setTypeface(font);
		txt_dengue_treatment_para6.setTypeface(font); 
		txt_link_hospital.setTypeface(font); 
		
		txt_link_hospital.setMovementMethod(LinkMovementMethod.getInstance());
		
		InitVewView();
		
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

	//Add web view to the layout
	private void InitVewView() {

	    webv_dengue_treatment.setWebViewClient(new webvHospical());
		
	    webv_dengue_treatment.getSettings().setLoadsImagesAutomatically(true);
	    webv_dengue_treatment.getSettings().setJavaScriptEnabled(true);
	    webv_dengue_treatment.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	    
    	if(Connectivity.NetworkConnectivity(this.getActivity(), false))
    	{
    		txt_dengue_treatment_title2.setVisibility(View.VISIBLE);
    		webv_dengue_treatment.setVisibility(View.VISIBLE);
    		txt_link_hospital.setVisibility(View.VISIBLE);
    		
    		//webv_dengue_treatment.loadUrl(web_url);
    		webv_dengue_treatment.loadData(web_data, "text/html", "UTF-8");
    	}
    	else
    	{
    		txt_dengue_treatment_title2.setVisibility(View.GONE);
    		webv_dengue_treatment.setVisibility(View.GONE);
    		txt_link_hospital.setVisibility(View.GONE);
    	}
	    
	    
	}
	
	//Implement the web view
	private class webvHospical extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {

	        if (Uri.parse(url).getHost().equals(web_url)) {
	            return false;
	        }
	        else
	        {
	        	return true;
	        }
	    }
	    
	    @Override
	    public void onPageFinished(WebView view, String url) {
	        super.onPageFinished(view, url);
	    }

	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        super.onPageStarted(view, url, favicon);
	    }

	    @Override
	    public void onLoadResource(WebView view, String url) {
	        super.onLoadResource(view, url);

	    }
	}
	
	//Check the network status
	public boolean isNetworkAvailable() 
	{	
	    ConnectivityManager cm = (ConnectivityManager)  this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();

	    if (networkInfo != null && networkInfo.isConnected()) 
	    {
	        return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
	
}
