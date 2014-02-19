package com.personal.rents.logic;

import android.content.Context;
import com.personal.rents.model.Account;

/**
 * Manages an account singleton.
 * 		Context passed to methods should be application context.
 */
public class UserAccountManager {
	
	private static Account account;
	
	public static void addAccount(Account pAccount, Context context) {
		account = pAccount;
		
		// Add account to shared preferences.
		UserAccountPreferencesManager.addAccount(account, context);
	}
	
	public static Account getAccount(Context context) {
		if(account == null) {
			account = UserAccountPreferencesManager.getAccount(context);
		}
		
		return account;
	}
}
