package com.personal.rents.rest.clients.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.personal.rents.model.Address;

import android.util.Log;

public final class PlacesParser {
	
	/*JSON parse elements*/
	private static final String RESULT = "result";
	
	private static final String SUGGESTIONS = "predictions";
	
	private static final String DESCRIPTION = "description";
	
	private static final String GEOMETRY = "geometry";
	
	private static final String LOCATION = "location";
	
	private static final String LATITUDE = "lat";
	
	private static final String LONGITUDE = "lng";
	

	private PlacesParser(){
	}
	
	public static List<String> parsePlacesResult(String result, String logTag) {
		List<String> placesNames = null;
		try {
			JSONArray placesSuggestionsArray = (new JSONObject(result)).getJSONArray(SUGGESTIONS);
			placesNames = new ArrayList<String>(placesSuggestionsArray.length());
			JSONObject placeSuggestion = null;	
			for(int i=0; i < placesSuggestionsArray.length(); i++) {
				placeSuggestion = placesSuggestionsArray.getJSONObject(i);
				placesNames.add(placeSuggestion.getString(DESCRIPTION));
			}
		} catch (JSONException e) {
			 Log.e(logTag, "Cannot process JSON result", e);
		}
		
		return placesNames;
	}
	
	public static Address parsePlaceResult(String result, String logTag) {
		Address address = null;
		try {
			JSONObject jsonResult = new JSONObject(result).getJSONObject(RESULT);
			if(jsonResult == null) {
				return null;
			}

			JSONObject location = jsonResult.getJSONObject(GEOMETRY).getJSONObject(LOCATION);
			address = new Address();
			address.latitude = location.getDouble(LATITUDE);
			address.longitude = location.getDouble(LONGITUDE);
		} catch (JSONException e) {
			Log.e(logTag, "Cannot process JSON result", e);
		}

		return address;
	}
}
