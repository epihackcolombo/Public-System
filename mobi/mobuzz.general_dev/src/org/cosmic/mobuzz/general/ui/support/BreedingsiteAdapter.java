package org.cosmic.mobuzz.general.ui.support;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//Adapter for the civic engagement module
public class BreedingsiteAdapter extends FragmentStatePagerAdapter{

	private List<Fragment> fragments;
	
	public BreedingsiteAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {		
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}



}
