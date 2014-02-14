package com.personal.rents.rest.api;

import com.personal.rents.model.Rent;

import retrofit.http.GET;
import retrofit.http.Query;

public interface IGetRent {

	@GET("/rent/detailed")
	public Rent getDetailedRent(@Query("rentId") int rentId);

}
