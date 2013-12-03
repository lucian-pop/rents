package com.personal.rents.task;

import com.personal.rents.logic.UserAccountPreferencesManager;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnChangePasswordTaskFinishListener;

import android.content.Context;
import android.os.AsyncTask;

public class ChangePasswordAsyncTask extends AsyncTask<Object, Void, String>{
	
	private OnChangePasswordTaskFinishListener onChangePasswordTaskFinishListener;
	
	public ChangePasswordAsyncTask(OnChangePasswordTaskFinishListener 
			onChangePasswordTaskFinishListener) {
		this.onChangePasswordTaskFinishListener = onChangePasswordTaskFinishListener;
	}

	@Override
	protected String doInBackground(Object... params) {
		String email = (String) params[0];
		String password = (String) params[1];
		String newPassword = (String) params[2];
		Context context = (Context) params[3];
		String tokenKey = RentsClient.changePassword(email, password, newPassword);
		
		UserAccountPreferencesManager.replaceToken(tokenKey, context);
				
		return tokenKey;
	}

	@Override
	protected void onPostExecute(String tokenKey) {
		onChangePasswordTaskFinishListener.onTaskFinish(tokenKey);
	}
	
}
