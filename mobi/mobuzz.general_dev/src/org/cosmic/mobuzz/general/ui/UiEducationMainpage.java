package org.cosmic.mobuzz.general.ui;

import java.util.List;
import java.util.Vector;

import org.cosmic.mobuzz.general.R;
import org.cosmic.mobuzz.general.ui.support.EducationAdapter;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.LogAction;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class UiEducationMainpage extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	Context context = UiEducationMainpage.this;
	private TabHost tabHost;
	private ViewPager viewPager;
	private EducationAdapter pagerAdapter;
	HorizontalScrollView horzscroll;
	
	private final String TAG = UiEducationMainpage.class.getName();
	int tab_height, tab_width_enable, tab_width_disable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_education_mainpage);

		//Initialization of components 
		tab_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics()); 
		tab_width_enable = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics()); 
		tab_width_disable = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
		getActionBar().hide();

		this.initialiseTabHost(savedInstanceState);
		this.intialiseViewPager();

		int startPage = 0; //Set the default page
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			startPage = extras.getInt("startPage", 0);
			viewPager.setCurrentItem(startPage); //Set the starting page, if the index is set
		}
				
		for(int i=0; i<6; i++) //to remove the additional apace introduced by the wrap option. This is used to have different sizes for selected and other tabs.
		{
			if(startPage==i)
			{
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().width = tab_width_enable;
			}
			else
			{
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().width = tab_width_disable;
			}
		}
		
		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	public void onResume(){
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	//Manage scroll-bar horizontal placement
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		View tabView = tabHost.getTabWidget().getChildAt(position); //Get the current view
		horzscroll = (HorizontalScrollView) findViewById(R.id.horzscrolleducation);
		if (tabView != null)
		{

			int width = horzscroll.getWidth();
			int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2; //Position the current tab according to it's physical location in the device
			horzscroll.scrollTo(scrollPos, 0);
		}
		else
		{
			horzscroll.scrollBy(positionOffsetPixels, 0); //Reset the tab position
		}
	}

	@Override
	public void onPageSelected(int position) {
		this.tabHost.setCurrentTab(position);
	}

	@Override
	public void onTabChanged(String tabId) {
		int pos = this.tabHost.getCurrentTab();
		this.viewPager.setCurrentItem(pos); //Allocate the page-viewer according to tab position
		
		for(int i=0; i<6; i++) //to remove the additional apace introduced by the wrap option. This is used to have different sizes for selected and other tabs.
		{
			if(pos==i)
			{
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().width = tab_width_enable;
			}
			else
			{
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().width = tab_width_disable;
			}
		}
	}


	//------------- Implementation -------------//

	//Initialization of tab components and resources. Set the size of the tab based on the resolution(dpi) of the physical screen.
	private void initialiseTabHost(Bundle args) {

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		Resources ressources = getResources();

		UiEducationMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("1").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_education_statistics)));
		UiEducationMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("2").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_education_dengue)));
		UiEducationMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("3").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_education_transmission)));
		UiEducationMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("4").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_education_symptoms)));
		UiEducationMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("5").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_education_treatment)));
		UiEducationMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("6").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_education_prevention)));

		for(int i=0; i<6; i++)
		{
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = tab_height;
		}

		tabHost.setOnTabChangedListener(this);
	}


	private static void AddTab(UiEducationMainpage activity, TabHost tabHost,	TabHost.TabSpec tabSpec) {
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	//Initialization of tab-pages and resources.
	private void intialiseViewPager() {
		List<Fragment> fragments = new Vector<Fragment>();

		fragments.add(Fragment.instantiate(this, UiEducationSubpageStatistics.class.getName()));
		fragments.add(Fragment.instantiate(this, UiEducationSubpageAbout.class.getName()));
		fragments.add(Fragment.instantiate(this, UiEducationSubpageSpread.class.getName()));
		fragments.add(Fragment.instantiate(this, UiEducationSubpageSymptoms.class.getName()));
		fragments.add(Fragment.instantiate(this, UiEducationSubpageTreatment.class.getName()));
		fragments.add(Fragment.instantiate(this, UiEducationSubpagePrevention.class.getName()));

		this.pagerAdapter = new EducationAdapter(super.getSupportFragmentManager(), fragments);

		this.viewPager = (ViewPager) super.findViewById(R.id.vwp_education);
		this.viewPager.setAdapter(this.pagerAdapter);
		this.viewPager.setOnPageChangeListener(this);
	}


	//------------- Implements Tab Factory -------------//

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

}
















