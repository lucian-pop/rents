package com.personal.rents.logic;

import com.personal.rents.model.Account;

import android.content.SharedPreferences;
import android.test.AndroidTestCase;

public class UserAccountPreferencesManagerTest extends AndroidTestCase {
	
	public void testAddAccount() {
		Account account = new Account();
		account.setId(1);
		
		UserAccountPreferencesManager.addAccount(account, getContext());
		SharedPreferences prefs = UserAccountPreferencesManager.getAccountPrefs(getContext());
		int accountId = prefs.getInt(UserAccountPreferencesManager.ID, -1);
		
		assertTrue(account.getId() == accountId);
	}
	
	public void testGetAccount() {
		int accountId = 1;

		SharedPreferences.Editor prefsEditor = UserAccountPreferencesManager
				.getAccountPrefs(getContext()).edit();
		prefsEditor.putInt(UserAccountPreferencesManager.ID, accountId);
		prefsEditor.commit();
		
		Account account = UserAccountPreferencesManager.getAccount(getContext());
		
		assertTrue(account.getId() == accountId);
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
		int accountId = 1;

		SharedPreferences prefs = UserAccountPreferencesManager.getAccountPrefs(getContext());
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putInt(UserAccountPreferencesManager.ID, accountId);
		prefsEditor.commit();
		
		UserAccountPreferencesManager.removeAccount(getContext());
		
		assertTrue(prefs.getAll().size() == 0);
	}
}
