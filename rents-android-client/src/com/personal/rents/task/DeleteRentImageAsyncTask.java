package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.rest.client.RentsClient;

public class DeleteRentImageAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, Void> {

	@Override
	protected Void doInBackground(Object... params) {
		int rentImageId = (Integer) params[0];
		String tokenKey = (String) params[1];
		try {
			RentsClient.deleteRentImage(rentImageId, tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return null;
	}
}
