package com.personal.rents.rest.api;

import com.personal.rents.rest.error.OperationFailedException;
import com.personal.rents.rest.error.UnauthorizedException;

import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public interface IRentImage {
	
	@Multipart
	@POST("/rentimage")
	public String uploadImage(@Part("image") TypedByteArray image, 
			@Part("rentId") TypedString rentId, @Header("tokenKey") String tokenKey)
					throws UnauthorizedException, OperationFailedException;
}
