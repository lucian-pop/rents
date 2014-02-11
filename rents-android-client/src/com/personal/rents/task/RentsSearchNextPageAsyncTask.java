package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.personal.rents.model.Rent;
import com.personal.rents.model.RentSearch;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnLoadNextPageTaskFinishListener;

public class RentsSearchNextPageAsyncTask 
		extends LoadNextPageAsyncTask<Void, List<Rent>, Rent> {
	
	private RentSearch rentSearch;
	
	public RentsSearchNextPageAsyncTask(RentSearch rentSearch) {
		this.rentSearch = rentSearch;
	}

	@Override
	public LoadNextPageAsyncTask<Void, List<Rent>, Rent> newInstance(
			OnLoadNextPageTaskFinishListener<Rent> onLoadNextPageTaskFinishListener) {
		RentsSearchNextPageAsyncTask instance = new RentsSearchNextPageAsyncTask(rentSearch);
		instance.setOnLoadNextPageTaskFinishListener(onLoadNextPageTaskFinishListener);
		return instance;
	}
	
	@Override
	protected List<Rent> doInBackground(Object... params) {
		Rent lastRent = (Rent) params[0];
		rentSearch.highRent.rentId = lastRent.rentId;
		rentSearch.highRent.rentAddDate = lastRent.rentAddDate;
		
		if(isCancelled()) {
			return null;
		}

		List<Rent> result = null;
		try {
			result = RentsClient.searchRentsNextPage(rentSearch);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return result;
	}
}
