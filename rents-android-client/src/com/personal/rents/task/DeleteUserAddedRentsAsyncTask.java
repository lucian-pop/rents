package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;

public class DeleteUserAddedRentsAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, Integer> {
	
	private List<Integer> rentIds;
	
	public DeleteUserAddedRentsAsyncTask(List<Integer> rentIds) {
		this.rentIds = rentIds;
	}

	@Override
	protected Integer doInBackground(Object... params) {
		Account account  = (Account) params[0];

		int noOfDeletedRents = 0;
		try {
			noOfDeletedRents = RentsClient.deleteUserAddedRents(rentIds, account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return noOfDeletedRents;
	}
}
