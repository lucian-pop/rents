package com.personal.rents.rest.client;

import java.util.List;

import com.personal.rents.model.Address;

import android.test.AndroidTestCase;

public class PlacesRESTClientTest extends AndroidTestCase {
	
	private static final String INPUT = "zorilor";
	
	private static final String REFERENCE = "CnRmAAAAbdl0FiGF-mzPLuwvgga2ofcEkSFlbIdlPBU7lvutKBYIaG" +
			"xw_TsJe5jygYSwxoOZEjO5fRQLIopla6TNEICVeelsN3x7wpnzJbmbQnHxT3CtoK1nKZPsjgSfG3w0bLxkKLioa" +
			"Ictzw1zygsdoPuQohIQNceGGFrCXxLGZGbRjdElHxoUK1lAbE062rJlX4bJSezrgSlusEs";
	
	private double latitude  = 46.7667;
	
	private double longitude = 23.5833;

	public void testGetPlacesSuggestions() {
		 List<String> placesNames = PlacesClient.getPlacesSuggestions(INPUT, latitude, 
				 longitude);
		
		assertTrue(placesNames.size() > 0);
	}
	
	public void testGetPlaceLocation() {
		Address address = PlacesClient.getPlaceLocation(REFERENCE);

		assertTrue(address.latitude != 0) ;
	}	
}
