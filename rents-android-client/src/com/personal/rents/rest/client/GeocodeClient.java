package com.personal.rents.rest.client;

import com.personal.rents.model.Address;
import com.personal.rents.model.Geocode;
import com.personal.rents.rest.api.IGeocode;
import com.personal.rents.rest.util.GeocodingParser;

import retrofit.RestAdapter;

public class GeocodeClient {
	
	private static final String LOG_TAG = GeocodeClient.class.getSimpleName();
			
	private static final String MAPS_API = "http://maps.googleapis.com/maps/api";
	
	private static final RestAdapter restAdapter;
	
	private static IGeocode geocodingService;

	static {
		restAdapter = new RestAdapter.Builder().setServer(MAPS_API).build();
	}
	
	public static IGeocode getGeocodingService() {
		if(geocodingService == null) {
			geocodingService = restAdapter.create(IGeocode.class);
		}
		
		return geocodingService;
	}
	
	public static Address getAddressFromAddress(String address) {
		Geocode response = getGeocodingService().getAddressFromAdress(
				IGeocode.GPS_ENABLED, IGeocode.LANG_RO, address);

		return GeocodingParser.parseAddressResult(response.results[0], LOG_TAG);
	}
	
	public static Address getAddressFromLocation(double lat, double lng) {
		Geocode response = getGeocodingService().getAddressFromLocation(
				IGeocode.GPS_ENABLED, IGeocode.LANG_RO, lat + "," +lng);
		
		return GeocodingParser.parseAddressResult(response.results[0], LOG_TAG);
	}
}
