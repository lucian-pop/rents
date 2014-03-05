package com.personal.rents.task;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.RetrofitError;

import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;

public class AddRentAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Rent> {

	public static AtomicBoolean completed;

	@Override
	protected Rent doInBackground(Object... params) {
		Rent rent = (Rent) params[0];
		String tokenKey = (String) params[1];
		
		Rent addedRent = null;
		try {
			addedRent = RentsClient.addRent(rent, tokenKey);
			completed = new AtomicBoolean(true);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return addedRent;
	}
}
