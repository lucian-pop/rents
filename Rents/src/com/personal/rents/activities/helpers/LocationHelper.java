package com.personal.rents.activities.helpers;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.personal.rents.utils.Constants;

import android.content.Context;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public final class LocationHelper {

	private static LocationManager locationManager;
	
	public LocationHelper(Context context) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
		LatLng latLng = new LatLng(latitude, longitude);

		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		map.animateCamera(CameraUpdateFactory.zoomTo(Constants.DEFAULT_ZOOM_FACTOR));
	}
	
	public void moveToLocation(LatLng location, GoogleMap map) {
		map.moveCamera(CameraUpdateFactory.newLatLng(location));
		map.animateCamera(CameraUpdateFactory.zoomTo(Constants.DEFAULT_ZOOM_FACTOR));
	}
	
	public LatLng getBottomLeftMapPosition(GoogleMap map, int mapHeight) {
		return map.getProjection().fromScreenLocation(new Point(0, mapHeight));
	}

	public LatLng getUpperRigthMapPosition(GoogleMap map, int mapWidth) {
		return map.getProjection().fromScreenLocation(new Point(mapWidth, 0));
	}
	
	public LatLng getBottomRightMapPosition(GoogleMap map, int mapHeight, int mapWidth) {
		return map.getProjection().fromScreenLocation(new Point(mapWidth, mapHeight));
	}
	
	public LatLng getUpperLeftMapPosition(GoogleMap map) {
		return map.getProjection().fromScreenLocation(new Point(5, 5));
	}
}
