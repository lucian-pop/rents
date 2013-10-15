package com.personal.rents.utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationHelper {

	private static LocationManager locationManager;
	
	public LocationHelper(Context context) {
		if(locationManager == null) {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
	}

	public boolean isNetworkLocationEnabled() {
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	public boolean isGPSLocationEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public Location getLastKnownLocation() {
		Criteria criteria = new Criteria();
		String locationProvider = locationManager.getBestProvider(criteria, true);

		return locationManager.getLastKnownLocation(locationProvider);
	}
	
	public void moveToLocation(Location location, GoogleMap map) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng position = new LatLng(latitude, longitude);

		moveToLocation(position, map);
	}
	
	public void moveToLocation(LatLng position, GoogleMap map) {
		CameraPosition cameraPosition = new CameraPosition.Builder().target(position)
				.zoom(Constants.DEFAULT_ZOOM_FACTOR).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	public Location getCurrentLocation() {
		return null;
	}
}
