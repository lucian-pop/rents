package com.personal.rents.rest.api;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import com.personal.rents.model.Account;

public interface ILogin {

	@POST("/login")
	@FormUrlEncoded
	public Account login(@Field("email") String email, @Field("password") String password);
}
