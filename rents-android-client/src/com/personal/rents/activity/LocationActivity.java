package com.personal.rents.activity;

import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationListener;
import com.personal.rents.fragment.ProgressDialogFragment;
import com.personal.rents.logic.LocationClientWrapper;
import com.personal.rents.logic.LocationManagerWrapper;
import com.personal.rents.task.listener.OnProgressDialogDismissListener;
import com.personal.rents.util.GoogleServicesUtil;

public class LocationActivity extends BaseActivity {
	
	private static final long REQUEST_TIMEOUT = 15000;
	
	private static final String LOCATION_REQUEST_TIMEOUT_MSG = "Nu am reusit sa va localizam.";
	
	protected static final String PROGRESS_DIALOG_FRAGMENT_TAG = "PROGRESS_DIALOG_FRAGMENT_TAG";
	
    protected static final String ENABLE_LOCATION_SERVICES_FRAGMENT_TAG = 
    		"ENABLE_LOCATION_SERVICES_FRAGMENT_TAG";
    
    protected boolean locationServicesEnabled = false;

    protected LocationManagerWrapper locationManager;
    
    protected LocationClientWrapper locationClient;

    protected ProgressDialogFragment progressDialogFragment;
    
    protected boolean requestedCurrentLocation = false;
    
	protected void setupProgressDialogFragment() {
		if(progressDialogFragment == null) {
			progressDialogFragment = 
					ProgressDialogFragment.newInstance(REQUEST_TIMEOUT,
							new GetLocationProgressDialogDismissListener());
		} else {
			progressDialogFragment.setOnProgressDialogDismissListener(
					new GetLocationProgressDialogDismissListener());
		}
	}
	
	protected void resetProgressDialogFragment() {
		if(progressDialogFragment != null) {
			progressDialogFragment.reset();
		}
	}
	
	protected void setupLocationClient(LocationListener locationListener) {
		if(locationClient == null) {
			locationClient = new LocationClientWrapper(this, new ConnectionCallbacksImpl(),
					new OnConnectionFailureListenerImpl());
			locationClient.setLocationListener(locationListener);
		}
		
		if(locationClient.isConnected()) {
			locationClient.removeLocationUpdates();
			locationClient.disconnect();
			locationClient.connect();
		} else {
			locationClient.connect();
		}
	}
	
	protected void setupLocationManager() {
		if(locationManager == null) {
			locationManager = new LocationManagerWrapper(this);
			locationServicesEnabled = locationManager.isLocationServicesEnabled();
		}
	}

	protected void showGetLocationProgressDialog() {
		ProgressDialogFragment pdf = (ProgressDialogFragment) getSupportFragmentManager()
				.findFragmentByTag(PROGRESS_DIALOG_FRAGMENT_TAG);
		if(pdf != null) {
			progressDialogFragment = pdf;
			progressDialogFragment.getDialog().show();
		} else {
			progressDialogFragment.show(getSupportFragmentManager(), PROGRESS_DIALOG_FRAGMENT_TAG);
		}
	}
	
	protected void dismissGetLocationProgressDialog() {
		if(progressDialogFragment.isResumed()) {
			progressDialogFragment.dismiss();
		}
	}

    private class GetLocationProgressDialogDismissListener 
    		implements OnProgressDialogDismissListener {
		@Override
		public void onDialogDismiss(boolean timeoutReached) {
			requestedCurrentLocation = false;
			if(timeoutReached) {
				Toast.makeText(LocationActivity.this, LOCATION_REQUEST_TIMEOUT_MSG,
						Toast.LENGTH_LONG).show();
				}
		}
    }

    private class ConnectionCallbacksImpl implements GooglePlayServicesClient.ConnectionCallbacks {
		@Override
		public void onConnected(Bundle connectionHint) {
			locationClient.requestLocationUpdates();
		}

		@Override
		public void onDisconnected() {
			dismissGetLocationProgressDialog();
		}
    }
    
    private class OnConnectionFailureListenerImpl 
    		implements GooglePlayServicesClient.OnConnectionFailedListener {
		@Override
		public void onConnectionFailed(ConnectionResult result) {
	        int errorCode = result.getErrorCode();
	        
	        if (result.hasResolution()) {
	        	try {
	        		result.startResolutionForResult(LocationActivity.this, 
	        				GoogleServicesUtil.CONNECTION_FAILURE_RESOLUTION_REQUEST);
	        	} catch(IntentSender.SendIntentException e) {
	        		Toast.makeText(LocationActivity.this, 
	        				GoogleServicesUtil.UNABLE_TO_RESOLVE_ERROR_MSG,
	        				Toast.LENGTH_SHORT).show();
	        	}
	        } else {
	        	GoogleServicesUtil.showErrorDialog(errorCode, LocationActivity.this);
	        }
		}
    }
}
