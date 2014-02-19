package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.UnauthorizedException;
import com.personal.rents.util.GeneralConstants;

public class GetUserAddedRentsAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, RentsCounter> {

	@Override
	protected RentsCounter doInBackground(Object... params) {
		Account account = (Account) params[0];
		RentsCounter rentsCounter = null;
		try {
			rentsCounter = RentsClient.getUserAddedRents(account.accountId, 
					GeneralConstants.PAGE_SIZE, account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		} catch(UnauthorizedException unautorizedError){
			handleUnauthorizedError();
		}

		return rentsCounter;
	}

	@Override
	protected void onPostExecute(RentsCounter result) {
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(result, taskId, status); 
		}
	}
}
