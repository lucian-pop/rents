package com.personal.rents.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.personal.rents.R;
import com.personal.rents.model.Rent;
import com.personal.rents.task.AddMarkersTask;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.LocationUtil;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class RentMapActivity extends ActionBarActivity {
	
	private Rent rent;
	
	public GoogleMap rentMap;
	
	private static final int zoomLevel = 16;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent_map_activity_layout);
		
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
		
		rent = bundle.getParcelable(ActivitiesContract.RENT);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.RENT, rent);
		
		super.onSaveInstanceState(outState);
	}
	
	private void init() {
		setupActionBar();
		
		setupMap();
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void setupMap() {
		if(rentMap != null) {
			return;
		}

		SupportMapFragment rentMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rent_map);

		// Sometimes mylocationbutton is null think for a fix when you have time.
		View myLocationButton = rentMapFragment.getView().findViewById(0x2);
		if(myLocationButton != null) {
	        myLocationButton.setBackgroundResource(R.drawable.app_my_location_btn_selector);
		}

        rentMap = rentMapFragment.getMap();
        rentMap.setMyLocationEnabled(true);
		rentMap.getUiSettings().setMyLocationButtonEnabled(true);
		rentMap.getUiSettings().setCompassEnabled(true);
		rentMap.getUiSettings().setZoomControlsEnabled(false);
		
		LocationUtil.moveToLocation(new LatLng(rent.address.addressLatitude,
				rent.address.addressLongitude), zoomLevel, rentMap);
		
		AddMarkersTask.addMarker( getLayoutInflater(), rent, rentMap);
	}
}
