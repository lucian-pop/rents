package com.personal.rents.rest.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public final class PlacesRESTClient {
	
	private static final String LOG_TAG = "PlacesSuggestionsRESTClient.TAG";
	
	/*Query parameters*/
	private static final boolean GPS_ENABLED = true;
	
	private static final String LANGUAGE = "ro";
	
	private static final String COUNTRY = "country:ro";
	
	private static final int RADIUS = 50000; // 50 km
	
	/*REST client config params*/
	private static final String GOOGLE_PLACES_API_URL = "https://maps.googleapis.com/maps/api/place";
	
	private static final String AUTOCOMPLETE_MODE = "/autocomplete";
	
	private static final String DETAILS_MODE = "/details";
	
	private static final String RESPONSE_FORMAT = "/json";
	
	private static final String API_KEY = "AIzaSyCDGOqpLsETLZ34Lco9IzZ4l62IKHmdReg";

	private static final String REFERER = "http://blog.dahanne.net";
	
	/*JSON parse elements*/
	private static final String SUGGESTIONS = "predictions";
	
	private static final String REFERENCE = "reference";
	
	private static final String DESCRIPTION = "description";
	
	private static final String GEOMETRY = "geometry";
	
	private static final String LOCATION = "location";
	
	private static final String RESULT = "result";
	
	private PlacesRESTClient() {}
	
	public static void getPlacesSuggestions(String input, double latitude, double longitude,
				List<String> placesNames, List<String> placesRefs) {
		HttpURLConnection con = null;
		StringBuilder jsonResults = new StringBuilder();
		StringBuilder urlBuilder = new StringBuilder(GOOGLE_PLACES_API_URL 
				+ AUTOCOMPLETE_MODE + RESPONSE_FORMAT);
		urlBuilder.append("?sensor=" + GPS_ENABLED);
		urlBuilder.append("&key=" + API_KEY);
		urlBuilder.append("&language=" + LANGUAGE);
		urlBuilder.append("&components=" + COUNTRY);
		urlBuilder.append("&location=" + latitude + "," +longitude);
		urlBuilder.append("&radius=" + RADIUS);
		try {
			urlBuilder.append("&input="+ URLEncoder.encode(input, "UTF-8"));

			URL googlePlaces = new URL(urlBuilder.toString());
			con = (HttpURLConnection)googlePlaces.openConnection();
		    con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.addRequestProperty("Referer", REFERER);
		    con.connect();

		    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String line;
	        while ((line = in.readLine()) != null) {
	        	jsonResults.append(line);
	        }
		} catch (MalformedURLException mue) {
			Log.e(LOG_TAG, "Error processing Places API URL", mue);

			return;
		} catch (IOException ioe) {
			Log.e(LOG_TAG, "Error connecting to Places API", ioe);

			return;
		} finally {
	        if (con != null) {
	            con.disconnect();
	        }
	    }

		parsePlacesSuggestionsResult(jsonResults.toString(), placesNames, placesRefs);
	}
	
	public static JSONObject getPlaceLocation(String reference) {
		HttpURLConnection con = null;
		StringBuilder jsonResult = new StringBuilder();
		StringBuilder urlBuilder = new StringBuilder(GOOGLE_PLACES_API_URL + DETAILS_MODE 
				+ RESPONSE_FORMAT);
		urlBuilder.append("?sensor=" + GPS_ENABLED);
		urlBuilder.append("&key=" + API_KEY);
		urlBuilder.append("&reference=" + reference);
		try {

			URL googlePlaces = new URL(urlBuilder.toString());
			con = (HttpURLConnection)googlePlaces.openConnection();
		    con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.addRequestProperty("Referer", REFERER);
		    con.connect();

		    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String line;
	        while ((line = in.readLine()) != null) {
	        	jsonResult.append(line);
	        }
		} catch (IOException ioe) {
			Log.e(LOG_TAG, "Error connecting to Places API", ioe);

			return null;
		} finally {
	        if (con != null) {
	            con.disconnect();
	        }
	    }
		
		JSONObject location = parsePlaceDetailsResult(jsonResult.toString());
		
		return location;
	}

	private static void parsePlacesSuggestionsResult(String result, List<String> placesNames,
			List<String> placesRefs) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray placesSuggestionsArray = jsonObject.getJSONArray(SUGGESTIONS);
			JSONObject placeSuggestion = null;
			for(int i=0; i < placesSuggestionsArray.length(); i++) {
				placeSuggestion = placesSuggestionsArray.getJSONObject(i);
				placesNames.add(placeSuggestion.getString(DESCRIPTION));
				placesRefs.add(placeSuggestion.getString(REFERENCE));
			}
		} catch (JSONException e) {
			 Log.e(LOG_TAG, "Cannot process JSON results", e);
		}
	}
	
	private static JSONObject parsePlaceDetailsResult(String result) {
		JSONObject location = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			location = jsonObject.getJSONObject(RESULT).getJSONObject(GEOMETRY)
					.getJSONObject(LOCATION);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON result", e);
		}
		
		return location;
	}

}
