package com.personal.rents.rest;

import com.personal.rents.model.Address;
import com.personal.rents.rest.utils.GeocodingParser;

import retrofit.RestAdapter;

public class GeocodeRESTClient {
	
	private static final String LOG_TAG = GeocodeRESTClient.class.getSimpleName();
			
	private static final String MAPS_API = "http://maps.googleapis.com/maps/api";
	
	private static final RestAdapter restAdapter;
	
	private static GeocodeService geocodingService;

	static {
		restAdapter = new RestAdapter.Builder().setServer(MAPS_API).build();
	}
	
	public static GeocodeService getGeocodingService() {
		if(geocodingService == null) {
			geocodingService = restAdapter.create(GeocodeService.class);
		}
		
		return geocodingService;
	}
	
	public static Address getAddressFromAddress(String address) {
		GeocodeResponse response = getGeocodingService().getAddressFromAdress(
				GeocodeService.GPS_ENABLED, GeocodeService.LANG_RO, address);

		return GeocodingParser.parseAddressResult(response.results[0], LOG_TAG);
	}
	
	public static Address getAddressFromLocation(double lat, double lng) {
		GeocodeResponse response = getGeocodingService().getAddressFromLocation(
				GeocodeService.GPS_ENABLED, GeocodeService.LANG_RO, lat + "," +lng);
		
		return GeocodingParser.parseAddressResult(response.results[0], LOG_TAG);
	}
}
