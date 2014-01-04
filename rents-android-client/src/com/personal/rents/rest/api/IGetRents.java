package com.personal.rents.rest.api;

import com.personal.rents.dto.RentsCounter;

import retrofit.http.GET;
import retrofit.http.Query;

public interface IGetRents {

	@GET("/rents/light")
	public RentsCounter getRentsByMapBoundaries(@Query("minLatitude") double minLatitude,
			@Query("maxLatitude") double maxLatitude, @Query("minLongitude") double minLongitude,
			@Query("maxLongitude") double maxLongitude);
}
