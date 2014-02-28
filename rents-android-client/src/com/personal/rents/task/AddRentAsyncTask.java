package com.personal.rents.task;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.OperationFailedException;
import com.personal.rents.rest.error.UnauthorizedException;

public class AddRentAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Rent> {

	public static AtomicBoolean completed;

	@Override
	protected Rent doInBackground(Object... params) {
		Rent rent = (Rent) params[0];
		Account account = (Account) params[1];
		
		Rent addedRent = null;
		try {
			addedRent = RentsClient.addRent(rent, account.tokenKey);
			completed = new AtomicBoolean(true);
		} catch(RetrofitError error) {
			handleError(error);
		} catch(UnauthorizedException unauthorizedError) {
			handleUnauthorizedError();
		} catch(OperationFailedException operationFailedError) {
			handleOperationFailedError();
		}

		return addedRent;
	}
}
