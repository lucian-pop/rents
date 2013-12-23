package com.personal.rents.rest.api;

import java.util.List;

import com.personal.rents.model.Rent;

import retrofit.http.GET;
import retrofit.http.Query;

public interface IGetRents {

	@GET("/rents/light")
	public List<Rent> getRentsByMapBoundaries(@Query("minLatitude") double minLatitude,
			@Query("maxLatitude") double maxLatitude, @Query("minLongitude") double minLongitude,
			@Query("maxLongitude") double maxLongitude);
}
