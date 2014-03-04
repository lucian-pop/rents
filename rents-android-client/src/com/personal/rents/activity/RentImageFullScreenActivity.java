package com.personal.rents.activity;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.R;
import com.personal.rents.adapter.ImageViewPagerAdapter;
import com.personal.rents.model.RentImage;
import com.personal.rents.util.ActivitiesContract;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

public class RentImageFullScreenActivity extends FragmentActivity {
	
	private int selectedImagePosition;
	
	private List<RentImage> images;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent_image_fullscreen_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}
		
		restoreInstanceState(bundle);
		
		setupImagePager();
	}
	
	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		selectedImagePosition = bundle.getInt(ActivitiesContract.SELECTED_IMG_POSITION);
		images = bundle.getParcelableArrayList(ActivitiesContract.IMAGES);
	}
	
	private void setupImagePager() {
		ViewPager rentImagePager = (ViewPager) findViewById(R.id.rent_image_pager);
		rentImagePager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager(), images));
		rentImagePager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 v.getParent().requestDisallowInterceptTouchEvent(true);
				 return false;
			}
	    });
		rentImagePager.setCurrentItem(selectedImagePosition);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ActivitiesContract.SELECTED_IMG_POSITION, selectedImagePosition);
		outState.putParcelableArrayList(ActivitiesContract.IMAGES, (ArrayList<RentImage>) images);
		
		super.onSaveInstanceState(outState);
	}
	
	public void onCloseImageBtnClick(View view) {
		finish();
	}
}
