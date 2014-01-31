package com.personal.rents.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.adapter.RentMarkerInfoWindowAdapter;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.fragment.EnableLocationServicesDialogFragment;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.fragment.RentsMapFragment;
import com.personal.rents.logic.CameraPositionPreferencesManager;
import com.personal.rents.logic.UserPreferencesManager;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.AddMarkersTask;
import com.personal.rents.task.GetRentsByMapBoundariesAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.GeneralConstants;
import com.personal.rents.util.GoogleServicesUtil;
import com.personal.rents.util.LocationUtil;
import com.personal.rents.view.TouchableMapView;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class RentsMapActivity extends LocationActivity implements OnInfoWindowClickListener, 
		OnMyLocationButtonClickListener {
	
	private int totalNoOfRents;
	
	private List<Rent> rents;
	
    private LatLng previousCameraPosition;
	
    private float previousZoomLevel;
    
    private boolean isTouched = false;
    
    private boolean hasDelayedTasks = false;
    
    private boolean taskInProgress = false;
    
    private Handler handler;
    
    private OnCameraChangeTask onCameraChangeTask;
    
	private GoogleMap rentsMap;
	
    private Marker lastClickedMarker;
    
    private CameraPosition cameraPosition;
    
    private LayoutInflater inflater;
    
    private ProgressBarFragment progressBarFragment;
    
    private boolean servicesAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rents_map_activity_layout);
        Log.e("TEST_TAG", "**********On create called**********");
        
		setupActionBar();

		servicesAvailable = GoogleServicesUtil.servicesConnected(this);
		
		if(!servicesAvailable) {
			return;
		}
		
		restoreSavedInstanceState(savedInstanceState);
    }
	
	private void restoreSavedInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState == null) {
			Log.e("TEST_TAG", "**********No saved instance state**********");
	        return;
		}
		
		cameraPosition = 
				(CameraPosition) savedInstanceState.getParcelable(ActivitiesContract.CAMERA_POSITION);
		previousCameraPosition = (LatLng) savedInstanceState
				.getParcelable(ActivitiesContract.PREVIOUS_CAMERA_POSITION);
		previousZoomLevel = savedInstanceState.getFloat(ActivitiesContract.PREVIOUS_ZOOM_LEVEL);
		totalNoOfRents = savedInstanceState.getInt(ActivitiesContract.NO_OF_RENTS);
		rents = savedInstanceState.getParcelableArrayList(ActivitiesContract.RENTS);
		hasDelayedTasks = savedInstanceState.getBoolean(ActivitiesContract.HAS_DELAYED_TASKS);
		taskInProgress = savedInstanceState.getBoolean(ActivitiesContract.TASK_IN_PROGRESS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TEST_TAG", "*********On ACTIVITY RESULT called**********");

		setupLocationServices(new LocationListenerImpl());
		switch (requestCode) {
			case  ActivitiesContract.LOCATION_SERVICES_REQ_CODE:
				locationServicesEnabled = locationManager.isLocationServicesEnabled();
				if(locationServicesEnabled) {
					updateCameraPosition();
				}

				break;
			case GoogleServicesUtil.CONNECTION_FAILURE_RESOLUTION_REQUEST:
				if(resultCode != RESULT_OK) {
					Toast.makeText(this, GoogleServicesUtil.UNABLE_TO_RESOLVE_ERROR_MSG, 
							Toast.LENGTH_SHORT).show();
				}

				servicesAvailable = GoogleServicesUtil.checkServicesConnectedWithoutResolution(this);
				if(servicesAvailable && locationServicesEnabled){
					locationClient.connect();
				}

				break;
			default:
				break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.e("TEST_TAG", "*********On START called**********");
		if(!servicesAvailable){
			return;
		}
        
		setupMap();
		setupProgressBarFragment();
		setupProgressDialogFragment();
		
		// Initialize system resources.
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setupOnCameraChangeTaskHandler();
		setupLocationServices(new LocationListenerImpl());

		if(hasDelayedTasks || taskInProgress) {
			resetMapChangeTimer(GeneralConstants.SHORT_DELAY);
			progressBarFragment.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(!servicesAvailable) {
			return;
		}
		
		if(rents != null) {
			AddMarkersTask.addMarkers(rents, rentsMap, inflater);
		}
		
		if(UserPreferencesManager.getUserPreferences(this).showEnableLocationServices 
				&& !locationServicesEnabled) {
			(EnableLocationServicesDialogFragment.newInstance(true, false))
					.show(getSupportFragmentManager(), ENABLE_LOCATION_SERVICES_FRAGMENT_TAG);

			return;
		}
		
		restoreCameraPosition();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.e("TEST_TAG", "*********On saved instance state called**********");

		if(!servicesAvailable) {
			return;
		}

		outState.putParcelable(ActivitiesContract.CAMERA_POSITION, rentsMap.getCameraPosition());
		outState.putParcelable(ActivitiesContract.PREVIOUS_CAMERA_POSITION, previousCameraPosition);
		outState.putFloat(ActivitiesContract.PREVIOUS_ZOOM_LEVEL, previousZoomLevel);
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfRents);
		outState.putParcelableArrayList(ActivitiesContract.RENTS, (ArrayList<Rent>)rents);
		outState.putBoolean(ActivitiesContract.HAS_DELAYED_TASKS, handler.hasMessages(0));
		
		taskInProgress = progressBarFragment.getVisibility() == View.VISIBLE;
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, taskInProgress);

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e("TEST_TAG", "********On STOP called********");

		if(!servicesAvailable) {
			return;
		}

		// Retain current camera position in order to be saved during onDestroy().
		cameraPosition = rentsMap.getCameraPosition();
		
		// We reset all threads and listeners since we don't want to block the MAIN THREAD.
		resetOnCameraChangeTaskHandler();
		progressBarFragment.reset();
		resetProgressDialogFragment();
		locationClient.reset();
		resetMap();

		handler = null;
		onCameraChangeTask = null;
		rentsMap = null;
		lastClickedMarker = null;
		
		// Release system resources
		inflater = null;
		locationManager = null;
		locationClient = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("TEST_TAG", "************On destroy called************");

		if(!servicesAvailable) {
			return;
		}
		
		CameraPositionPreferencesManager.addCameraPosition(cameraPosition,
				this.getApplicationContext());

		rents = null;
		cameraPosition = null;
		previousCameraPosition = null;
		progressBarFragment = null;
		progressDialogFragment = null;
	}

	private void setupOnCameraChangeTaskHandler() {
		if(handler != null) {
			return;
		}

		handler = new Handler();
		onCameraChangeTask = new OnCameraChangeTask();
	}
	
	private void resetOnCameraChangeTaskHandler() {
		hasDelayedTasks = handler.hasMessages(0);
		if(hasDelayedTasks) {
			handler.removeCallbacks(onCameraChangeTask);
		}
	}
	
	private void setupActionBar() {
        getSupportActionBar().setTitle("Chirii");
	}
	
	public void setupMap() {
		if(rentsMap != null) {
			return;
		}

		RentsMapFragment rentsMapFragment = (RentsMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_map);

		// Sometimes mylocationbutton is null think for a fix when you have time.
		View myLocationButton = rentsMapFragment.getView().findViewById(0x2);
		if(myLocationButton != null) {
	        myLocationButton.setBackgroundResource(R.drawable.app_my_location_btn_selector);
		}
        ((TouchableMapView) rentsMapFragment.getView())
        	.setOnMapTouchListener(new RentsMapOnTouchListener());

        rentsMap = rentsMapFragment.getMap();
        rentsMap.setMyLocationEnabled(true);
		rentsMap.getUiSettings().setMyLocationButtonEnabled(true);
		rentsMap.getUiSettings().setCompassEnabled(true);
		rentsMap.getUiSettings().setZoomControlsEnabled(false);
        rentsMap.setInfoWindowAdapter(new RentMarkerInfoWindowAdapter(this));
        rentsMap.setOnCameraChangeListener(new RentsMapOnCameraChangeListener());
        rentsMap.setOnMarkerClickListener(new RentsMapOnMarkerClickListener());
        rentsMap.setOnInfoWindowClickListener(this);
        rentsMap.setOnMyLocationButtonClickListener(this);
	}
	
	private void resetMap() {
		rentsMap.setInfoWindowAdapter(null);
		rentsMap.setOnCameraChangeListener(null);
		rentsMap.setOnMarkerClickListener(null);
		rentsMap.setOnInfoWindowClickListener(null);
		rentsMap.setOnMyLocationButtonClickListener(null);
	}

	private void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}

		progressBarFragment.setOnTaskFinishListener(new OnGetRentsTaskFinishListener());
	}
	
	private void restoreCameraPosition() {
		if(cameraPosition != null) {
			// Recreated activity or camera position updated by the location client.
			return;
		}

		cameraPosition = CameraPositionPreferencesManager.getCameraPosition(this);
		if(cameraPosition != null) {
			LocationUtil.moveToLocation(cameraPosition, rentsMap);

			return;
		}
		
		updateCameraPosition();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rents_map_menu, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.search_action) {
			Intent intent = new Intent(this, FilterSearchActivity.class);
			intent.putExtra(ActivitiesContract.LATITUDE, 
					rentsMap.getCameraPosition().target.latitude);
			intent.putExtra(ActivitiesContract.LONGITUDE, 
					rentsMap.getCameraPosition().target.longitude);
			intent.putExtra(ActivitiesContract.VISIBLE_REGION, 
					rentsMap.getProjection().getVisibleRegion());
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, 
					ActivitiesContract.RENTS_MAP_ACTIVITY);

			startActivity(intent);

			return true;
		} else if(item.getItemId() == R.id.list_rents_action) {
			Intent intent = new Intent(this, RentsListActivity.class);
			intent.putExtra(ActivitiesContract.LATITUDE, 
					rentsMap.getCameraPosition().target.latitude);
			intent.putExtra(ActivitiesContract.LONGITUDE, 
					rentsMap.getCameraPosition().target.longitude);
			intent.putExtra(ActivitiesContract.VISIBLE_REGION, 
					rentsMap.getProjection().getVisibleRegion());
			intent.putExtra(ActivitiesContract.NO_OF_RENTS, totalNoOfRents);
			intent.putParcelableArrayListExtra(ActivitiesContract.RENTS, (ArrayList<Rent>) rents);
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

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Intent intent = new Intent(this, RentDetailsActivity.class);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, ActivitiesContract.RENTS_MAP_ACTIVITY);

		startActivity(intent);
	}
	
	@Override
	public boolean onMyLocationButtonClick() {
		if(!locationServicesEnabled) {
			(EnableLocationServicesDialogFragment.newInstance(false, false))
					.show(getSupportFragmentManager(), ENABLE_LOCATION_SERVICES_FRAGMENT_TAG);
		} else if(!locationManager.isNetworkLocationServicesEnabled()) {
			if(UserPreferencesManager.getUserPreferences(this).showEnableNetworkLocationService)
			(EnableLocationServicesDialogFragment.newInstance(true, true))
				.show(getSupportFragmentManager(), ENABLE_LOCATION_SERVICES_FRAGMENT_TAG);
		}

		return false;
	}
	
	private void onChange(LatLng newCenter, LatLng oldCenter, float newZoom, float oldZoom) {
		previousCameraPosition = newCenter;
		previousZoomLevel = newZoom;

		if ((!newCenter.equals(oldCenter)) && (newZoom != oldZoom)) {
			Toast.makeText(RentsMapActivity.this, "Map Zoom + Pan", Toast.LENGTH_SHORT).show();
		} else if (!newCenter.equals(oldCenter)) {
			Toast.makeText(RentsMapActivity.this, "Map Pan", Toast.LENGTH_SHORT).show();
		} else if (newZoom != oldZoom) {
			Toast.makeText(RentsMapActivity.this, "Map Zoom", Toast.LENGTH_SHORT).show();
		}
		
		startGetRentsAsyncTask(rentsMap.getProjection().getVisibleRegion());
	}
	
	private void resetMapChangeTimer(long delay) {
		handler.removeCallbacks(onCameraChangeTask);
		handler.postDelayed(onCameraChangeTask, delay);
	}
    
	private void startGetRentsAsyncTask(VisibleRegion visibleRegion) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetRentsByMapBoundariesAsyncTask getRentsTask = new GetRentsByMapBoundariesAsyncTask();
		progressBarFragment.setTask(getRentsTask);
		getRentsTask.execute(visibleRegion);
	}
	
	private class OnCameraChangeTask implements Runnable {
		@Override
		public void run() {
			if(!isTouched) {
				onChange(rentsMap.getCameraPosition().target, previousCameraPosition, 
						rentsMap.getCameraPosition().zoom, previousZoomLevel);
			} else {
				resetMapChangeTimer(GeneralConstants.LONG_DELAY);
			}
		}
	}

    private class RentsMapOnCameraChangeListener implements GoogleMap.OnCameraChangeListener {
		@Override
		public void onCameraChange(CameraPosition center) {
			if(isSpanChange(center.target) || isZoomChange(center.zoom)) {
				Log.e("TEST_TAG", "********Camera CHANGED********");
				resetMapChangeTimer(GeneralConstants.LONG_DELAY);
				progressBarFragment.show();
			}
		}
    	
    	private boolean isSpanChange(LatLng newCenter) {
    		return !newCenter.equals(previousCameraPosition);
    	}
    	
    	private boolean isZoomChange(float newZoom) {
    		return (newZoom != previousZoomLevel);
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
    
    private class OnGetRentsTaskFinishListener implements OnNetworkTaskFinishListener {
    	
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			Log.e("TEST_TAG", "************On task FINISH called**************");
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, RentsMapActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(RentsMapActivity.this, "Rents couldn't be retrieved", 
						Toast.LENGTH_LONG).show();
				
				return;
			}

			rents = ((RentsCounter)result).rents;
			totalNoOfRents = ((RentsCounter)result).counter;

			Toast.makeText(RentsMapActivity.this, "Rents have been successfully retrieved: " 
					+ rents.size(), Toast.LENGTH_LONG).show();

			AddMarkersTask.addMarkers(rents, rentsMap, inflater);

			getSupportActionBar().setTitle(totalNoOfRents + " chirii au fost gasite");
		}
    }
    
    private class LocationListenerImpl implements LocationListener{
    	@Override
		public void onLocationChanged(Location location) {
			if(location == null) {
				return;
			}
			
			cameraPosition = CameraPosition.fromLatLngZoom(new LatLng(location.getLatitude(),
					location.getLongitude()), GeneralConstants.DEFAULT_ZOOM_FACTOR);

			LocationUtil.moveToLocation(cameraPosition, rentsMap);
			if(progressDialogFragment.isResumed()) {
				progressDialogFragment.dismiss();
			}
		}
    }
}