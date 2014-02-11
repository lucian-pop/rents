package com.personal.rents.task;

import retrofit.RetrofitError;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.RentSearch;
import com.personal.rents.rest.client.RentsClient;

public class RentsSearchAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, RentsCounter> {

	@Override
	protected RentsCounter doInBackground(Object... params) {
		RentSearch rentSearch = (RentSearch) params[0];
		VisibleRegion visibleRegion = (VisibleRegion) params[1];

		LatLng southwest = visibleRegion.latLngBounds.southwest;
		LatLng northeast = visibleRegion.latLngBounds.northeast;
		double minLatitude = Math.min(southwest.latitude, northeast.latitude);
		double maxLatitude = Math.max(southwest.latitude, northeast.latitude);
		double minLongitude = Math.min(southwest.longitude, northeast.longitude);
		double maxLongitude = Math.max(southwest.longitude, northeast.longitude);
		
		rentSearch.lowRent.address.addressLatitude = minLatitude;
		rentSearch.lowRent.address.addressLongitude = minLongitude;
		rentSearch.highRent.address.addressLatitude = maxLatitude;
		rentSearch.highRent.address.addressLongitude = maxLongitude;
		
		RentsCounter rentsCounter = null;
		try {
			rentsCounter = RentsClient.searchRents(rentSearch);
		} catch(RetrofitError error) {
			handleError(error);
		} 
		
		return rentsCounter;
	}

	@Override
	protected void onCancelled(RentsCounter result) {
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(null, taskId, status);
		}
	}

	@Override
	protected void onPostExecute(RentsCounter result) {
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(result, taskId, status); 
		}
	}
}
