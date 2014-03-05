package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.rest.client.RentsClient;

import android.content.Context;

public class SignupAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Account> {
	
	@Override
	protected Account doInBackground(Object... params) {
		Account account = (Account) params[0];
		Context context = (Context) params[1];

		Account createdAccount = null;
		try {
			createdAccount = RentsClient.signup(account);
		} catch(RetrofitError error) {
			handleError(error);
		}
		
		if(createdAccount != null) {
			UserAccountManager.addAccount(createdAccount, context);
		}

		return createdAccount;
	}

}
