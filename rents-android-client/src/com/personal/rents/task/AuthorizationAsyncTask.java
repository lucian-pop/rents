package com.personal.rents.task;

import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnAuthorizationTaskFinishListener;

import android.content.Context;
import android.os.AsyncTask;

public class AuthorizationAsyncTask extends AsyncTask<Context, Void, Boolean> {
	
	private OnAuthorizationTaskFinishListener onAuthorizationTaskFinishListener;
	
	public AuthorizationAsyncTask(OnAuthorizationTaskFinishListener 
			onAuthorizationTaskFinishListener) {
		this.onAuthorizationTaskFinishListener = onAuthorizationTaskFinishListener;
	}

	@Override
	protected Boolean doInBackground(Context... params) {
		Context context = params[0];
		Account account = UserAccountManager.getAccount(context);
		if(account == null) {
			return false;
		}

		return RentsClient.isAuthorized(account.accountId, account.tokenKey);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		onAuthorizationTaskFinishListener.onTaskFinish(result);
	}

}
