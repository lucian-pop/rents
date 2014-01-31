package com.personal.rents.task;

import retrofit.RetrofitError;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.util.GeneralConstants;

public class GetRentsByMapBoundariesAsyncTask 
		extends ProgressBarFragmentAsyncTask<VisibleRegion, Void, RentsCounter> {

	@Override
	protected RentsCounter doInBackground(VisibleRegion... params) {
		VisibleRegion visibleRegion = params[0];
		
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
