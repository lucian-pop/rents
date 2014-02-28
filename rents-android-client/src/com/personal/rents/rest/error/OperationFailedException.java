package com.personal.rents.rest.error;

import retrofit.RetrofitError;

public class OperationFailedException extends ServerException {

	private static final long serialVersionUID = 1L;
	
	public OperationFailedException(RetrofitError error) {
		super(error);
	}
}
