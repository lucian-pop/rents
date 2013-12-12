package com.personal.rents.rest.api;

import retrofit.http.Body;
import retrofit.http.POST;

import com.personal.rents.model.Rent;

public interface IAddRent {

	@POST("/addrent")
	public Rent addRent(@Body Rent rent);
}
