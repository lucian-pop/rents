package com.personal.rents.activity;

import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;

import android.content.Intent;
import android.os.Bundle;

public class AccountActivity extends BaseActivity {
	
	protected Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		verifyCredentials();
	}
	
	private void verifyCredentials() {
		account = UserAccountManager.getAccount(this);
		if(account == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			
			finish();
		}
	}
}
