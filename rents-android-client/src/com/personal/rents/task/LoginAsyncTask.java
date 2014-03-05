package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;

import android.content.Context;;

public class LoginAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Account> {

	@Override
	protected Account doInBackground(Object... params) {
		String email = (String) params[0];
		String password = (String) params[1];
		Context context = (Context) params[2];
		
		Account account = null;
		try {
			account = RentsClient.login(email, password);
		} catch (RetrofitError error) {
			handleError(error);
		}
	
		if(account != null) {
			UserAccountManager.addAccount(account, context);
		}
		
		return account;
	}
}
