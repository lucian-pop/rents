package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;

import android.content.Context;

public class AuthorizationAsyncTask extends BaseAsyncTask<Context, Void, Boolean> {
	
	@Override
	protected Boolean doInBackground(Context... params) {
		Context context = params[0];
		Account account = UserAccountManager.getAccount(context);
		if(account == null) {
			return false;
		}

		boolean authorized = false;
		try {
			authorized = RentsClient.isAuthorized(account.accountId, account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error, context);
		}
		
		return authorized;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		onTaskFinishListener.onTaskFinish(result, getTaskId(), status);
	}

}
