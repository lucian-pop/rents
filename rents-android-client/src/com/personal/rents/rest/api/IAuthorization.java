package com.personal.rents.rest.api;

import com.personal.rents.rest.error.UnauthorizedException;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;

public interface IAuthorization {
	
	@GET("/auth")
	public Response authorize(@Header("tokenKey") String tokenKey) throws UnauthorizedException;

}
