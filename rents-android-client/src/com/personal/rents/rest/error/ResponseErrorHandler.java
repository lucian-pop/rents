package com.personal.rents.rest.error;

import com.personal.rents.webservice.response.WebserviceResponseStatus;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ResponseErrorHandler implements ErrorHandler {

	@Override
	public Throwable handleError(RetrofitError error) {
		Response response = error.getResponse();
		if(response != null 
				&& response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode()) {
			return new UnauthorizedException(error);
		}
		
		return error;
	}

}
