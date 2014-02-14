package com.personal.rents.adapter;

import java.util.List;

import com.personal.rents.fragment.RentImageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
	
	List<String> imageURIs;
	
	public ImageViewPagerAdapter(FragmentManager fragmentManager, List<String> imageURIs) {
		super(fragmentManager);
		
		this.imageURIs = imageURIs;
	}

	@Override
	public Fragment getItem(int position) {
		return RentImageFragment.getNewInstance(imageURIs.size() > 0 
				? imageURIs.get(position) : null);
	}

	@Override
	public int getCount() {
		return imageURIs.size() > 0 ? imageURIs.size() : 1;
	}
}
