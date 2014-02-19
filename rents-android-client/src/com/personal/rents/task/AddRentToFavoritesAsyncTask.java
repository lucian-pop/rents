package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.UnauthorizedException;

public class AddRentToFavoritesAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Object... params) {
		Integer rentId = (Integer) params[0];
		Account account = (Account) params[1];
		boolean added = false;
		try {
			added = RentsClient.addRentToFavorites(rentId, account.accountId, account.tokenKey);
		} catch (RetrofitError error) {
			handleError(error);
		} catch (UnauthorizedException unauthorizedError) {
			handleUnauthorizedError();
		}
		
		return added;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(result, taskId, status); 
		}
	}
}
