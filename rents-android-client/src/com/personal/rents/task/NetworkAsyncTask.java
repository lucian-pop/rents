package com.personal.rents.task;

import retrofit.RetrofitError;
import retrofit.client.Response;

import com.personal.rents.webservice.response.ResponseStatusReason;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

import android.os.AsyncTask;

public abstract class NetworkAsyncTask<Params, Progress, Result> 
		extends AsyncTask<Params, Progress, Result> {
	
	protected ResponseStatusReason statusReason = ResponseStatusReason.OK;

	protected void handleError(RetrofitError error) {
		if(error.isNetworkError()) {
			statusReason = ResponseStatusReason.NETWORK_ERROR;
			
			return;
		} 
		
		Response response = error.getResponse();
		if(response == null) {
			statusReason = ResponseStatusReason.UNKNOWN_ERROR;
			
			return;
		}
		
		int status = response.getStatus();
		if(status == WebserviceResponseStatus.UNAUTHORIZED.getCode()) {
			statusReason = ResponseStatusReason.UNAUTHORIZED_ERROR;
		} else if(status ==  WebserviceResponseStatus.OPERATION_FAILED.getCode()) {
			statusReason = ResponseStatusReason.OPERATION_FAILED_ERROR;
		} else if(status == WebserviceResponseStatus.ACCOUNT_CONFLICT.getCode()) {
			statusReason = ResponseStatusReason.ACCOUNT_CONFLICT_ERROR;
		} else if(status == WebserviceResponseStatus.BAD_CREDENTIALS.getCode()) {
			statusReason = ResponseStatusReason.BAD_CREDENTIALS_ERROR;
		} else if(status == WebserviceResponseStatus.VERSION_OUTDATED.getCode()){
			statusReason = ResponseStatusReason.VERSION_OUTDATED_ERROR;
		} else {
			statusReason = ResponseStatusReason.UNKNOWN_ERROR;
		}
	}
}
