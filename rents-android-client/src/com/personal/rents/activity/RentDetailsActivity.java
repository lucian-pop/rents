package com.personal.rents.activity;

import java.util.ArrayList;

import com.personal.rents.R;
import com.personal.rents.adapter.ImageViewPagerAdapter;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.util.GoogleMapsUtil;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.GetRentAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.GeneralConstants;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RentDetailsActivity extends ActionBarActivity {
	
	private int rentId;
	
	private Rent rent;
	
	private static final int MAX_NO_LINES = 2;
	
	private int fromActivity;
	
	private ProgressBarFragment progressBarFragment;

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
		
		restoreSavedInstanceState(bundle);
		
		init();
	}

	private void restoreSavedInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		rentId = bundle.getInt(ActivitiesContract.RENT_ID);
		rent = bundle.getParcelable(ActivitiesContract.RENT);
		fromActivity = bundle.getInt(ActivitiesContract.FROM_ACTIVITY);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(rent == null) {
			setupProgressBarFragment();
			startGetRentAsyncTask();
		} else {
			showRentDetails();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ActivitiesContract.RENT_ID, rentId);
		outState.putParcelable(ActivitiesContract.RENT, rent);
		outState.putInt(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(progressBarFragment != null) {
			progressBarFragment.reset();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		progressBarFragment = null;
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
		
		setupPhoneNumberTextBtn();
		
		setupDescSection();
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Detalii chirie");
	}
	
	private void setupImagePager() {
		final ViewPager rentImagePager = (ViewPager) findViewById(R.id.rent_image_pager);
		rentImagePager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager(),
				rent.rentImageURIs));
		final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(
				RentDetailsActivity.this, new GestureDetector.SimpleOnGestureListener() {
					  @Override
				        public boolean onSingleTapConfirmed(MotionEvent e) {
				        	Intent intent = new Intent(RentDetailsActivity.this,
				        			RentImageFullScreenActivity.class);
				        	intent.putExtra(ActivitiesContract.SELECTED_IMG_POSITION,
				        			rentImagePager.getCurrentItem());
				        	intent.putStringArrayListExtra(ActivitiesContract.IMAGE_URIS,
				        			(ArrayList<String>)rent.rentImageURIs);
				        	startActivity(intent);

				        	return true;
				        }
				});
		rentImagePager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
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
	
	private void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}

		progressBarFragment.setOnTaskFinishListener(new OnGetRentTaskFinishListener());
	}
	
	private void startGetRentAsyncTask() {
		View rentDetailsViewGroup = findViewById(R.id.rent_details);
		rentDetailsViewGroup.setVisibility(View.GONE);
		
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetRentAsyncTask getRentTask = new GetRentAsyncTask();
		progressBarFragment.setTask(getRentTask);
		getRentTask.execute(rentId);
	}
	
	private void showRentDetails() {
		setupImagePager();
		
		View rentDetailsViewGroup = findViewById(R.id.rent_details);
		rentDetailsViewGroup.setVisibility(View.VISIBLE);
		
		TextView rentTypeDesc = (TextView) findViewById(R.id.rent_type_desc);
		rentTypeDesc.setText(getResources().getStringArray(R.array.rent_types)[rent.rentType + 1]
				+ ", " + getResources()
				.getStringArray(R.array.rent_architectures)[rent.rentArchitecture + 1] + ", "
				+ getResources().getStringArray(R.array.rent_ages)[rent.rentAge + 1]);
		
		TextView rentSpecs = (TextView) findViewById(R.id.rent_specs);
		rentSpecs.setText(rent.rentRooms + " .cam, " + rent.rentBaths + " bai, " + rent.rentSurface
				+ " " + GeneralConstants.SQUARE_METERS);
		
		TextView rentParty = (TextView) findViewById(R.id.rent_party);
		rentParty.setText(getResources().getStringArray(R.array.rent_parties)[rent.rentParty + 1]);
		
		TextView rentCallBtn = (TextView) findViewById(R.id.rent_call_btn);
		rentCallBtn.setText(rent.rentPhone);
		
		TextView rentAddress = (TextView) findViewById(R.id.rent_address);
		rentAddress.setText(rent.address.toString());
		
		TextView rentLocation = (TextView) findViewById(R.id.rent_location);
		rentLocation.setText(rent.address.addressLocality + GeneralConstants.COMMA 
				+ rent.address.addressAdmAreaL1);
		
		TextView rentDescription = (TextView) findViewById(R.id.rent_desc);
		rentDescription.setText(rent.rentDescription);
		
		CheckBox rentPetsAllowed = (CheckBox) findViewById(R.id.rent_pets_allowed);
		rentPetsAllowed.setChecked(rent.rentPetsAllowed);
	}
	
	public void onDirectionsBtnClick(View view) {
		try {
			startActivity(GoogleMapsUtil.getDirectionsIntent(rent.address));
		} catch(ActivityNotFoundException e) {
			Toast.makeText(this, "Instalati va rog Google Maps", Toast.LENGTH_LONG).show();
		}
	}
	
	public void onMapBtnClick(View view) {
		Intent intent = new Intent(this, RentMapActivity.class);
		intent.putExtra(ActivitiesContract.RENT, rent);
		
		startActivity(intent);
	}
	
	private class OnGetRentTaskFinishListener implements OnNetworkTaskFinishListener {
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, RentDetailsActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(RentDetailsActivity.this, "Chiria nu a putut fi incarcata.", 
						Toast.LENGTH_LONG).show();
				
				return;
			}
			
			rent = (Rent) result;
			
			showRentDetails();
		}
	}
}
