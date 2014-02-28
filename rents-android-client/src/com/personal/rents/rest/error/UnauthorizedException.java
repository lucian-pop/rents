package com.personal.rents.rest.error;

import retrofit.RetrofitError;

public class UnauthorizedException extends ServerException {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException(RetrofitError error) {
		super(error);
	}
}