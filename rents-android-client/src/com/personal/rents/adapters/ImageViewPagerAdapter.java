package com.personal.rents.adapters;

import java.util.List;

import com.personal.rents.fragments.RentPictureFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
	
	List<String> pictureIds;
	
	public ImageViewPagerAdapter(FragmentManager fragmentManager, List<String> pictureIds) {
		super(fragmentManager);
		
		this.pictureIds = pictureIds;
	}

	@Override
	public Fragment getItem(int position) {
		return RentPictureFragment.getNewInstance(pictureIds.get(position));
	}

	@Override
	public int getCount() {
		return pictureIds.size();
	}

}
