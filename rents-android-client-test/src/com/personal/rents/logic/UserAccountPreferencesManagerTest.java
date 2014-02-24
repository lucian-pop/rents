package com.personal.rents.logic;

import com.personal.rents.model.Account;

import android.content.SharedPreferences;
import android.test.AndroidTestCase;

public class UserAccountPreferencesManagerTest extends AndroidTestCase {
	
	private static final String TOKEN_KEY = "dsadasdasda21123qdsadad";
	
	public void testAddAccount() {
		Account account = new Account();
		account.tokenKey = TOKEN_KEY;

		UserAccountPreferencesManager.addAccount(account, getContext());
		SharedPreferences prefs = UserAccountPreferencesManager.getAccountPrefs(getContext());
		String tokenKey = prefs.getString(UserAccountPreferencesManager.TOKEN_KEY, "");
		
		assertEquals(tokenKey, TOKEN_KEY);
	}
	
	public void testGetSavedAccount() {
		int accountId = 1;

		SharedPreferences.Editor prefsEditor = UserAccountPreferencesManager
				.getAccountPrefs(getContext()).edit();
		prefsEditor.putString(UserAccountPreferencesManager.TOKEN_KEY, TOKEN_KEY);
		prefsEditor.commit();
		
		Account account = UserAccountPreferencesManager.getAccount(getContext());
		
		assertTrue(account.tokenKey == TOKEN_KEY);
	}
	
	public void testGetUnexistingAccount() {
		SharedPreferences.Editor prefsEditor = UserAccountPreferencesManager
				.getAccountPrefs(getContext()).edit();
		prefsEditor.clear();
		prefsEditor.commit();
		
		Account account = UserAccountPreferencesManager.getAccount(getContext());
		assertTrue(account == null);
	}
	
	public void testRemoveAccount() {
		SharedPreferences prefs = UserAccountPreferencesManager.getAccountPrefs(getContext());
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putString(UserAccountPreferencesManager.TOKEN_KEY, TOKEN_KEY);
		prefsEditor.commit();
		
		UserAccountPreferencesManager.removeAccount(getContext());
		
		assertTrue(prefs.getAll().size() == 0);
	}
}
