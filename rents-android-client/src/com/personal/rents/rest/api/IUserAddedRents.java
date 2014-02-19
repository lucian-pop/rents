package com.personal.rents.rest.api;

import java.util.List;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.model.Rent;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

public interface IUserAddedRents {

	@GET("/rents/useradded")
	public RentsCounter getUserAddedRents(@Query("accountId") int accountId,
			@Query("pageSize") int pageSize, @Header("tokenKey") String tokenKey);
	
	@GET("/rents/useradded/page")
	public List<Rent> getUserAddedRentsNextPage(@Query("accountId") int accountId,
			@Query("lastRentDate") String lastRentDate, @Query("lastRentId") int lastRentId,
			@Query("pageSize") int pageSize, @Header("tokenKey") String tokenKey);
	
	@POST("/rents/useradded/delete")
	public int deleteUserAddedRents(@Body List<Integer> rentIds,
			@Header("accountId") String accountId, @Header("tokenKey") String tokenKey);
}
