package com.personal.rents.rest.api;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;

public interface IAuthorization {
	
	@GET("/auth")
	public Response authorize(@Header("accountId") int accountId, 
			@Header("tokenKey") String tokenKey);

}
