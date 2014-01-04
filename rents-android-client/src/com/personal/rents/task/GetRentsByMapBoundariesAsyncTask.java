package com.personal.rents.task;

import retrofit.RetrofitError;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.rest.client.RentsClient;

public class GetRentsByMapBoundariesAsyncTask extends BaseAsyncTask<Object, Void, RentsCounter> {

	@Override
	protected RentsCounter doInBackground(Object... params) {
		VisibleRegion visibleRegion = (VisibleRegion) params[0];
		Context context = (Context) params[1];
		
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
					minLongitude, maxLongitude);
		} catch(RetrofitError error) {
			handleError(error, context);
		} 
		
		return rentsCounter;
	}

	@Override
	protected void onCancelled(RentsCounter result) {
		onTaskFinishListener.onTaskFinish(null, getTaskId(), status);
	}

	@Override
	protected void onPostExecute(RentsCounter result) {
		onTaskFinishListener.onTaskFinish(result, getTaskId(), status);
	}

}
