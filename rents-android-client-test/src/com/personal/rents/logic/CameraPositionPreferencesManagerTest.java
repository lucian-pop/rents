package com.personal.rents.logic;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.SharedPreferences;
import android.test.AndroidTestCase;

public class CameraPositionPreferencesManagerTest extends AndroidTestCase {
	
	private static final double LATITUDE  = 46.7667;
	
	private static final double LONGITUDE = 23.5833;
	
	private static final float ZOOM_LEVEL = 15;

	public void testAddCameraPosition() {
		CameraPosition cameraPosition = 
				CameraPosition.fromLatLngZoom(new LatLng(LATITUDE, LONGITUDE), ZOOM_LEVEL);
		CameraPositionPreferencesManager.addCameraPosition(cameraPosition, getContext());
		
		SharedPreferences prefs = 
				CameraPositionPreferencesManager.getCameraPositionPrefs(getContext());
		double latitude = prefs.getFloat(CameraPositionPreferencesManager.LATITUDE, 
				CameraPositionPreferencesManager.DEFAULT_VALUE);

		assertTrue(latitude != CameraPositionPreferencesManager.DEFAULT_VALUE);
	}

	public void testGetSavedCameraPosition() {
		SharedPreferences.Editor prefsEditor = 
				CameraPositionPreferencesManager.getCameraPositionPrefs(getContext()).edit();
		prefsEditor.putFloat(CameraPositionPreferencesManager.LATITUDE, (float) LATITUDE);
		prefsEditor.putFloat(CameraPositionPreferencesManager.LONGITUDE, (float) LONGITUDE);
		prefsEditor.putFloat(CameraPositionPreferencesManager.ZOOM_LEVEL, ZOOM_LEVEL);
		prefsEditor.commit();
		
		CameraPosition cameraPosition = 
				CameraPositionPreferencesManager.getCameraPosition(getContext());
		assertNotNull(cameraPosition);
	}
	
	public void testUnexistingCameraPosition() {
		SharedPreferences.Editor prefsEditor = 
				CameraPositionPreferencesManager.getCameraPositionPrefs(getContext()).edit();
		prefsEditor.clear();
		prefsEditor.commit();
		
		CameraPosition cameraPosition = 
				CameraPositionPreferencesManager.getCameraPosition(getContext());
		assertNull(cameraPosition);
	}
}
