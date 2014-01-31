package com.personal.rents.activity;

import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.fragment.RentsListFragment;
import com.personal.rents.task.GetRentsNextPageByMapBoundariesAsyncTask;
import com.personal.rents.util.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class RentsListActivity extends ActionBarActivity {
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private VisibleRegion visibleRegion;
	
	private int totalNoOfRents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rents_list_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}

		init(bundle);
	}
	
	private void init(Bundle bundle) {
		if(bundle != null) {
			mapCenterLatitude = bundle.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = bundle.getDouble(ActivitiesContract.LONGITUDE);
			visibleRegion = bundle.getParcelable(ActivitiesContract.VISIBLE_REGION);		
			totalNoOfRents = bundle.getInt(ActivitiesContract.NO_OF_RENTS);
		}

		setupActionBar();
		
		RentsListFragment rentsListFragment = (RentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
		rentsListFragment.setLoadNextPageTask(
				new GetRentsNextPageByMapBoundariesAsyncTask(visibleRegion));
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putDouble(ActivitiesContract.LATITUDE, mapCenterLatitude);
		outState.putDouble(ActivitiesContract.LONGITUDE, mapCenterLongitude);
		outState.putParcelable(ActivitiesContract.VISIBLE_REGION, visibleRegion);
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfRents);
		
		super.onSaveInstanceState(outState);
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(totalNoOfRents +  " oferte gasite");
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
					ActivitiesContract.RENTS_LIST_ACTIVITY);

			startActivity(intent);

			return true;
		} else if(item.getItemId() == R.id.user_account_action) {
			Intent intent = new Intent(this, UserAddedRentsActivity.class);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, 
					ActivitiesContract.RENTS_LIST_ACTIVITY);

			startActivity(intent);
			
			return true;
		}
		
		return false;
	}
	
	public void onRetryLoadNextPageBtnClick(View view) {
		((RentsListFragment) getSupportFragmentManager().findFragmentById(R.id.rents_list_fragment))
			.retryLoadingNextPage();
	}
}
