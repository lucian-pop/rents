package com.personal.rents.activity;

import java.util.ArrayList;

import com.android.volley.toolbox.NetworkImageView;
import com.personal.rents.R;
import com.personal.rents.adapter.ImageViewPagerAdapter;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;
import com.personal.rents.rest.util.GoogleMapsUtil;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.task.AddRentToFavoritesAsyncTask;
import com.personal.rents.task.GetRentAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.GeneralConstants;
import com.personal.rents.webservice.response.ResponseStatusReason;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RentDetailsActivity extends BaseActivity {
	
	private int rentId;
	
	private Rent rent;
	
	private String fromActivity;
	
	private static final int MAX_NO_LINES = 2;
	
	private boolean addToFavoritesTaskInProgress;
	
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

	private void init() {
		setupActionBar();
		
		setupPhoneNumberTextBtn();
		
		setupDescSection();
		
		setupProgressBarFragment();
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Detalii chirie");
	}
	
	private void setupImagePager() {
		final ViewPager rentImagePager = (ViewPager) findViewById(R.id.rent_image_pager);
		rentImagePager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager(),
				rent.rentImages));
		final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(
				RentDetailsActivity.this, new GestureDetector.SimpleOnGestureListener() {
					  @Override
				        public boolean onSingleTapConfirmed(MotionEvent e) {
				        	Intent intent = new Intent(RentDetailsActivity.this,
				        			RentImageFullScreenActivity.class);
				        	intent.putExtra(ActivitiesContract.SELECTED_IMG_POSITION,
				        			rentImagePager.getCurrentItem());
				        	intent.putParcelableArrayListExtra(ActivitiesContract.IMAGES,
				        			(ArrayList<RentImage>) rent.rentImages);
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

	private void restoreSavedInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		rentId = bundle.getInt(ActivitiesContract.RENT_ID);
		rent = bundle.getParcelable(ActivitiesContract.RENT);
		fromActivity = bundle.getString(ActivitiesContract.FROM_ACTIVITY);
		addToFavoritesTaskInProgress = bundle.getBoolean(ActivitiesContract.TASK_IN_PROGRESS,
				false);
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(addToFavoritesTaskInProgress) {
			startAddRentToFavoritesAsyncTask();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(rent == null) {
			startGetRentAsyncTask();
		} else {
			showRentDetails();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ActivitiesContract.RENT_ID, rentId);
		outState.putParcelable(ActivitiesContract.RENT, rent);
		outState.putString(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, addToFavoritesTaskInProgress);
		
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
		if(fromActivity.equals(UserAddedRentsActivity.class.getSimpleName())) {
			getMenuInflater().inflate(R.menu.rent_details_menu_with_edit, menu);
		} else {
			getMenuInflater().inflate(R.menu.rent_details_menu_with_fav, menu);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			Intent intent;
			if(fromActivity.equals(RentsListActivity.class.getSimpleName())){
				intent = new Intent(this, RentsListActivity.class);
			} else if(fromActivity.equals(RentsMapActivity.class.getSimpleName())){
				intent = new Intent(this, RentsMapActivity.class);
			} else if(fromActivity.equals(UserAddedRentsActivity.class.getSimpleName())){
				intent = new Intent(this, UserAddedRentsActivity.class);
			} else {
				intent = new Intent(this, UserFavoriteRentsActivity.class);
			}

			NavUtils.navigateUpTo(this, intent);
			
			return true;
		} else if(item.getItemId() == R.id.add_to_favorites_action) {
			startAddRentToFavoritesAsyncTask();
			
			return true;
		} else if(item.getItemId() == R.id.user_account_action) {
			Intent intent = new Intent(this, LoginActivity.class);
			
			startActivity(intent);
			
			return true;
		} else if(item.getItemId() == R.id.edit_rent_action) {
			Intent intent = new Intent(this, EditRentActivity.class);
			intent.putExtra(ActivitiesContract.RENT_ID, rentId);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, fromActivity);

			startActivity(intent);
		}
		
		return false;
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
	
	private void startAddRentToFavoritesAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		Account account = UserAccountManager.getAccount(this);
		if(account == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			
			return;
		}

		AddRentToFavoritesAsyncTask addRentToFavoritesAsyncTask = new AddRentToFavoritesAsyncTask();
		progressBarFragment.setTask(addRentToFavoritesAsyncTask);
		progressBarFragment.setOnTaskFinishListener(new OnAddRentToFavoritesTaskFinishListener());
		addRentToFavoritesAsyncTask.execute(rent.rentId, account);
		addToFavoritesTaskInProgress = true;
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
	
	public void onShareBtnClick(View view) {
		NetworkImageView imageView = (NetworkImageView) findViewById(R.id.rent_img);
		BitmapDrawable bmpDrawable = (BitmapDrawable) imageView.getDrawable();
		
		// Store the image in the media store
		String url = Images.Media.insertImage(this.getContentResolver(), bmpDrawable.getBitmap(),
				"Image to delete", "Some description");

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		Uri uri = Uri.parse(url);
		sharingIntent.setType("*/*");
		sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
		sharingIntent.putExtra(Intent.EXTRA_TITLE, "Photo title");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Photo Subject");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, "Photo text");
		startActivityForResult(Intent.createChooser(sharingIntent, "Share image using"), 9999);
		
		// Delete the image onActivityResult based on title.
		// An async task could be started to start the deletion let's say after 5 seconds.
		
		// Also tweak it to get the best visual results
		
		/****Another alternative would be to use the Facebook Share API*****/
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
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, RentDetailsActivity.this);

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
	
	private class OnAddRentToFavoritesTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String SUCCESS_MSG = "Chiria a fost salvata";
		
		private static final String ALREADY_ADDED_MSG = "Chiria este deja salvata";

		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			addToFavoritesTaskInProgress = false;

			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, RentDetailsActivity.this);

				return;
			}
			
			Boolean added = (Boolean) result;
			if(added) {
				Toast.makeText(RentDetailsActivity.this, SUCCESS_MSG, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(RentDetailsActivity.this, ALREADY_ADDED_MSG, Toast.LENGTH_LONG)
					.show();
			}
		}
	}
}
