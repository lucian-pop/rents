package com.personal.rents.rest.api;

import retrofit.http.GET;

public interface IMyResource {

	@GET("/myresource")
	public String getIt();
	
}
