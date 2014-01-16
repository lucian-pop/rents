package com.personal.rents.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.personal.rents.R;
import com.personal.rents.fragment.RentsMapFragment;
import com.personal.rents.logic.LocationManagerWrapper;
import com.personal.rents.util.GeneralConstants;
import com.personal.rents.view.TouchableMapView;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends ActionBarActivity {
	
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);

        if (savedInstanceState == null) {
            // First incarnation of this activity.
            mapFragment.setRetainInstance(true);
        } else {
            // Reincarnated activity. The obtained map is the same map instance in the previous
            // activity life cycle. There is no need to reinitialize it.
            mMap = mapFragment.getMap();
        }
        setUpMapIfNeeded();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}
	
	private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
//	    if (mMap == null) {
//	    	// Programatically add map fragment
//	    	GoogleMapOptions mapOptions = new GoogleMapOptions();
//	        LocationManagerWrapper locationHelper = new LocationManagerWrapper(this);
//	    	Location location = locationHelper.getLastKnownLocation();
//	    	LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
//	    	CameraPosition cameraPosition = new CameraPosition.Builder()
//	    		.target(position)
//	    		.zoom(15)
//	    		.build();
//	    	mapOptions.camera(cameraPosition);
//	    	
//	    	getSupportFragmentManager().beginTransaction().add(R.id.map_container, 
//	    			SupportMapFragment.newInstance(mapOptions), "f_tag").commit();
//	    }

	  }

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.setOnCameraChangeListener(new CustomOnCameraChangeListener());
//	        if(location != null) {
//	        	locationHelper.moveToLocation(location, mMap);
//	        }
	}
	
    private class RentsMapOnTouchListener implements TouchableMapView.OnMapTouchListener {
		@Override
		public void onMapTouch(boolean touched) {
		}
    }

	private class CustomOnCameraChangeListener implements GoogleMap.OnCameraChangeListener {
		@Override
		public void onCameraChange(CameraPosition center) {
			Log.e("SMART_TAG", "**********Camera changed position***********");
		}
    	
    }

}
