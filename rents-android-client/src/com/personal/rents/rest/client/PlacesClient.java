package com.personal.rents.rest.client;

import java.util.Arrays;
import java.util.List;

import retrofit.RestAdapter;

import com.personal.rents.model.Address;
import com.personal.rents.model.Place;
import com.personal.rents.model.PlaceDetails;
import com.personal.rents.model.Places;
import com.personal.rents.rest.api.IPlaces;
import com.personal.rents.rest.util.PlacesParser;

public class PlacesClient {
	
	private static final String PLACES_API = "https://maps.googleapis.com/maps/api/place";
	
	private static final RestAdapter restAdapter;
	
	private static IPlaces placesService;

	static {
		restAdapter = new RestAdapter.Builder().setServer(PLACES_API).build();
	}
	
	public static IPlaces getPlacesService() {
		if(placesService == null) {
			placesService = restAdapter.create(IPlaces.class);
		}
		
		return placesService;
	}

	public static List<Place> getPlacesSuggestions(String input) {
		Places places = getPlacesService().getPlacesSuggestions(true, IPlaces.API_KEY,
						IPlaces.LANGUAGE, IPlaces.COUNTRY, input);

		return Arrays.asList(places.predictions);
	}

	public static Address getPlaceAddress(String reference) {
		PlaceDetails placeDetails = getPlacesService().getPlaceDetails(true, IPlaces.API_KEY, reference);
		
		return PlacesParser.parsePlaceDetails(placeDetails.result);
	}
}
