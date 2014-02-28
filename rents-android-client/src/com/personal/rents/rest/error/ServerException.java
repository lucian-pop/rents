package com.personal.rents.rest.error;

import retrofit.RetrofitError;

public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;

	private RetrofitError error;
	
	public ServerException(RetrofitError error) {
		this.error = error;
	}

	public RetrofitError getCause() {
		return error;
	}
}
