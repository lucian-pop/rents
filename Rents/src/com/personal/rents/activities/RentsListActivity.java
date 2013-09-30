package com.personal.rents.activities;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.personal.rents.R;
import com.personal.rents.fragments.RentsListFragment;
import com.personal.rents.model.Rent;
import com.personal.rents.utils.ActivitiesContract;

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
			
			// get rents from the fragment saved state.
		} else {
			bundle = getIntent().getExtras();
			mapCenterLatitude = bundle.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = bundle.getDouble(ActivitiesContract.LONGITUDE);

			// get rents from the intent.
		}
		
		init();
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
			startActivity(intent);

			return true;
		}
		
		return false;
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		rents = new ArrayList<Rent>(6);
		Rent rent = new Rent(new LatLng(10, 10), 160);
		for(int i=0; i < 6; i++) {
			rents.add(rent);
		}
		
		RentsListFragment rentsListFragment = (RentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
		rentsListFragment.setRents(rents);
	}
}
