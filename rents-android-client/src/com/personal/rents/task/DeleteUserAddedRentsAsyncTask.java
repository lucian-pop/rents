package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.UnauthorizedException;

public class DeleteUserAddedRentsAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, Integer> {
	
	private List<Integer> rentIds;
	
	public DeleteUserAddedRentsAsyncTask(List<Integer> rentIds) {
		this.rentIds = rentIds;
	}

	@Override
	protected Integer doInBackground(Object... params) {
		Account account  = (Account) params[0];

		int noOfDeletedRents = -1;
		try {
			noOfDeletedRents = RentsClient.deleteUserAddedRents(rentIds, account.accountId,
					account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		} catch (UnauthorizedException unauthorizedError) {
			handleUnauthorizedError();
		}

		return noOfDeletedRents;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(result, taskId, status); 
		}
	}
}
