package com.personal.rents.task;

import retrofit.RetrofitError;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.util.GeneralConstants;

public class GetRentsByMapBoundariesAsyncTask 
	extends ProgressBarFragmentAsyncTask<Object, Void, RentsCounter> {

	@Override
	protected RentsCounter doInBackground(Object... params) {
		VisibleRegion visibleRegion = (VisibleRegion) params[0];
		
		LatLng southwest = visibleRegion.latLngBounds.southwest;
		LatLng northeast = visibleRegion.latLngBounds.northeast;
		double minLatitude = Math.min(southwest.latitude, northeast.latitude);
		double maxLatitude = Math.max(southwest.latitude, northeast.latitude);
		double minLongitude = Math.min(southwest.longitude, northeast.longitude);
		double maxLongitude = Math.max(southwest.longitude, northeast.longitude);
		
		if(isCancelled()) {
			return null;
		}
		
		RentsCounter rentsCounter = null;
		try {
			rentsCounter = RentsClient.getRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, GeneralConstants.PAGE_SIZE);
		} catch(RetrofitError error) {
			handleError(error);
		} 
		
		return rentsCounter;
	}
}
