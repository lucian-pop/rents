package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.logic.UserAccountPreferencesManager;
import com.personal.rents.rest.client.RentsClient;

import android.content.Context;

public class ChangePasswordAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, String>{
	@Override
	protected String doInBackground(Object... params) {
		String email = (String) params[0];
		String password = (String) params[1];
		String newPassword = (String) params[2];
		Context context = (Context) params[3];
		
		String tokenKey = null;
		try {
			tokenKey = RentsClient.changePassword(email, password, newPassword);
		} catch(RetrofitError error) {
			handleError(error);
		}

		if(tokenKey != null) {
			UserAccountPreferencesManager.replaceToken(tokenKey, context);
		}
				
		return tokenKey;
	}
}
