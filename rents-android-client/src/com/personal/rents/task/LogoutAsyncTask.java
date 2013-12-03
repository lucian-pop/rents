package com.personal.rents.task;

import com.personal.rents.logic.UserAccountPreferencesManager;
import com.personal.rents.model.Account;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LogoutAsyncTask extends AsyncTask<Context, Void, Void> {
	
	private Context context;

	@Override
	protected Void doInBackground(Context... params) {
		context = params[0];
		UserAccountPreferencesManager.removeAccount(context);
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Account account = UserAccountPreferencesManager.getAccount(context);
		
		Toast.makeText(context, "Account should be null: " + (account == null), Toast.LENGTH_LONG)
			.show();
	}

}
