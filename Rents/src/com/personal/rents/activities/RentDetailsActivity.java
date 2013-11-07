package com.personal.rents.activities;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.R;
import com.personal.rents.adapters.ImageViewPagerAdapter;
import com.personal.rents.utils.ActivitiesContract;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RentDetailsActivity extends ActionBarActivity {
	
	private static final int MAX_NO_LINES = 2;
	
	private int fromActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent_details_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}
		
		if(bundle != null) {
			fromActivity = bundle.getInt(ActivitiesContract.FROM_ACTIVITY);
		}
		
		init();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rent_details_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			Intent intent;
			if(fromActivity == ActivitiesContract.RENTS_LIST_ACTIVITY){
				intent = new Intent(this, RentsListActivity.class);
			} else if(fromActivity ==  ActivitiesContract.RENTS_MAP_ACTIVITY){
				intent = new Intent(this, RentsMapActivity.class);
			} else {
				intent = new Intent(this, UserAddedRentsActivity.class);
			}

			NavUtils.navigateUpTo(this, intent);
			
			return true;
		} else if(item.getItemId() == R.id.user_account_action) {
			Intent intent = new Intent(this, LoginActivity.class);
			
			startActivity(intent);
			
			return true;
		} else if(item.getItemId() == R.id.search_action) {
			Intent intent = new Intent(this, FilterSearchActivity.class);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, fromActivity);
			
			startActivity(intent);
		}
		
		return false;
	}

	private void init() {
		setupActionBar();
		
		setupPicturePager();
		
		setupPhoneNumberTextBtn();
		
		setupDescSection();
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Detalii chirie");
	}
	
	private void setupPicturePager() {
		List<String> pictureIds = new ArrayList<String>();
		for(int i=0; i<5; i++) {
			pictureIds.add("");
		}
		
		ViewPager rentPicturePager = (ViewPager) findViewById(R.id.rent_picture_pager);
		rentPicturePager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager(), pictureIds));
		rentPicturePager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 v.getParent().requestDisallowInterceptTouchEvent(true);
		            return false;
			}
	    });
	}
	
	private void setupPhoneNumberTextBtn() {
		final TextView phoneNumberTextBtn = (TextView)findViewById(R.id.rent_call_btn);
		phoneNumberTextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneNumberUri = "tel:" + phoneNumberTextBtn.getText().toString().trim();
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse(phoneNumberUri));
				startActivity(intent);
			}
		});
	}
	
	private void setupDescSection() {
		final ImageButton moreDescBtn = (ImageButton) findViewById(R.id.more_desc);
		final ImageButton lessDescBtn = (ImageButton) findViewById(R.id.less_desc);
		final TextView descriptionTextView = (TextView) findViewById(R.id.rent_desc);
		moreDescBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				descriptionTextView.setMaxLines(Integer.MAX_VALUE);
				descriptionTextView.setEllipsize(null);
				moreDescBtn.setVisibility(View.INVISIBLE);
				lessDescBtn.setVisibility(View.VISIBLE);
			}
		});
		
		lessDescBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				descriptionTextView.setMaxLines(MAX_NO_LINES);
				moreDescBtn.setVisibility(View.VISIBLE);
				lessDescBtn.setVisibility(View.INVISIBLE);
			}
		});
	}
}
