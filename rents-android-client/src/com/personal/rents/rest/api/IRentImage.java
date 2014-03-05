package com.personal.rents.rest.api;

import com.personal.rents.model.RentImage;

import retrofit.http.DELETE;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public interface IRentImage {
	
	@Multipart
	@POST("/rentimage/upload")
	public RentImage uploadRentImage(@Part("imageData") TypedByteArray image, 
			@Part("rentId") TypedString rentId, @Header("tokenKey") String tokenKey);
	
	@Multipart
	@PUT("/rentimage/replace")
	public RentImage replaceRentImage(@Part("imageData") TypedByteArray imageData,
			@Part("rentImageId") TypedString rentImageId,
			@Part("rentImageURI") String rentImageURI,
			@Part("rentId") TypedString rentId, @Header("tokenKey") String tokenKey);
	
	@DELETE("/rentimage/delete/{rentImageId}")
	public int deleteRentImage(@Path("rentImageId") int rentImageId, 
			@Header("tokenKey") String tokenKey);
}
