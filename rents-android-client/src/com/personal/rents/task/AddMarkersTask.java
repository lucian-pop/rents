package com.personal.rents.task;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.personal.rents.R;
import com.personal.rents.util.RentMarkerBuilder;

public class AddMarkersTask {
	
	private static final int MIN_PRICE = 100;
	
	private static final int MAX_PRICE = 20000;
	
	private LayoutInflater inflater;
	
	private GoogleMap rentsMap;
	
	public AddMarkersTask(Context context, GoogleMap rentsMap) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.rentsMap = rentsMap;
	}

	public void addMarkers(List<LatLng> positions) {
		View rentMarkerView = inflater.inflate(R.layout.rent_marker_icon_layout, null);

		Random random = new Random();
		Bitmap rentMarkerIcon = null;
		int price = 0;
		for(LatLng postion : positions) {
			price = MIN_PRICE + random.nextInt(MAX_PRICE - MIN_PRICE);
			rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(rentMarkerView,
					price);
			rentsMap.addMarker(new MarkerOptions()
				.position(postion)
				.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
		}

		// Add marker in the center of the map
		rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(rentMarkerView, 500);
		rentsMap.addMarker(new MarkerOptions().position(rentsMap.getCameraPosition().target)
				.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
	}
}
