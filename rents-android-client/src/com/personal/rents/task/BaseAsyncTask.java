package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.listener.OnTaskFinishListener;
import com.personal.rents.util.ConnectionDetector;

import android.content.Context;
import android.os.AsyncTask;

public abstract class BaseAsyncTask<Params, Progress, Result> 
		extends AsyncTask<Params, Progress, Result> {
	
	private int taskId;
	
	protected RetrofitResponseStatus status = RetrofitResponseStatus.OK;
	
	protected OnTaskFinishListener<Result> onTaskFinishListener;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setOnTaskFinishListener(OnTaskFinishListener<Result> onTaskFinishListener) {
		this.onTaskFinishListener = onTaskFinishListener;
	}
	
	protected void handleError(RetrofitError error, Context context) {
		if(error.isNetworkError()) {
			boolean internetConnected = ConnectionDetector.hasInternetConnectivity(context);
			if(internetConnected) {
				status = RetrofitResponseStatus.NETWORK_DOWN_ERROR;
			} else {
				status = RetrofitResponseStatus.NETWORK_UNREACHABLE_ERROR;
			}
		} else {
			status = RetrofitResponseStatus.UKNOWN_ERROR;
		}
	}
}
