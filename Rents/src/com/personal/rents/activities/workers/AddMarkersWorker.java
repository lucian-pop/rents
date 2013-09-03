package com.personal.rents.activities.workers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.rest.clients.RentsRESTClient;

import android.os.AsyncTask;

import java.util.List;

public class AddMarkersWorker extends AsyncTask<VisibleRegion, Void, List<LatLng>> {
	
	private GoogleMap rentsMap;
	
	public AddMarkersWorker(GoogleMap pRentsMap) {
		rentsMap = pRentsMap;
	}
	
	@Override
	protected List<LatLng> doInBackground(VisibleRegion... visibleRegions) {
		VisibleRegion visibleRegion = visibleRegions[0];
		List<LatLng> rentsPositions = RentsRESTClient.getRentsPositions(visibleRegion.latLngBounds.southwest,
				visibleRegion.latLngBounds.northeast);

		return rentsPositions;
	}

	@Override
	protected void onPostExecute(List<LatLng> result) {
		for(LatLng rentPosition : result) {
			rentsMap.addMarker(new MarkerOptions().position(rentPosition));
		}
	}
}
