package com.personal.rents.rest.api;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public interface IAuthorization {
	
	@GET("/auth")
	public Response authorize(@Query("accountId") int accountId, 
			@Header("tokenKey") String tokenKey);

}
