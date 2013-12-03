package com.personal.rents.rest.api;

import retrofit.http.Body;
import retrofit.http.POST;

import com.personal.rents.model.Account;

public interface ISignup {
	
	@POST("/signup")
	public Account signup(@Body Account account);

}
