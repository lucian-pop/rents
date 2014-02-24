package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.dto.RentFavoriteViewsCounter;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.UnauthorizedException;
import com.personal.rents.util.GeneralConstants;

public class GetUserFavoriteRentsAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, RentFavoriteViewsCounter> {

	@Override
	protected RentFavoriteViewsCounter doInBackground(Object... params) {
		Account account = (Account) params[0];
		RentFavoriteViewsCounter result = null;
		try {
			result = RentsClient.getUserFavoriteRents(GeneralConstants.PAGE_SIZE, account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		} catch(UnauthorizedException unautorizedError){
			handleUnauthorizedError();
		}

		return result;
	}
}
