package com.personal.rents.activity;

import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.fragment.RentsListFragment;
import com.personal.rents.model.RentSearch;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.GetRentsNextPageByMapBoundariesAsyncTask;
import com.personal.rents.task.RentsSearchAsyncTask;
import com.personal.rents.task.RentsSearchNextPageAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class RentsListActivity extends BaseActivity {
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private double placeLatitude;
	
	private double placeLongitude;
	
	private VisibleRegion visibleRegion;
	
	private int totalNoOfRents;
	
	private boolean startRentsSearch;
	
	private RentSearch rentSearch;

	private ProgressBarFragment progressBarFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rents_list_activity_layout);
		
		Log.e("TEST_TAG", "*********Rents list activity: On CREATE called**************");
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}

		restoreInstanceState(bundle);
		
		init();
	}
	
	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}

		mapCenterLatitude = bundle.getDouble(ActivitiesContract.LATITUDE);
		mapCenterLongitude = bundle.getDouble(ActivitiesContract.LONGITUDE);
		visibleRegion = bundle.getParcelable(ActivitiesContract.VISIBLE_REGION);		
		totalNoOfRents = bundle.getInt(ActivitiesContract.NO_OF_RENTS);
		startRentsSearch = bundle.getBoolean(ActivitiesContract.START_RENTS_SEARCH, false);
		placeLatitude = bundle.getDouble(ActivitiesContract.PLACE_LATITUDE);
		placeLongitude = bundle.getDouble(ActivitiesContract.PLACE_LONGITUDE);
		rentSearch = bundle.getParcelable(ActivitiesContract.RENT_SEARCH);
	}
	
	private void init() {
		setupActionBar();
		setupListFragment();
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(totalNoOfRents +  " oferte gasite");
	}
	
	private void setupListFragment() {
		RentsListFragment rentsListFragment = (RentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
		rentsListFragment.setListItemLayoutId(R.layout.rents_list_item_layout);
		rentsListFragment.setLoadNextPageTask(
				new GetRentsNextPageByMapBoundariesAsyncTask(visibleRegion));
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		Log.e("TEST_TAG", "*********Rents list activity: On START called**************");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TEST_TAG", "*********On ACTIVITY RESULT called**********");
		switch (requestCode) {
			case  ActivitiesContract.RENTS_SEARCH_REQ_CODE:
				if(resultCode == RESULT_OK) {
					startRentsSearch = true;
					placeLatitude = data.getDoubleExtra(ActivitiesContract.PLACE_LATITUDE, 0.0);
					placeLongitude = data.getDoubleExtra(ActivitiesContract.PLACE_LONGITUDE, 0.0);
					rentSearch = data.getParcelableExtra(ActivitiesContract.RENT_SEARCH);
				}
				
				break;
			default:
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(rentSearch != null && startRentsSearch) {
			setupProgressBarFragment();
			startRentsSearchAsyncTask();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putDouble(ActivitiesContract.LATITUDE, mapCenterLatitude);
		outState.putDouble(ActivitiesContract.LONGITUDE, mapCenterLongitude);
		outState.putParcelable(ActivitiesContract.VISIBLE_REGION, visibleRegion);
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfRents);
		outState.putBoolean(ActivitiesContract.START_RENTS_SEARCH, startRentsSearch);
		outState.putDouble(ActivitiesContract.PLACE_LATITUDE, placeLatitude);
		outState.putDouble(ActivitiesContract.PLACE_LONGITUDE, placeLongitude);
		outState.putParcelable(ActivitiesContract.RENT_SEARCH, rentSearch);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e("TEST_TAG", "*********Rents list activity: On STOP called**************");
		
		if(progressBarFragment != null) {
			progressBarFragment.reset();
			progressBarFragment.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("TEST_TAG", "*********Rents list activity: On DESTROY called**************");
		
		progressBarFragment = null;
	}
	
	private void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}

		progressBarFragment.setOnTaskFinishListener(new OnRentsSearchTaskFinishListener());
	}
	
	private void startRentsSearchAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		RentsSearchAsyncTask rentsSearchAsyncTask = new RentsSearchAsyncTask();
		progressBarFragment.setTask(rentsSearchAsyncTask);
		rentsSearchAsyncTask.execute(rentSearch, visibleRegion);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rents_list_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.search_action) {
			Intent intent = new Intent(this, FilterSearchActivity.class);
			intent.putExtra(ActivitiesContract.LATITUDE, mapCenterLatitude);
			intent.putExtra(ActivitiesContract.LONGITUDE, mapCenterLongitude);
			intent.putExtra(ActivitiesContract.VISIBLE_REGION, visibleRegion);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, 
					this.getClass().getSimpleName());

			startActivityForResult(intent, ActivitiesContract.RENTS_SEARCH_REQ_CODE);

			return true;
		} else if(item.getItemId() == R.id.user_account_action) {
			Intent intent = new Intent(this, UserFavoriteRentsActivity.class);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, this.getClass().getSimpleName());

			startActivity(intent);
			
			return true;
		}
		
		return false;
	}
	
	public void onRetryLoadNextPageBtnClick(View view) {
		((RentsListFragment) getSupportFragmentManager().findFragmentById(R.id.rents_list_fragment))
			.retryLoadingNextPage();
	}
	
	private class OnRentsSearchTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String NO_RENTS_FOUND_MSG = 
				"Nu am gasit chirii corespunzatoare cautarii dvs.";
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			startRentsSearch = false;

			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, RentsListActivity.this);

				return;
			}

			if(result == null) {
				Toast.makeText(RentsListActivity.this, NO_RENTS_FOUND_MSG, Toast.LENGTH_LONG)
					.show();
				
				return;
			}
			
			totalNoOfRents = ((RentsCounter) result).counter;
			getSupportActionBar().setTitle(totalNoOfRents + " chirii au fost gasite");

			RentsListFragment rentsListFragment = (RentsListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.rents_list_fragment);
			rentsListFragment.setupListAdapter(((RentsCounter) result).rents, totalNoOfRents,
					new RentsSearchNextPageAsyncTask(rentSearch), R.layout.rents_list_item_layout);
		}
	}
}
