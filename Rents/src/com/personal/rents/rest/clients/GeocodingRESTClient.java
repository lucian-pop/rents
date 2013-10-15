package com.personal.rents.rest.clients;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.personal.rents.model.Address;
import com.personal.rents.rest.clients.utils.GeocodingParser;
import com.personal.rents.rest.clients.utils.RESTClientUtil;

import android.util.Log;

public final class GeocodingRESTClient {
	
	private static final String LOG_TAG = GeocodingRESTClient.class.getSimpleName();
	
	/*Query parameters names and values*/
	private static final String SENSOR_PARAM = "?sensor";
	
	private static final String LAT_LNG_PARAM = "&latlng";
	
	private static final String ADDRESS_PARAM = "&address";
	
	private static final String LANGUAGE_PARAM = "&language";
	
	private static final boolean GPS_ENABLED = true;
	
	private static final String LANGUAGE = "ro";
	
	/*REST client configuration parameters*/
	private static final String GOOGLE_GEOCODING_API_URL = 
			"http://maps.googleapis.com/maps/api/geocode";
	
	private static final String RESPONSE_FORMAT = "/json";

	private GeocodingRESTClient() {
	}
	
	public static Address getAddressFromLocation(double latitude, double longitude) {
		StringBuilder urlBuilder = new StringBuilder(GOOGLE_GEOCODING_API_URL + RESPONSE_FORMAT);
		urlBuilder.append(SENSOR_PARAM + "=" + GPS_ENABLED);
		urlBuilder.append(LAT_LNG_PARAM + "=" + latitude + "," + longitude);
		urlBuilder.append(LANGUAGE_PARAM + "=" + LANGUAGE);

		String result = RESTClientUtil.getResultFromUrl(urlBuilder.toString(), LOG_TAG, false);

		return GeocodingParser.parseAddressResult(result, LOG_TAG);
	}
	
	public static Address getAddressFromAddress(String queryAddress) {
		StringBuilder urlBuilder = new StringBuilder(GOOGLE_GEOCODING_API_URL + RESPONSE_FORMAT);
		urlBuilder.append(SENSOR_PARAM + "=" + GPS_ENABLED);
		urlBuilder.append(LANGUAGE_PARAM + "=" + LANGUAGE);
		try {
			urlBuilder.append(ADDRESS_PARAM + "=" + URLEncoder.encode(queryAddress, "UTF-8"));
		} catch (UnsupportedEncodingException uee) {
			Log.e(LOG_TAG, "Error encoding address", uee);
		}
	
		String result = RESTClientUtil.getResultFromUrl(urlBuilder.toString(), LOG_TAG, false);

		return GeocodingParser.parseAddressResult(result, LOG_TAG);
	}
}
