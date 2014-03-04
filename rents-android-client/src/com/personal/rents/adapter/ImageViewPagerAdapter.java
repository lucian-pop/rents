package com.personal.rents.adapter;

import java.util.List;

import com.personal.rents.fragment.RentImageFragment;
import com.personal.rents.model.RentImage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
	
	List<RentImage> images;
	
	public ImageViewPagerAdapter(FragmentManager fragmentManager, List<RentImage> images) {
		super(fragmentManager);
		
		this.images = images;
	}

	@Override
	public Fragment getItem(int position) {
		return RentImageFragment.getNewInstance(images.size() > 0 
				? images.get(position) : null);
	}

	@Override
	public int getCount() {
		return images.size() > 0 ? images.size() : 1;
	}
}
