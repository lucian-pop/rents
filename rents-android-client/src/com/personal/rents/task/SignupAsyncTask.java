package com.personal.rents.task;

import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnSignupTaskFinishListener;

import android.content.Context;
import android.os.AsyncTask;

public class SignupAsyncTask extends AsyncTask<Object, Void, Account> {
	
	private OnSignupTaskFinishListener onSignupTaskFinishListener;
	
	public SignupAsyncTask(OnSignupTaskFinishListener onSignupTaskFinishListener) {
		this.onSignupTaskFinishListener = onSignupTaskFinishListener;
	}
	
	@Override
	protected Account doInBackground(Object... params) {
		Account account = (Account) params[0];
		Context context = (Context) params[1];
		account = RentsClient.signup(account);
		
		UserAccountManager.addAccount(account, context);

		return account;
	}

	@Override
	protected void onPostExecute(Account account) {
		onSignupTaskFinishListener.onTaskFinish(account);
	}

}
