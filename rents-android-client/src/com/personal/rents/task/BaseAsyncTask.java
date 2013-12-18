package com.personal.rents.task;

import com.personal.rents.task.listener.OnTaskFinishListener;

import android.os.AsyncTask;

public abstract class BaseAsyncTask<Params, Progress, Result> 
		extends AsyncTask<Params, Progress, Result> {
	
	protected OnTaskFinishListener onTaskFinishListener;

	public void setOnTaskFinishListener(OnTaskFinishListener onTaskFinishListener) {
		this.onTaskFinishListener = onTaskFinishListener;
	}
	
}
