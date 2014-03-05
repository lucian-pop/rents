package com.personal.rents.logic;

import com.personal.rents.model.Account;

import android.content.Context;
import android.content.SharedPreferences;

public class UserAccountPreferencesManager {
	
	private static final String PREFS_FILE = "com.personal.rents.account.prefs";
	
	protected static final String ACCOUNT_TYPE = "accountType";
	
	protected static final String EMAIL = "email";
	
	protected static final String PHONE = "phone";
	
	protected static final String TOKEN_KEY = "tokenKey";
	
	public static void addAccount(Account account, Context context) {
		SharedPreferences.Editor prefsEditor = getAccountPrefs(context).edit();
		prefsEditor.putString(EMAIL, account.accountEmail);
		prefsEditor.putString(PHONE, account.accountPhone);
		prefsEditor.putString(TOKEN_KEY, account.tokenKey);
		prefsEditor.commit();
	}
	
	public static Account getAccount(Context context) {
		SharedPreferences prefs = getAccountPrefs(context);
		if(prefs.getAll().size() == 0) {
			// There are no account saved preferences.
			return null;
		}
		
		Account account = new Account();
		account.tokenKey = prefs.getString(TOKEN_KEY, null);
		account.accountEmail = prefs.getString(EMAIL, null);
		account.accountPhone = prefs.getString(PHONE, null);
		
		return account;
	}
	
	public static void removeAccount(Context context) {
		SharedPreferences.Editor prefsEditor = getAccountPrefs(context).edit();
		prefsEditor.clear();
		prefsEditor.commit();
	}
	
	public static void replaceToken(String tokenKey, Context context) {
		SharedPreferences.Editor prefsEditor = getAccountPrefs(context).edit();
		prefsEditor.putString(TOKEN_KEY, tokenKey);
		prefsEditor.commit();
	}
	
	protected static SharedPreferences getAccountPrefs(Context context) {
		return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
	}
}
