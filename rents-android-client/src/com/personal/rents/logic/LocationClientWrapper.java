package com.personal.rents.logic;

import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationClientWrapper {
	
	private static final long REQUEST_NORMAL_FREQUENCY = 5000;
	
	private static final long REQUEST_FAST_FREQUENCY = 1000;

	private LocationListener locationListener;
	
	private LocationClient locationClient;
	
	private LocationRequest locationRequest;
	
	private ConnectionCallbacks connectionCallback;
	
	private OnConnectionFailedListener connectionFailedListener;
	
	public LocationClientWrapper(Context context, ConnectionCallbacks connectionCallback,
			OnConnectionFailedListener connectionFailedListener) {
		this.connectionCallback = connectionCallback;
		this.connectionFailedListener = connectionFailedListener;
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
		if(locationRequest == null) {
			locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(REQUEST_NORMAL_FREQUENCY);
			locationRequest.setFastestInterval(REQUEST_FAST_FREQUENCY);
		}
		
		locationClient.requestLocationUpdates(locationRequest, locationListener);
	}
	
	public void removeLocationUpdates() {
		locationClient.removeLocationUpdates(locationListener);
	}
	
	public void reset() {
		if(isConnected()) {
			removeLocationUpdates();
			disconnect();
		}
		
		locationClient.unregisterConnectionCallbacks(connectionCallback);
		locationClient.unregisterConnectionFailedListener(connectionFailedListener);

		locationClient = null;
		locationListener = null;
		locationRequest = null;
	}
}
