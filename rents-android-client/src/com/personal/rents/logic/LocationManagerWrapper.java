package com.personal.rents.logic;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.personal.rents.util.GeneralConstants;

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

	public void moveToLocation(Location location, GoogleMap map) {
		CameraPosition cameraPosition = 
				CameraPosition.fromLatLngZoom(new LatLng(location.getLatitude(), 
						location.getLongitude()), GeneralConstants.DEFAULT_ZOOM_FACTOR);

		moveToLocation(cameraPosition, map);
	}
	
	public void moveToLocation(LatLng position, GoogleMap map) {
		moveToLocation(position, GeneralConstants.DEFAULT_ZOOM_FACTOR, map);
	}
	
	public void moveToLocation(LatLng position, int zoomLevel, GoogleMap map) {
		CameraPosition cameraPosition = new CameraPosition.Builder().target(position)
				.zoom(zoomLevel).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	public void moveToLocation(CameraPosition cameraPosition, GoogleMap map) {
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

}
