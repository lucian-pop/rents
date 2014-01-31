package com.personal.rents.util;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

public final class LocationUtil {

	private LocationUtil(){
	}

	public static double getVisibleRegionLatitudeSize(VisibleRegion visibleRegion) {
		LatLng southwest = visibleRegion.latLngBounds.southwest;
		LatLng northeast = visibleRegion.latLngBounds.northeast;
		double size = Math.abs(southwest.latitude - northeast.latitude);
		
		return size;
	}
	
	public static double getVisibleRegionLongitudeSize(VisibleRegion visibleRegion) {
		LatLng southwest = visibleRegion.latLngBounds.southwest;
		LatLng northeast = visibleRegion.latLngBounds.northeast;
		double size = Math.abs(southwest.longitude - northeast.longitude);
		
		return size;
	}
	
	public static void moveToLocation(Location location, GoogleMap map) {
		CameraPosition cameraPosition = 
				CameraPosition.fromLatLngZoom(new LatLng(location.getLatitude(), 
						location.getLongitude()), GeneralConstants.DEFAULT_ZOOM_FACTOR);

		moveToLocation(cameraPosition, map);
	}
	
	public static void moveToLocation(LatLng position, GoogleMap map) {
		moveToLocation(position, GeneralConstants.DEFAULT_ZOOM_FACTOR, map);
	}
	
	public static void moveToLocation(LatLng position, int zoomLevel, GoogleMap map) {
		CameraPosition cameraPosition = new CameraPosition.Builder().target(position)
				.zoom(zoomLevel).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	public static void moveToLocation(CameraPosition cameraPosition, GoogleMap map) {
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

}
