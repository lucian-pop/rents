package com.personal.rents.rest.api;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

import com.personal.rents.model.PlaceDetails;
import com.personal.rents.model.Places;

public interface IPlaces {
	
	public static final String API_KEY = "AIzaSyCDGOqpLsETLZ34Lco9IzZ4l62IKHmdReg";
	
	public static final String LANGUAGE = "ro";
	
	public static final String COUNTRY = "country:ro";

	@Headers("Referer: http://blog.dahanne.net")
	@GET("/autocomplete/json")
	public Places getPlacesSuggestions(@Query("sensor") boolean gpsEnabled, 
			@Query("key") String apiKey, @Query("language") String language,
			@Query("components") String country, @Query("input") String input);
	
	@Headers("Referer: http://blog.dahanne.net")
	@GET("/details/json")
	public PlaceDetails getPlaceDetails(@Query("sensor") boolean gpsEnabled, @Query("key") String apiKey,
			@Query("reference") String reference);

}
