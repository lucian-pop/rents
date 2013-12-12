package com.personal.rents.rest.api;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface IChangePassword {

	@POST("/changepassword")
	@FormUrlEncoded
	public String changePassword(@Field("email") String email, @Field("password") String password,
			@Field("newPassword") String newPassword);
}	
