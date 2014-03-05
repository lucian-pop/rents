package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;

public class AddRentToFavoritesAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Object... params) {
		Integer rentId = (Integer) params[0];
		Account account = (Account) params[1];
		boolean added = false;
		try {
			added = RentsClient.addRentToFavorites(rentId, account.tokenKey);
		} catch (RetrofitError error) {
			handleError(error);
		}
		
		return added;
	}
}
