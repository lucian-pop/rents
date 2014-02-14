package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;

public class AuthorizationAsyncTask extends ProgressBarFragmentAsyncTask<Account, Void, Boolean> {
	
	@Override
	protected Boolean doInBackground(Account... params) {
		Account account = params[0];
		boolean authorized = false;
		try {
			authorized = RentsClient.isAuthorized(account.accountId, account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}
		
		return authorized;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progressBarFragment.taskFinished(result, taskId, status);
	}
}
