package com.personal.rents.rest.api;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

import com.personal.rents.model.Rent;

public interface IRent {

	@POST("/rent/add")
	public Rent addRent(@Body Rent rent, @Header("tokenKey") String tokenKey);
	
	@POST("/rent/update")
	public int updateRent(@Body Rent rent, @Header("tokenKey") String tokenKey);
}
