package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.OperationFailedException;
import com.personal.rents.rest.error.UnauthorizedException;

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
		} catch (UnauthorizedException unauthorizedError) {
			handleUnauthorizedError();
		} catch (OperationFailedException operationFailedError) {
			handleOperationFailedError();
		}

		return null;
	}
}
