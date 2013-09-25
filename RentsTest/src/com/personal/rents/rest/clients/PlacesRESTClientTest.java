package com.personal.rents.rest.clients;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

public class PlacesRESTClientTest extends AndroidTestCase {
	
	private static final String INPUT = "zorilor";
	
	private static final String REFERENCE = "CnRmAAAAbdl0FiGF-mzPLuwvgga2ofcEkSFlbIdlPBU7lvutKBYIaG" +
			"xw_TsJe5jygYSwxoOZEjO5fRQLIopla6TNEICVeelsN3x7wpnzJbmbQnHxT3CtoK1nKZPsjgSfG3w0bLxkKLioa" +
			"Ictzw1zygsdoPuQohIQNceGGFrCXxLGZGbRjdElHxoUK1lAbE062rJlX4bJSezrgSlusEs";
	
	private double latitude  = 46.7667;
	
	private double longitude = 23.5833;
	
	private static final String LATITUDE = "lat";

	public void testGetPlacesSuggestions() {
		List<String> placesNames = new ArrayList<String>();
		List<String> placesRefs = new ArrayList<String>();
		PlacesRESTClient.getPlacesSuggestions(INPUT, latitude, longitude, placesNames, placesRefs);
		
		assertTrue(placesNames.size() > 0);
	}
	
	public void testGetPlaceLocation() {
		JSONObject location = PlacesRESTClient.getPlaceLocation(REFERENCE);
		try {
			assertTrue(location.getDouble(LATITUDE) != 0) ;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}	
}
