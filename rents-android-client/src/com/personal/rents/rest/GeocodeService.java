package com.personal.rents.rest;

import retrofit.http.GET;
import retrofit.http.Query;

public interface GeocodeService {

	public static final String LANG_RO = "ro";
	
	public static boolean GPS_ENABLED = true;
	
	@GET("/geocode/json")
	public GeocodeResponse getAddressFromAdress(@Query("sensor") boolean sensor, 
			@Query("language") String language, @Query("address") String address);

	@GET("/geocode/json")
	public GeocodeResponse getAddressFromLocation(@Query("sensor") boolean sensor, 
			@Query("language")String language, @Query("latlng") String latlng);
}
