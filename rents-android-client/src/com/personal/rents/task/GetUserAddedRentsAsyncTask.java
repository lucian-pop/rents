package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.util.GeneralConstants;

public class GetUserAddedRentsAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, RentsCounter> {

	@Override
	protected RentsCounter doInBackground(Object... params) {
		Account account = (Account) params[0];
		RentsCounter rentsCounter = null;
		try {
			rentsCounter = RentsClient.getUserAddedRents(GeneralConstants.PAGE_SIZE,
					account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return rentsCounter;
	}
}
