package com.personal.rents.activity;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.R;
import com.personal.rents.fragment.RentsListFragment;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.util.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class RentsListActivity extends ActionBarActivity {
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private List<Rent> rents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rents_list_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			// get bundle from the intent (send by search activity or map activity).
			bundle = getIntent().getExtras();
		}
		
		if(bundle != null) {
			mapCenterLatitude = bundle.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = bundle.getDouble(ActivitiesContract.LONGITUDE);
			
			// populate rents from the bundle
		}

		init();
	}
	
	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("305 oferte gasite");

		rents = new ArrayList<Rent>(6);
		Rent rent = new Rent();
		rent.address = new Address();
		rent.address.addressLatitude = 10;
		rent.address.addressLongitude = 10;
		rent.rentPrice = 160;
		for(int i=0; i < 6; i++) {
			rents.add(rent);
		}
		RentsListFragment rentsListFragment = (RentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
		rentsListFragment.setRents(rents);
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
}
