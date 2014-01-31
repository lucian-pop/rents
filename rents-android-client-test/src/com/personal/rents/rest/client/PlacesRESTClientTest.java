package com.personal.rents.rest.client;

import java.util.List;

import com.personal.rents.model.Address;
import com.personal.rents.model.Place;

import android.test.AndroidTestCase;
import android.util.Log;

public class PlacesRESTClientTest extends AndroidTestCase {
	
	private static final String INPUT = "zorilor";
	
	private static final String REFERENCE = "CnRmAAAAbdl0FiGF-mzPLuwvgga2ofcEkSFlbIdlPBU7lvutKBYIaG" +
			"xw_TsJe5jygYSwxoOZEjO5fRQLIopla6TNEICVeelsN3x7wpnzJbmbQnHxT3CtoK1nKZPsjgSfG3w0bLxkKLioa" +
			"Ictzw1zygsdoPuQohIQNceGGFrCXxLGZGbRjdElHxoUK1lAbE062rJlX4bJSezrgSlusEs";

	public void testGetPlacesSuggestions() {
		List<Place> places = PlacesClient.getPlacesSuggestions(INPUT);

		assertTrue(places.size() > 0);
	}
	
	public void testGetPlaceLocation() {
		Address address = PlacesClient.getPlaceAddress(REFERENCE);
		
		Log.e("TEST_TAG", "********Latitude: " + address.addressLatitude + ", Longitude: "
				+ address.addressLongitude);
		assertTrue(address.addressLatitude != 0) ;
	}	
}
