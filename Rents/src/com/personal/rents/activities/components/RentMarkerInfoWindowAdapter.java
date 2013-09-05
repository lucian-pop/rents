package com.personal.rents.activities.components;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.personal.rents.R;

public class RentMarkerInfoWindowAdapter implements InfoWindowAdapter {
	
	private final View window;
	
	public RentMarkerInfoWindowAdapter(Activity context) {
		window = context.getLayoutInflater().inflate(R.layout.rent_marker_info_window_layout, null);
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return window;
	}
	
}