package com.personal.rents.rest.api;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

public interface IRentFavorites {

	@POST("/rentfavorites/addrent")
	public boolean addRentToFavorites(@Body int rentId, @Header("accountId") String accountId,
			@Header("tokenKey") String tokenKey);
}
