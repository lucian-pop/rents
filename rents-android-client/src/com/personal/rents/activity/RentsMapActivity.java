package com.personal.rents.activity;

import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.adapter.RentMarkerInfoWindowAdapter;
import com.personal.rents.fragment.RentsMapFragment;
import com.personal.rents.logic.LocationManagerWrapper;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.RentMarkerBuilder;
import com.personal.rents.view.TouchableMapView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class RentsMapActivity extends ActionBarActivity implements OnInfoWindowClickListener {

	private static final long EVENTS_DELAY = 250L;
	
    private RentsMapFragment rentsMapFragment;
	
    public GoogleMap rentsMap;

    private LocationManagerWrapper locationHelper;
    
    private LatLng lastCenterPosition;
    
    private float lastZoomLevel;
    
    private Marker lastClickedMarker;
    
    private boolean isTouched = false;
    
    private Runnable onMapChangeTask = new Runnable() {
		@Override
		public void run() {
			if(!isTouched) {
				onChange(rentsMap.getCameraPosition().target, lastCenterPosition, 
						rentsMap.getCameraPosition().zoom, lastZoomLevel);
			} else {
				resetMapChangeTimer();
			}	
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rents_map_activity_layout);
        
        getSupportActionBar().setTitle("Chirii");

        setUpMapIfNeeded();
        
        // Add code in onSaveInstanceState() to save user location and use it at activity recreation
        // (create, onRestoreInstanceState). Use this location if no other one is available.
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rents_map_menu, menu);
		
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.search_action) {
			Intent intent = new Intent(this, FilterSearchActivity.class);
			intent.putExtra(ActivitiesContract.LATITUDE, lastCenterPosition.latitude);
			intent.putExtra(ActivitiesContract.LONGITUDE, lastCenterPosition.longitude);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, 
					ActivitiesContract.RENTS_MAP_ACTIVITY);

			startActivity(intent);

			return true;
		} else if(item.getItemId() == R.id.list_rents_action) {
			Intent intent = new Intent(this, RentsListActivity.class);
			intent.putExtra(ActivitiesContract.LATITUDE, lastCenterPosition.latitude);
			intent.putExtra(ActivitiesContract.LONGITUDE, lastCenterPosition.longitude);

			startActivity(intent);
			
			return true;
		} else if(item.getItemId() == R.id.user_account_action) {
			Intent intent = new Intent(this, UserAddedRentsActivity.class);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, 
					ActivitiesContract.RENTS_MAP_ACTIVITY);

			startActivity(intent);
			
			return true;
		}

		return false;
	}

	private void setUpMapIfNeeded() {
        rentsMapFragment = (RentsMapFragment) getSupportFragmentManager()
        		.findFragmentById(R.id.rents_map);
		if (rentsMap == null) {
			rentsMap = rentsMapFragment.getMap();
	        if (rentsMap != null) {
	        	setUpMap();
	        }
		}
	}
	
	private void setUpMap() {
		View myLocationButton = rentsMapFragment.getView().findViewById(0x2);
        myLocationButton.setBackgroundResource(R.drawable.app_my_location_btn_selector);
		
		rentsMap.setMyLocationEnabled(true);
		rentsMap.getUiSettings().setMyLocationButtonEnabled(true);
		rentsMap.getUiSettings().setCompassEnabled(true);
		rentsMap.getUiSettings().setZoomControlsEnabled(false);
        
        rentsMap.setInfoWindowAdapter(new RentMarkerInfoWindowAdapter(this));
        rentsMap.setOnCameraChangeListener(new RentsMapOnCameraChangeListener());
        rentsMap.setOnMarkerClickListener(new RentsMapOnMarkerClickListener());
        rentsMap.setOnInfoWindowClickListener(this);
        
        locationHelper = new LocationManagerWrapper(getApplicationContext());
        Location location = locationHelper.getLastKnownLocation();
        if(location != null) {
        	locationHelper.moveToLocation(location, rentsMap);
        }

        ((TouchableMapView)rentsMapFragment.getView())
        		.setOnMapTouchListener(new RentsMapOnTouchListener());
        lastCenterPosition = rentsMap.getCameraPosition().target;
        lastZoomLevel = rentsMap.getCameraPosition().zoom;
	}
	
	@Override
	public void onInfoWindowClick(Marker arg0) {
		Intent intent = new Intent(this, RentDetailsActivity.class);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, ActivitiesContract.RENTS_MAP_ACTIVITY);

		startActivity(intent);
	}
	
	private void resetMapChangeTimer() {
		rentsMapFragment.getView().removeCallbacks(onMapChangeTask);
		rentsMapFragment.getView().postDelayed(onMapChangeTask, EVENTS_DELAY);
	}
	
	private void onChange(LatLng newCenter, LatLng oldCenter, float newZoom, float oldZoom) {
		lastCenterPosition = newCenter;
		lastZoomLevel = newZoom;

		AddMarkersTask task = new AddMarkersTask();
		if ((!newCenter.equals(oldCenter)) && (newZoom != oldZoom)) {
			rentsMap.clear();
			Toast.makeText(RentsMapActivity.this, "Map Zoom + Pan", Toast.LENGTH_SHORT).show();
			task.execute(rentsMap.getProjection().getVisibleRegion());
		}
		else if (!newCenter.equals(oldCenter)) {
			rentsMap.clear();
			Toast.makeText(RentsMapActivity.this, "Map Pan", Toast.LENGTH_SHORT).show();
			task.execute(rentsMap.getProjection().getVisibleRegion());
		}
		else if (newZoom != oldZoom) {
			Toast.makeText(RentsMapActivity.this, "Map Zoom", Toast.LENGTH_SHORT).show();
		}
	}
	
    private class AddMarkersTask extends AsyncTask<VisibleRegion, Void, List<Rent>> {

    	@Override
    	protected List<Rent> doInBackground(VisibleRegion... visibleRegions) {
    		VisibleRegion visibleRegion = visibleRegions[0];
    		List<Rent> rents = 
    				RentsClient.getRentsPositions(visibleRegion.latLngBounds.southwest, 
    						visibleRegion.latLngBounds.northeast);

    		return rents;
    	}

    	@Override
    	protected void onPostExecute(List<Rent> result) {
    		View rentMarkerView = 
    				((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
    					.inflate(R.layout.rent_marker_icon_layout, null);

    		Bitmap rentMarkerIcon = null;
    		for(Rent rent : result) { 
    			rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(rentMarkerView,
    					rent.price);
    			rentsMap.addMarker(new MarkerOptions().position(rent.position)
    					.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
    		}
    		rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(rentMarkerView, 500);
    		rentsMap.addMarker(new MarkerOptions().position(rentsMap.getCameraPosition().target)
    				.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
    	}
    }
    
    private class RentsMapOnCameraChangeListener implements GoogleMap.OnCameraChangeListener {
		@Override
		public void onCameraChange(CameraPosition center) {
			if(isSpanChange(center.target) || isZoomChange(center.zoom)) {
				resetMapChangeTimer();
			}
		}
    	
    	private boolean isSpanChange(LatLng newCenter) {
    		return !newCenter.equals(lastCenterPosition);
    	}
    	
    	private boolean isZoomChange(float newZoom) {
    		return (newZoom != lastZoomLevel);
    	}
    }
    
    private class RentsMapOnTouchListener implements TouchableMapView.OnMapTouchListener {
		@Override
		public void onMapTouch(boolean touched) {
			isTouched = touched;
		}
    }
    
    private class RentsMapOnMarkerClickListener implements OnMarkerClickListener {
    	@Override
    	public boolean onMarkerClick(final Marker marker) {
    		if (lastClickedMarker != null && lastClickedMarker.equals(marker)) {
                lastClickedMarker = null;
                marker.hideInfoWindow();

                return true;
            } else {
                lastClickedMarker = marker;

                return false;
            }
    	}
    }
}
