package com.personal.rents.rest.api;

import com.personal.rents.rest.error.UnauthorizedException;

import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public interface IUploadImage {
	
	@Multipart
	@POST("/uploadimage")
	public String uploadImage(@Part("image") TypedByteArray image, 
			@Part("filename") TypedString filename, @Part("datetime") TypedString datetime,
			@Header("tokenKey") String tokenKey) throws UnauthorizedException;
}
