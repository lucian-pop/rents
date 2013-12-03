package com.personal.rents.logic;

import com.personal.rents.model.Account;

import android.content.Context;
import android.content.SharedPreferences;

public class UserAccountPreferencesManager {
	
	protected static final String ACCOUNT_PREFS_FILE = "com.personal.rents.account.prefs";
	
	protected static final String ID = "ID";
	
	protected static final String ACCOUNT_TYPE = "accountType";
	
	protected static final String EMAIL = "email";
	
	protected static final String FIRSTNAME = "firstname";
	
	protected static final String LASTNAME = "lastname";
	
	protected static final String PHONE = "phone";
	
	protected static final String TOKEN_KEY = "tokenKey";
	
	public static void addAccount(Account account, Context context) {
		SharedPreferences.Editor accountPrefsEditor = getAccountPrefs(context).edit();
		accountPrefsEditor.putInt(ID, account.getId());
		accountPrefsEditor.putString(TOKEN_KEY, account.getTokenKey());
		accountPrefsEditor.commit();
	}
	
	public static Account getAccount(Context context) {
		SharedPreferences accountPrefs = getAccountPrefs(context);
		if(accountPrefs.getAll().size() == 0) {
			// There are no account saved preferences.
			return null;
		}
		
		Account account = new Account();
		account.setId(accountPrefs.getInt(ID, -1));
		account.setTokenKey(accountPrefs.getString(TOKEN_KEY, null));
		
		return account;
	}
	
	public static void removeAccount(Context context) {
		SharedPreferences.Editor accountPrefsEditor = getAccountPrefs(context).edit();
		accountPrefsEditor.clear();
		accountPrefsEditor.commit();
	}
	
	public static void replaceToken(String tokenKey, Context context) {
		SharedPreferences.Editor accountPrefsEditor = getAccountPrefs(context).edit();
		accountPrefsEditor.putString(TOKEN_KEY, tokenKey);
		accountPrefsEditor.commit();
	}
	
	protected static SharedPreferences getAccountPrefs(Context context) {
		return context.getSharedPreferences(ACCOUNT_PREFS_FILE, Context.MODE_PRIVATE);
	}
}
