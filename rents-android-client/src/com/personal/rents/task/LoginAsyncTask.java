package com.personal.rents.task;

import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnLoginTaskFinishListener;

import android.content.Context;
import android.os.AsyncTask;

public class LoginAsyncTask extends AsyncTask<Object, Void, Account> {
	
	private OnLoginTaskFinishListener onLoginTaskFinishListener;
	
	public LoginAsyncTask(OnLoginTaskFinishListener onLoginTaskFinishListener) {
		this.onLoginTaskFinishListener = onLoginTaskFinishListener;
	}

	@Override
	protected Account doInBackground(Object... params) {
		String email = (String) params[0];
		String password = (String) params[1];
		Context context = (Context) params[2];
		Account account = RentsClient.login(email, password);
	
		UserAccountManager.addAccount(account, context);
		
		return account;
	}

	@Override
	protected void onPostExecute(Account account) {
		onLoginTaskFinishListener.onTaskFinish(account);
	}

	
}
