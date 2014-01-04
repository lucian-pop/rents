package com.personal.rents.task;

import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.personal.rents.R;
import com.personal.rents.model.Rent;
import com.personal.rents.util.RentMarkerBuilder;

public class AddMarkersTask {

	public static void addMarkers(List<Rent> rents, GoogleMap rentsMap, LayoutInflater inflater) {
		rentsMap.clear();
		View rentMarkerView = inflater.inflate(R.layout.rent_marker_icon_layout, null);

		Bitmap rentMarkerIcon = null;
		for(Rent rent : rents) {
			rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(rentMarkerView, rent.rentPrice);
			rentsMap.addMarker(new MarkerOptions()
				.position(new LatLng(rent.address.addressLatitude, rent.address.addressLongitude))
				.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
		}

		// Add marker in the center of the map
		rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(rentMarkerView, 500);
		rentsMap.addMarker(new MarkerOptions().position(rentsMap.getCameraPosition().target)
				.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
	}

}
