package com.personal.rents.rest.api;

import java.util.List;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Rent;

import retrofit.http.GET;
import retrofit.http.Query;

public interface IGetRents {

	@GET("/rents/light")
	public RentsCounter getRentsByMapBoundaries(@Query("minLatitude") double minLatitude,
			@Query("maxLatitude") double maxLatitude, @Query("minLongitude") double minLongitude,
			@Query("maxLongitude") double maxLongitude, @Query("pageSize") int pageSize);
	
	@GET("/rents/light/page")
	public List<Rent> getRentsNextPageByMapBoundaries(@Query("minLatitude") double minLatitude,
			@Query("maxLatitude") double maxLatitude, @Query("minLongitude") double minLongitude,
			@Query("maxLongitude") double maxLongitude, @Query("lastRentDate") String lastRentDate,
			@Query("lastRentId") int lastRentId, @Query("pageSize") int pageSize);
}
