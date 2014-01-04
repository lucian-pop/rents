package com.personal.rents.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.adapter.RentMarkerInfoWindowAdapter;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.fragment.RentsMapFragment;
import com.personal.rents.logic.LocationManagerWrapper;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.AddMarkersTask;
import com.personal.rents.task.GetRentsByMapBoundariesAsyncTask;
import com.personal.rents.task.listener.OnTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.view.TouchableMapView;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class RentsMapActivity extends ActionBarActivity implements OnInfoWindowClickListener {
	
	private int totalNoOfRents;
	
	private List<Rent> rents;
	
    private LatLng lastCenterPosition;
	
    private float lastZoomLevel;
    
    private boolean isTouched = false;
    
    /*Don't need to be retained in onSaveInstanceState*/
    // This one should be nullified in onStop() and recreated during onStart();
    private LayoutInflater inflater;
    
    // This next two should be nullified after dismiss pending tasks.
	private GoogleMap rentsMap;
	
    private Marker lastClickedMarker;
    
    // Pending task should be released during onStop() and re-added during onStart().
    // Also nullify this two instances.
    private Handler handler;
    
    private OnCameraChangeTask onCameraChangeTask;
    
    // Maybe it should be nullified in onDestroy.
    private ProgressBarFragment progressBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rents_map_activity_layout);
        
        Log.e("TEST_TAG", "**********On create called**********");
		
        // Think hardly on what to do whit this two.
		handler = new Handler();
		onCameraChangeTask = new OnCameraChangeTask();
		
		setupActionBar();
		
		setUpMapView();
		
		// definitely should stay in onCreate or onRestoreInstaceState
		setupProgressBarFragment();
        
		// should move in the implementation of the onRestoreInstanceState
		restoreInstanceState(savedInstanceState);
		
		// if reincarnated activity you do not need to setup map. just retrieve it.
		if(rentsMap == null) {
			setUpMap();
		}
		
		// move this to on resume (maybe you should release inflater during onPause or onStop)
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(rents != null) {
			AddMarkersTask.addMarkers(rents, rentsMap, inflater);
		}
		
    }

	@Override
	protected void onStop() {
		super.onStop();
		
		// release resources that are saved during onSavedInstanceState and the ones that might leak memory (rentsMap)
		
		Log.e("TEST_TAG", "********On STOP called********");
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.e("TEST_TAG", "************On destroy called************");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.e("TEST_TAG", "*********On saved instance state called**********");
		outState.putParcelable(ActivitiesContract.LAST_MAP_CENTER, lastCenterPosition);
		outState.putFloat(ActivitiesContract.LAST_ZOOM_LEVEL, lastZoomLevel);
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfRents);
		outState.putParcelableArrayList(ActivitiesContract.RENTS, (ArrayList<Rent>)rents);
		
		boolean hasDelayedTasks = handler.hasMessages(0);
		outState.putBoolean(ActivitiesContract.HAS_DELAYED_TASKS, hasDelayedTasks);
		if(hasDelayedTasks) {
			handler.removeCallbacks(onCameraChangeTask);
		}
		
		super.onSaveInstanceState(outState);
	}

	private void restoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState == null) {
	        return;
		}
		
		rentsMap = ((RentsMapFragment) getSupportFragmentManager().findFragmentById(R.id.rents_map))
				.getMap();
		lastCenterPosition = 
				(LatLng) savedInstanceState.getParcelable(ActivitiesContract.LAST_MAP_CENTER);
		lastZoomLevel = savedInstanceState.getFloat(ActivitiesContract.LAST_ZOOM_LEVEL);
		totalNoOfRents = savedInstanceState.getInt(ActivitiesContract.NO_OF_RENTS);
		rents = savedInstanceState.getParcelableArrayList(ActivitiesContract.RENTS);

		boolean hasDelayedTasks = 
				savedInstanceState.getBoolean(ActivitiesContract.HAS_DELAYED_TASKS);
		if(hasDelayedTasks) {
			resetMapChangeTimer();
		}
	}

	private void setupActionBar() {
        getSupportActionBar().setTitle("Chirii");
	}

	private void setUpMapView() {
		RentsMapFragment rentsMapFragment = (RentsMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_map);
		View myLocationButton = rentsMapFragment.getView().findViewById(0x2);
        myLocationButton.setBackgroundResource(R.drawable.app_my_location_btn_selector);
        
        ((TouchableMapView) rentsMapFragment.getView())
        	.setOnMapTouchListener(new RentsMapOnTouchListener());
	}

	private void setUpMap() {
        rentsMap = ((RentsMapFragment) getSupportFragmentManager().findFragmentById(R.id.rents_map))
        		.getMap();
		rentsMap.setMyLocationEnabled(true);
		rentsMap.getUiSettings().setMyLocationButtonEnabled(true);
		rentsMap.getUiSettings().setCompassEnabled(true);
		rentsMap.getUiSettings().setZoomControlsEnabled(false);
        
        rentsMap.setInfoWindowAdapter(new RentMarkerInfoWindowAdapter(this));
        rentsMap.setOnCameraChangeListener(new RentsMapOnCameraChangeListener());
        rentsMap.setOnMarkerClickListener(new RentsMapOnMarkerClickListener());
        rentsMap.setOnInfoWindowClickListener(this);
        
    	LocationManagerWrapper locationHelper = new LocationManagerWrapper(this);
        Location location = locationHelper.getLastKnownLocation();
        if(location != null) {
        	locationHelper.moveToLocation(location, rentsMap);
        }
	}

	private void setupProgressBarFragment() {
		progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
				.findFragmentById(R.id.progressBarFragment);
		
		if(progressBarFragment.getTask() != null) {
			((GetRentsByMapBoundariesAsyncTask) progressBarFragment.getTask())
				.setOnTaskFinishListener(new OnGetRentsTaskFinishListener());
		}
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

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Intent intent = new Intent(this, RentDetailsActivity.class);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, ActivitiesContract.RENTS_MAP_ACTIVITY);

		startActivity(intent);
	}
	
	private void onChange(LatLng newCenter, LatLng oldCenter, float newZoom, float oldZoom) {
		lastCenterPosition = newCenter;
		lastZoomLevel = newZoom;

		if ((!newCenter.equals(oldCenter)) && (newZoom != oldZoom)) {
			Toast.makeText(RentsMapActivity.this, "Map Zoom + Pan", Toast.LENGTH_SHORT).show();
		} else if (!newCenter.equals(oldCenter)) {
			Toast.makeText(RentsMapActivity.this, "Map Pan", Toast.LENGTH_SHORT).show();
		} else if (newZoom != oldZoom) {
			Toast.makeText(RentsMapActivity.this, "Map Zoom", Toast.LENGTH_SHORT).show();
		}
		
		startGetRentsAsyncTask(rentsMap.getProjection().getVisibleRegion());
	}
	
	private void resetMapChangeTimer() {
		handler.removeCallbacks(onCameraChangeTask);
		handler.postDelayed(onCameraChangeTask, 1000L);
	}
    
	private void startGetRentsAsyncTask(VisibleRegion visibleRegion) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetRentsByMapBoundariesAsyncTask getRentsTask = new GetRentsByMapBoundariesAsyncTask();
		getRentsTask.setOnTaskFinishListener(new OnGetRentsTaskFinishListener());
		progressBarFragment.setTask(getRentsTask);
		getRentsTask.execute(visibleRegion, this);
	}
	
	private class OnCameraChangeTask implements Runnable {
		@Override
		public void run() {
			if(!isTouched) {
				onChange(rentsMap.getCameraPosition().target, lastCenterPosition, 
						rentsMap.getCameraPosition().zoom, lastZoomLevel);
			} else {
				resetMapChangeTimer();
			}
		}
	}

    private class RentsMapOnCameraChangeListener implements GoogleMap.OnCameraChangeListener {
		@Override
		public void onCameraChange(CameraPosition center) {
			if(isSpanChange(center.target) || isZoomChange(center.zoom)) {
				resetMapChangeTimer();
				progressBarFragment.show();
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
    
    private class OnGetRentsTaskFinishListener extends OnTaskFinishListener<RentsCounter> {
		@Override
		public void onTaskFinish(RentsCounter result, int taskId, RetrofitResponseStatus status) {
			handleResponse(result, taskId, status, RentsMapActivity.this);

			if(progressBarFragment.isResumed() 
					&& (progressBarFragment.getCurrentTaskId() == taskId)) {
				progressBarFragment.dismiss();
			}
			
			progressBarFragment.setTask(null);
		}

		@Override
		protected void handleOkStatus(RentsCounter result, int taskId) {
			if(result != null) {
				rents = result.rents;
				totalNoOfRents = result.counter;

				Toast.makeText(RentsMapActivity.this, "Rents have been successfully retrieved: " 
						+ rents.size(), Toast.LENGTH_LONG).show();

				AddMarkersTask.addMarkers(rents, rentsMap, inflater);

				getSupportActionBar().setTitle(result.counter + " chirii au fost gasite");
			} else {
				Toast.makeText(RentsMapActivity.this, "Rents couldn't be retrieved", 
						Toast.LENGTH_LONG).show();
			}
		}
    }

}
