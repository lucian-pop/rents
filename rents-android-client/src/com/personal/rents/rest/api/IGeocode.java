package com.personal.rents.rest.api;

import com.personal.rents.model.Geocode;

import retrofit.http.GET;
import retrofit.http.Query;

public interface IGeocode {

	public static final String LANG_RO = "ro";
	
	public static boolean GPS_ENABLED = true;
	
	@GET("/geocode/json")
	public Geocode getAddressFromAdress(@Query("sensor") boolean sensor, 
			@Query("language") String language, @Query("address") String address);

	@GET("/geocode/json")
	public Geocode getAddressFromLocation(@Query("sensor") boolean sensor, 
			@Query("language")String language, @Query("latlng") String latlng);
}
