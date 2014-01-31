package com.personal.rents.logic;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationManagerWrapper {

	private LocationManager locationManager;
	
	public LocationManagerWrapper(Context context) {
		if(locationManager == null) {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
	}

	public boolean isLocationServicesEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) 
				|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	public boolean isGPSLocationServicesEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public boolean isNetworkLocationServicesEnabled() {
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	public Location getGPSLastKnownLocation() {
		return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}
	
	public Location getLastKnownLocation() {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		return location;
	}

}
