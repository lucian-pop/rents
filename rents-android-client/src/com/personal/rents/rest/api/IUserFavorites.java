package com.personal.rents.rest.api;

import java.util.List;

import com.personal.rents.dto.RentFavoriteViewsCounter;
import com.personal.rents.model.view.RentFavoriteView;
import com.personal.rents.rest.error.UnauthorizedException;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

public interface IUserFavorites {

	@POST("/rents/userfavorites/addrent")
	public boolean addRentToFavorites(@Body int rentId,	@Header("tokenKey") String tokenKey)
			throws UnauthorizedException;
	
	@GET("/rents/userfavorites")
	public RentFavoriteViewsCounter getUserFavoriteRents(@Query("pageSize") int pageSize,
			@Header("tokenKey") String tokenKey) throws UnauthorizedException;
	
	@GET("/rents/userfavorites/page")
	public List<RentFavoriteView> getUserFavoriteRentsNextPage(@Query("lastDate") String lastDate,
			@Query("pageSize") int pageSize, @Header("tokenKey") String tokenKey) 
					throws UnauthorizedException;
	
	@POST("/rents/userfavorites/delete")
	public int deleteUserFavoriteRents(@Body List<Integer> rentIds,
			@Header("tokenKey") String tokenKey) throws UnauthorizedException;
}
