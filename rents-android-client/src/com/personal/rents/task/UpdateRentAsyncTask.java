package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;

public class UpdateRentAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Integer>{

	@Override
	protected Integer doInBackground(Object... params) {
		Rent rent = (Rent) params[0];
		String tokenKey = (String) params[1];
		
		int updatesCount = -1;
		try {
			updatesCount = RentsClient.updateRent(rent, tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}
		
		return updatesCount;
	}
}
