package com.personal.rents.activities;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.activities.components.RentMarkerInfoWindowAdapter;
import com.personal.rents.activities.helpers.LocationHelper;
import com.personal.rents.activities.workers.AddMarkersWorker;
import com.personal.rents.fragments.helpers.TouchableRentsMapWrapper.PanningListener;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class RentsMapActivity extends ActionBarActivity implements OnMyLocationButtonClickListener,
		PanningListener, OnMarkerClickListener {

    private SupportMapFragment rentsMapFragment;
	
    public GoogleMap rentsMap;

    private LocationHelper locationHelper;

    private boolean cameraChangedPosition = false;
    
    private boolean panningFinished = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rents_map_layout);

        locationHelper = new LocationHelper(getApplicationContext());
        rentsMapFragment = (SupportMapFragment) getSupportFragmentManager()
        		.findFragmentById(R.id.rents_map);
        if (savedInstanceState == null) {
            rentsMapFragment.setRetainInstance(true);
        } else {
        	rentsMap = rentsMapFragment.getMap();
        }
  
        setUpMapIfNeeded();
        
        // Add code in onSaveInstanceState() to save user location and use it at activity recreation
        // (create, onRestoreInstanceState). Use this location if no other one is available.
    }

	@Override
	protected void onStart() {
		super.onStart();
	}

    @Override
	protected void onStop() {
		super.onStop();
	}
    
	private void setUpMapIfNeeded() {
		if (rentsMap == null) {
			rentsMap = rentsMapFragment.getMap();
	        if (rentsMap != null) {
	        	setUpMap();
	        }
		}
	}
	
	private void setUpMap() {
		rentsMap.setMyLocationEnabled(true);
        rentsMap.setOnMyLocationButtonClickListener(this);
        rentsMap.setInfoWindowAdapter(new RentMarkerInfoWindowAdapter(this));
        rentsMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {			
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				if(panningFinished) {
					addMarkers();
					panningFinished = false;
				} else {
					cameraChangedPosition = true;
				}
			}
		});

        Location location = locationHelper.getLastKnownLocation();
        if(location != null) {
        	locationHelper.moveToLocation(location, rentsMap);
        }
	}

	@Override
	public void onPanningStarted() {
		panningFinished = false;
	}

	@Override
	public void onPanningFinished() {
		if(cameraChangedPosition) {
			addMarkers();
			cameraChangedPosition = false;
		}
		
		panningFinished = true;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		panningFinished = true;
		cameraChangedPosition = false;

		return false;
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		locationHelper.moveToLocation(marker.getPosition(), rentsMap);
		return false;
	}
	
	private void addMarkers() {
		rentsMap.clear();
		VisibleRegion visibleRegion = rentsMap.getProjection().getVisibleRegion();
		AddMarkersWorker addMarkersWorker = new AddMarkersWorker(rentsMap, this);
		addMarkersWorker.execute(visibleRegion);
	}
}