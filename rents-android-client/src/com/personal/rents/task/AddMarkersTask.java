package com.personal.rents.task;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.personal.rents.R;
import com.personal.rents.model.Rent;
import com.personal.rents.util.RentMarkerBuilder;

public class AddMarkersTask {

	public static void addMarkers(LayoutInflater inflater, List<Rent> rents, GoogleMap map, 
			Map<String, Integer> markersRents) {
		map.clear();
		markersRents.clear();

		View rentMarkerView = inflater.inflate(R.layout.rent_marker_icon_layout, null);
		Marker marker = null;
		int rentIndex = 0;
		for(Rent rent : rents) {
			marker = addMarker(rent, map, rentMarkerView);
			markersRents.put(marker.getId(), rentIndex);
			++rentIndex;
		}
	}
	
	public static Marker addMarker(LayoutInflater inflater, Rent rent, GoogleMap map) {
		View rentMarkerView = inflater.inflate(R.layout.rent_marker_icon_layout, null);

		return addMarker(rent, map, rentMarkerView);
	}
	
	public static Marker addMarker(Rent rent, GoogleMap map, View rentMarkerView) {
		Marker marker = map.addMarker(new MarkerOptions()
		 	.position(new LatLng(rent.address.addressLatitude, rent.address.addressLongitude))
		 	.icon(BitmapDescriptorFactory.fromBitmap(RentMarkerBuilder.createRentMarkerIcon(
		 			rentMarkerView, rent.rentPrice))));
		
		return marker;
	}

}
