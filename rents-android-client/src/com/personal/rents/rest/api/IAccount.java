package com.personal.rents.rest.api;

import com.personal.rents.model.Account;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface IAccount {

	@POST("/account/changepassword")
	@FormUrlEncoded
	public String changePassword(@Field("email") String email, @Field("password") String password,
			@Field("newPassword") String newPassword);
	
	@POST("/account/login")
	@FormUrlEncoded
	public Account login(@Field("email") String email, @Field("password") String password);
	
	@POST("/account/signup")
	public Account signup(@Body Account account);
}
