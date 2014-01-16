package com.personal.rents.logic;

import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationClientWrapper {
	
	private static final long REQUEST_NORMAL_FREQUENCY = 1000;
	
	private static final long REQUEST_FAST_FREQUENCY = 500;
	
	public static final long REQUEST_TIMEOUT = 15000;

	private LocationListener locationListener;
	
	private LocationClient locationClient;
	
	public LocationClientWrapper(Context context, 
			GooglePlayServicesClient.ConnectionCallbacks connectionCallback,
			GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
		locationClient = new LocationClient(context, connectionCallback, connectionFailedListener);
	}

	public void setLocationListener(LocationListener locationListener) {
		this.locationListener = locationListener;
	}

	public void connect() {
		locationClient.connect();
	}

	public void disconnect() {
		locationClient.disconnect();
	}
	
	public boolean isConnected() {
		return locationClient.isConnected();
	}
	
	public void requestLocationUpdates() {
		LocationRequest locationRequest = new LocationRequest();
		locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		locationRequest.setInterval(REQUEST_NORMAL_FREQUENCY);
		locationRequest.setFastestInterval(REQUEST_FAST_FREQUENCY);
		locationRequest.setExpirationDuration(REQUEST_TIMEOUT);
		
		locationClient.requestLocationUpdates(locationRequest, locationListener);
	}
	
	public void cancelRequestLocationUpdates() {
		if(locationClient.isConnected()) {
			locationClient.removeLocationUpdates(locationListener);
			locationClient.disconnect();
		}
	}
	
	public void reset() {
		cancelRequestLocationUpdates();
		locationClient = null;
		locationListener = null;
	}
}
