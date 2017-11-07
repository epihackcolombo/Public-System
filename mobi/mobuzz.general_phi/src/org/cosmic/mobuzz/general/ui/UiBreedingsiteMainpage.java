package org.cosmic.mobuzz.general.ui;

import java.util.List;
import java.util.Vector;

import org.cosmic.mobuzz.general.phi.R;
import org.cosmic.mobuzz.general.ui.support.BreedingsiteAdapter;
import org.cosmic.mobuzz.general.util.GlobalIO;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.LogAction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class UiBreedingsiteMainpage extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	Context context = UiBreedingsiteMainpage.this;
	private TabHost tabHost;
	private ViewPager viewPager;
	private BreedingsiteAdapter pagerAdapter;
	HorizontalScrollView horzscroll;
	
	private final String TAG = UiBreedingsiteMainpage.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ui_breedingsite_mainpage);

		//Initialization of components 
		getActionBar().hide();

		this.initialiseTabHost(savedInstanceState);
		this.intialiseViewPager();

		viewPager.setCurrentItem(0);
		
		GlobalIO.writeLog(context, TAG, LogAction.START, true);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		//'onActivityResult' for fragments
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
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	//Manage scroll-bar horizontal placement
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		View tabView = tabHost.getTabWidget().getChildAt(position); //Get the current view
		horzscroll = (HorizontalScrollView) findViewById(R.id.horzscrollbreedingsite);
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
	}

	@Override
	public void onBackPressed(){
		super.onBackPressed();
		
		GlobalIO.writeLog(context, TAG, LogAction.END, true);
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		GlobalMethods.cleanMemory();
	}

	
	//------------- Implementation -------------//

	//Initialization of tab components and resources. Set the size of the tab based on the resolution(dpi) of the physical screen.
	private void initialiseTabHost(Bundle args) {

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		Resources ressources = getResources();

		UiBreedingsiteMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("1").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_breedingsite_report)));
		UiBreedingsiteMainpage.AddTab(this, this.tabHost,  this.tabHost.newTabSpec("2").setIndicator("", ressources.getDrawable(R.drawable.xml_icon_breedingsite_history)));

		for(int i=0; i<2; i++)
		{
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
		}

		tabHost.setOnTabChangedListener(this);
	}


	private static void AddTab(FragmentManager supportFragmentManager, TabHost tabHost2, TabSpec setIndicator) {
		// TODO Auto-generated method stub
		
	}


	private static void AddTab(UiBreedingsiteMainpage activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	//Initialization of tab-pages and resources.
	private void intialiseViewPager() {
		List<Fragment> fragments = new Vector<Fragment>();

		fragments.add(Fragment.instantiate(this, UiBreedingsiteSubpageReport.class.getName()));
		fragments.add(Fragment.instantiate(this, UiBreedingsiteSubpageHistory.class.getName()));

		this.pagerAdapter = new BreedingsiteAdapter(super.getSupportFragmentManager(), fragments);

		this.viewPager = (ViewPager) super.findViewById(R.id.vwp_breedingsite);
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
















