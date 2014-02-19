package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.rest.util.RetrofitResponseStatus;

import android.os.AsyncTask;

public abstract class NetworkAsyncTask<Params, Progress, Result> 
	extends AsyncTask<Params, Progress, Result> {
	
	protected RetrofitResponseStatus status = RetrofitResponseStatus.OK;

	protected void handleError(RetrofitError error) {
		if(error.isNetworkError()) {
			status = RetrofitResponseStatus.NETWORK_ERROR;
		} else {
			status = RetrofitResponseStatus.UKNOWN_ERROR;
		}
	}
	
	protected void handleUnauthorizedError() {
		status = RetrofitResponseStatus.UNAUTHORIZED_ERROR;
	}

}
