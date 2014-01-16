package com.personal.rents.logic;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;

public class CameraPositionPreferencesManager {
	
	private static final String PREFS_FILE = "com.personal.rents.cameraposition.prefs";
	
	protected static final String LATITUDE = "latitude";
	
	protected static final String LONGITUDE = "longitude";
	
	protected static final String ZOOM_LEVEL = "zoomLevel";
	
	protected static final float DEFAULT_VALUE = -1000;
	
	public static void addCameraPosition(CameraPosition cameraPosition, Context context) {
		SharedPreferences.Editor prefsEditor = getCameraPositionPrefs(context).edit();
		prefsEditor.putFloat(LATITUDE, (float) cameraPosition.target.latitude);
		prefsEditor.putFloat(LONGITUDE, (float) cameraPosition.target.longitude);
		prefsEditor.putFloat(ZOOM_LEVEL, cameraPosition.zoom);
		prefsEditor.commit();
	}
	
	public static CameraPosition getCameraPosition(Context context) {
		SharedPreferences prefs = getCameraPositionPrefs(context);
		if(prefs.getAll().size() == 0) {
			// There are no camera position saved preferences.
			return null;
		}

		double latitude = prefs.getFloat(LATITUDE, DEFAULT_VALUE);
		double longitude = prefs.getFloat(LONGITUDE, DEFAULT_VALUE);
		float zoom = prefs.getFloat(ZOOM_LEVEL, DEFAULT_VALUE);

		return CameraPosition.fromLatLngZoom(new LatLng(latitude, longitude), zoom);
	}

	protected static SharedPreferences getCameraPositionPrefs(Context context) {
		return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
	}

}
