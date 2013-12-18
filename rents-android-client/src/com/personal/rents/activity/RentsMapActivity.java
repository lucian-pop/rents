package com.personal.rents.activity;

import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.personal.rents.R;
import com.personal.rents.adapter.RentMarkerInfoWindowAdapter;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.fragment.RentsMapFragment;
import com.personal.rents.logic.LocationManagerWrapper;
import com.personal.rents.task.AddMarkersTask;
import com.personal.rents.task.AddRentsAsyncTask;
import com.personal.rents.task.listener.OnTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.RentsGenerator;
import com.personal.rents.view.TouchableMapView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class RentsMapActivity extends ActionBarActivity implements OnInfoWindowClickListener {

	private static final String TASK_FRAGMENT_TAG = "TASK";
	
	private static final long EVENTS_DELAY = 1000L;
	
    private FragmentManager fragmentManager;
	
    private RentsMapFragment rentsMapFragment;
    
    private ProgressBarFragment progressBarFragment;
	
    public GoogleMap rentsMap;
    
    private LatLng lastCenterPosition;
    
    private float lastZoomLevel;
    
    private Marker lastClickedMarker;
    
    private boolean isTouched = false;
    
    private OnTaskFinishListener onTaskFinishListener;
    
    private Handler handler = new Handler();
    
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

		fragmentManager = getSupportFragmentManager();

        init();

		restoreInstanceState(savedInstanceState);
    }

	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
	    	LocationManagerWrapper locationHelper = new LocationManagerWrapper(this);
	        Location location = locationHelper.getLastKnownLocation();
	        if(location != null) {
	        	locationHelper.moveToLocation(location, rentsMap);
	        }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rents_map_menu, menu);
		
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.LAST_MAP_CENTER, lastCenterPosition);
		outState.putFloat(ActivitiesContract.LAST_ZOOM_LEVEL, lastZoomLevel);
		
		boolean hasDelayedTasks = handler.hasMessages(0);
		outState.putBoolean(ActivitiesContract.HAS_DELAYED_TASKS, hasDelayedTasks);
		if(hasDelayedTasks) {
			handler.removeCallbacks(onMapChangeTask);
		}
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		lastCenterPosition = 
				(LatLng) savedInstanceState.getParcelable(ActivitiesContract.LAST_MAP_CENTER);
		lastZoomLevel = savedInstanceState.getFloat(ActivitiesContract.LAST_ZOOM_LEVEL);
		boolean hasDelayedTasks = 
				savedInstanceState.getBoolean(ActivitiesContract.HAS_DELAYED_TASKS);
		if(hasDelayedTasks) {
			onChange(rentsMap.getCameraPosition().target, lastCenterPosition, 
					rentsMap.getCameraPosition().zoom, lastZoomLevel);
		}
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
	
	private void init() {

		setupActionBar();
		
		setUpMapIfNeeded();
		
		initListeners();
		
		initProgressBarFragment();
	}

	private void setupActionBar() {
        getSupportActionBar().setTitle("Chirii");
	}
	
	private void setUpMapIfNeeded() {
        rentsMapFragment = (RentsMapFragment) fragmentManager.findFragmentById(R.id.rents_map);
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

        ((TouchableMapView) rentsMapFragment.getView())
        	.setOnMapTouchListener(new RentsMapOnTouchListener());
	}
	
	private void initListeners() {
		onTaskFinishListener = new OnTaskFinishListener() {
			@Override
			public void onTaskFinish(Object result) {
				if(result != null) {
					Toast.makeText(RentsMapActivity.this, "Rents have been successfully added", 
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(RentsMapActivity.this, "Rents couldn't be added", 
							Toast.LENGTH_LONG).show();
				}
				
				if(progressBarFragment.isResumed()) {
					progressBarFragment.dismiss();
				} else {
					progressBarFragment.setTask(null);
				}
			}
		};
	}
	
	private void initProgressBarFragment() {
		progressBarFragment = (ProgressBarFragment) fragmentManager.findFragmentByTag(TASK_FRAGMENT_TAG);
		
		if(progressBarFragment != null && progressBarFragment.getTask() != null) {
			progressBarFragment.getTask().setOnTaskFinishListener(onTaskFinishListener);
		}
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Intent intent = new Intent(this, RentDetailsActivity.class);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, ActivitiesContract.RENTS_MAP_ACTIVITY);

		startActivity(intent);
	}
	
	private void onChange(LatLng newCenter, LatLng oldCenter, float newZoom, float oldZoom) {
		lastCenterPosition = newCenter;
		lastZoomLevel = newZoom;

		List<LatLng> positions = 
				RentsGenerator.generatePositions(rentsMap.getProjection().getVisibleRegion());
		AddMarkersTask addMarkersTask = new AddMarkersTask(this, rentsMap);
		
		if ((!newCenter.equals(oldCenter)) && (newZoom != oldZoom)) {
			rentsMap.clear();
			Toast.makeText(RentsMapActivity.this, "Map Zoom + Pan", Toast.LENGTH_SHORT).show();
			addMarkersTask.addMarkers(positions);

			startAddRentsAsyncTask(positions);
		} else if (!newCenter.equals(oldCenter)) {
			rentsMap.clear();
			Toast.makeText(RentsMapActivity.this, "Map Pan", Toast.LENGTH_SHORT).show();
			addMarkersTask.addMarkers(positions);

			startAddRentsAsyncTask(positions);
		} else if (newZoom != oldZoom) {
			Toast.makeText(RentsMapActivity.this, "Map Zoom", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void resetMapChangeTimer() {
		handler.removeCallbacks(onMapChangeTask);
		handler.postDelayed(onMapChangeTask, EVENTS_DELAY);
	}
    
	private void startAddRentsAsyncTask(List<LatLng> positions) {
		AddRentsAsyncTask addRentsTask = new AddRentsAsyncTask(positions);
		addRentsTask.setOnTaskFinishListener(onTaskFinishListener);
		
		if(progressBarFragment == null) {
			progressBarFragment = new ProgressBarFragment();
			progressBarFragment.show(fragmentManager, TASK_FRAGMENT_TAG);
		} else if(progressBarFragment.getTask() != null && progressBarFragment.isResumed()) {
			progressBarFragment.getTask().cancel(false);
		} else {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(progressBarFragment);
			transaction.add(progressBarFragment, TASK_FRAGMENT_TAG);
			transaction.commit();
		}
		
		progressBarFragment.setTask(addRentsTask);
		addRentsTask.execute(this.getApplicationContext());
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
