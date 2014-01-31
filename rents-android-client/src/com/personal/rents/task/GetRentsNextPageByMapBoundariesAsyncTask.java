package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnLoadNextPageTaskFinishListener;
import com.personal.rents.util.GeneralConstants;

public class GetRentsNextPageByMapBoundariesAsyncTask 
		extends LoadNextPageAsyncTask<Void, List<Rent>, Rent> {
	
	private VisibleRegion visibleRegion;
	
	public GetRentsNextPageByMapBoundariesAsyncTask(VisibleRegion visibleRegion) {
		this.visibleRegion = visibleRegion;
	}

	@Override
	public LoadNextPageAsyncTask<Void, List<Rent>, Rent> newInstance(
			OnLoadNextPageTaskFinishListener<Rent> onLoadNextPageTaskFinishListener) {
		// TODO Auto-generated method stub
		GetRentsNextPageByMapBoundariesAsyncTask instance = 
				new GetRentsNextPageByMapBoundariesAsyncTask(visibleRegion);
		instance.setOnLoadNextPageTaskFinishListener(onLoadNextPageTaskFinishListener);
		return instance;
	}
	
	@Override
	protected List<Rent> doInBackground(Object... params) {
		LatLng southwest = visibleRegion.latLngBounds.southwest;
		LatLng northeast = visibleRegion.latLngBounds.northeast;
		double minLatitude = Math.min(southwest.latitude, northeast.latitude);
		double maxLatitude = Math.max(southwest.latitude, northeast.latitude);
		double minLongitude = Math.min(southwest.longitude, northeast.longitude);
		double maxLongitude = Math.max(southwest.longitude, northeast.longitude);
		
		if(isCancelled()) {
			return null;
		}

		Rent lastRent = (Rent) params[0];
		List<Rent> result = null;
		try {
			result = RentsClient.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, lastRent.rentAddDate, lastRent.rentId, 
					GeneralConstants.PAGE_SIZE);
		} catch(RetrofitError error) {
			handleError(error);
		} 

		return result;
	}

}
