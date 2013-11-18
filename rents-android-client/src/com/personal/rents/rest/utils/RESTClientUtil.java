package com.personal.rents.rest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public final class RESTClientUtil {

	private static final String REFERER = "http://blog.dahanne.net";
	
	private static final String REF_REQUEST_PROPERTY = "Referer";
	
	private RESTClientUtil() {
	}

	public static String getResultFromUrl(String url, String logTag, boolean addReferer) {
		HttpURLConnection conn = null;
		StringBuilder jsonResult = new StringBuilder();
		try {
			URL googlePlaces = new URL(url);
			conn = (HttpURLConnection)googlePlaces.openConnection();
		    conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if(addReferer) {
				conn.addRequestProperty(REF_REQUEST_PROPERTY, REFERER);
			}
		    conn.connect();

		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line;
	        while ((line = in.readLine()) != null) {
	        	jsonResult.append(line);
	        }
		} catch (MalformedURLException murle) {
			Log.e(logTag, "Error processing GEOCODE API URL", murle);

			return null;
		} catch (IOException ioe) {
			Log.e(logTag, "Error connecting to GEOCODE API", ioe);

			return null;
		} finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
		
		return jsonResult.toString();
	}
}
