package com.personal.rents.logic;

import com.personal.rents.model.UserPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferencesManager {
	
	private static UserPreferences userPreferences;
	
	private static final String PREFS_FILE = "com.personal.rents.user.prefs";
	
	protected static final String SHOW_ENABLE_LOCATION_SERVICES = "SHOW_ENABLE_LOCATION_SERVICES";
	
	protected static final String SHOW_ENABLE_NETWORK_LOCATION_SERVICE = 
			"SHOW_ENABLE_NETWORK_LOCATION_SERVICE";
	
	public static UserPreferences getUserPreferences(Context context) {
		if(userPreferences != null) {
			return userPreferences;
		}
		
		userPreferences = loadUserPreferences(context);

		return userPreferences;
	}
	
	private static UserPreferences loadUserPreferences(Context context) {
		SharedPreferences prefs = getUserPreferencesFile(context);
		
		userPreferences = new UserPreferences();
		userPreferences.showEnableLocationServices = 
				prefs.getBoolean(SHOW_ENABLE_LOCATION_SERVICES, true);
		userPreferences.showEnableNetworkLocationService = 
				prefs.getBoolean(SHOW_ENABLE_NETWORK_LOCATION_SERVICE, true);
		
		return userPreferences;
	}
	
	public static void updateShowEnableLocationServices(boolean showEnableLocationServices, 
			Context context) {
		userPreferences.showEnableLocationServices = showEnableLocationServices;
		addShowEnabledLocationServices(showEnableLocationServices, context);
	}
	
	public static void updateShowEnableNetworkLocationService(
			boolean showEnableNetworkLocationService, Context context) {
		userPreferences.showEnableNetworkLocationService = showEnableNetworkLocationService;
		addShowEnableNetworkLocationServices(showEnableNetworkLocationService, context);
	}
	
	protected static void addShowEnabledLocationServices(boolean showEnableLocationServices, 
			Context context) {
		SharedPreferences.Editor prefsEditor = getUserPreferencesFile(context).edit();
		prefsEditor.putBoolean(SHOW_ENABLE_LOCATION_SERVICES, showEnableLocationServices);
		prefsEditor.commit();
	}
	
	protected static void addShowEnableNetworkLocationServices(
			boolean showEnableNetworkLocationService, Context context){
		SharedPreferences.Editor prefsEditor = getUserPreferencesFile(context).edit();
		prefsEditor.putBoolean(SHOW_ENABLE_NETWORK_LOCATION_SERVICE, showEnableNetworkLocationService);
		prefsEditor.commit();
	}
	
	protected static SharedPreferences getUserPreferencesFile(Context context) {
		return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
	}
}
