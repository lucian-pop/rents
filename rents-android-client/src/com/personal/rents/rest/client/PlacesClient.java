package com.personal.rents.rest.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.personal.rents.model.Address;
import com.personal.rents.rest.util.PlacesParser;
import com.personal.rents.rest.util.RESTClientUtil;

import android.util.Log;

public final class PlacesClient {
	
	private static final String LOG_TAG = PlacesClient.class.getSimpleName();
	
	/*Query parameters and values */
	
	private static final String SENSOR_PARAM = "?sensor";
	
	private static final String KEY_PARAM = "&key";
	
	private static final String REFERENCE_PARAM = "&reference";
	
	private static final String LANGUAGE_PARAM = "&language";
	
	private static final String COMPONENTS_PARAM = "&components";
	
	private static final String LOCATION_PARAM = "&location";
	
	private static final String RADIUS_PARAM = "&radius";
	
	private static final String INPUT_PARAM = "&input";

	private static final boolean GPS_ENABLED = true;
	
	private static final String API_KEY = "AIzaSyCDGOqpLsETLZ34Lco9IzZ4l62IKHmdReg";
	
	private static final String LANGUAGE = "ro";
	
	private static final String COUNTRY = "country:ro";
	
	private static final int RADIUS = 50000; // 50 km
	
	/*REST client configuration params*/
	private static final String GOOGLE_PLACES_API_URL = 
			"https://maps.googleapis.com/maps/api/place";
	
	private static final String AUTOCOMPLETE_MODE = "/autocomplete";
	
	private static final String DETAILS_MODE = "/details";
	
	private static final String RESPONSE_FORMAT = "/json";
	
	private PlacesClient() {}
	
	public static List<String> getPlacesSuggestions(String input, double latitude, double longitude) {
		StringBuilder urlBuilder = new StringBuilder(GOOGLE_PLACES_API_URL 
				+ AUTOCOMPLETE_MODE + RESPONSE_FORMAT);
		urlBuilder.append(SENSOR_PARAM + "=" + GPS_ENABLED);
		urlBuilder.append(KEY_PARAM + "=" + API_KEY);
		urlBuilder.append(LANGUAGE_PARAM + "=" + LANGUAGE);
		urlBuilder.append(COMPONENTS_PARAM + "=" + COUNTRY);
		urlBuilder.append(LOCATION_PARAM + "=" + latitude + "," +longitude);
		urlBuilder.append(RADIUS_PARAM + "=" + RADIUS);
		try {
			urlBuilder.append(INPUT_PARAM + "="+ URLEncoder.encode(input, "UTF-8"));
		} catch (UnsupportedEncodingException uee) {
			Log.e(LOG_TAG, "Error encoding address", uee);
		}
		
		String result = RESTClientUtil.getResultFromUrl(urlBuilder.toString(), LOG_TAG, true);

		return PlacesParser.parsePlacesResult(result, LOG_TAG);
	}
	
	public static Address getPlaceLocation(String reference) {
		StringBuilder urlBuilder = new StringBuilder(GOOGLE_PLACES_API_URL + DETAILS_MODE 
				+ RESPONSE_FORMAT);
		urlBuilder.append(SENSOR_PARAM + "=" + GPS_ENABLED);
		urlBuilder.append(KEY_PARAM + "=" + API_KEY);
		urlBuilder.append(REFERENCE_PARAM + "=" + reference);
	
		String result = RESTClientUtil.getResultFromUrl(urlBuilder.toString(), LOG_TAG, true);
		
		return PlacesParser.parsePlaceResult(result, LOG_TAG);
	}

}
