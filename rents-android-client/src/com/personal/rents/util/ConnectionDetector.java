package com.personal.rents.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectionDetector {
	
	private ConnectionDetector() {		
	}
	
	public static boolean hasInternetConnectivity(Context context) {
		ConnectivityManager connectivityManager = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
			for(NetworkInfo networkInfo : networkInfos) {
				if(networkInfo.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		
		return false;
	}
}
