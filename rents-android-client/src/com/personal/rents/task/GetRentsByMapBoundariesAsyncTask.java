package com.personal.rents.task;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;

public class GetRentsByMapBoundariesAsyncTask 
	extends BaseAsyncTask<VisibleRegion, Void, List<Rent>> {

	@Override
	protected List<Rent> doInBackground(VisibleRegion... params) {
		VisibleRegion visibleRegion = params[0];
		LatLng southwest = visibleRegion.latLngBounds.southwest;
		LatLng northeast = visibleRegion.latLngBounds.northeast;
		double minLatitude = Math.min(southwest.latitude, northeast.latitude);
		double maxLatitude = Math.max(southwest.latitude, northeast.latitude);
		double minLongitude = Math.min(southwest.longitude, northeast.longitude);
		double maxLongitude = Math.max(southwest.longitude, northeast.longitude);
		
		return RentsClient.getRentsByMapBoundaries(minLatitude, maxLatitude, minLongitude, maxLongitude);
	}

	@Override
	protected void onCancelled(List<Rent> result) {
		onTaskFinishListener.onTaskFinish(null, getTaskId());
	}

	@Override
	protected void onPostExecute(List<Rent> result) {
		onTaskFinishListener.onTaskFinish(result, getTaskId());
	}

}
