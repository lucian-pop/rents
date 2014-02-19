package com.personal.rents.rest.error;

import retrofit.RetrofitError;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private RetrofitError error;
	
	public UnauthorizedException(RetrofitError error) {
		this.error = error;
	}

	public RetrofitError getCause() {
		return error;
	}
}