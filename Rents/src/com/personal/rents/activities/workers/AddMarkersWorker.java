package com.personal.rents.activities.workers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.activities.helpers.RentMarkerBuilder;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.clients.RentsRESTClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

public class AddMarkersWorker extends AsyncTask<VisibleRegion, Void, List<Rent>> {
	
	private GoogleMap rentsMap;
	
	private Context context;
	
	public AddMarkersWorker(GoogleMap rentsMap, Context context) {
		this.rentsMap = rentsMap;
		this.context = context;
	}
	
	@Override
	protected List<Rent> doInBackground(VisibleRegion... visibleRegions) {
		VisibleRegion visibleRegion = visibleRegions[0];
		List<Rent> rents = RentsRESTClient.getRentsPositions(visibleRegion.latLngBounds.southwest,
				visibleRegion.latLngBounds.northeast);

		return rents;
	}

	@Override
	protected void onPostExecute(List<Rent> result) {
		View rentMarkerView = 
				((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.rent_marker_icon_layout, null);

		Bitmap rentMarkerIcon = null;
		for(Rent rent : result) { 
			rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(context, rentMarkerView, rent.price);
			rentsMap.addMarker(new MarkerOptions().position(rent.position)
					.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
		}
		rentMarkerIcon = RentMarkerBuilder.createRentMarkerIcon(context, rentMarkerView, 500);
		rentsMap.addMarker(new MarkerOptions().position(rentsMap.getCameraPosition().target)
				.icon(BitmapDescriptorFactory.fromBitmap(rentMarkerIcon)));
	}
}