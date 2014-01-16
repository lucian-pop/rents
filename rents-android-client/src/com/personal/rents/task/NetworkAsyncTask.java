package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.rest.util.RetrofitResponseStatus;

import android.os.AsyncTask;

public abstract class NetworkAsyncTask<Params, Progress, Result> 
		extends AsyncTask<Params, Progress, Result> {
	
	protected int taskId;
	
	protected ProgressBarFragment progressBarFragment;
	
	protected RetrofitResponseStatus status = RetrofitResponseStatus.OK;

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public void setProgressBarFragment(ProgressBarFragment progressBarFragment) {
		this.progressBarFragment = progressBarFragment;
	}

	protected void handleError(RetrofitError error) {
		if(error.isNetworkError()) {
			status = RetrofitResponseStatus.NETWORK_ERROR;
		} else {
			status = RetrofitResponseStatus.UKNOWN_ERROR;
		}
	}

}
