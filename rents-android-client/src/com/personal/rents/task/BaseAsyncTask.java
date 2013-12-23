package com.personal.rents.task;

import com.personal.rents.task.listener.OnTaskFinishListener;

import android.os.AsyncTask;

public abstract class BaseAsyncTask<Params, Progress, Result> 
		extends AsyncTask<Params, Progress, Result> {
	
	private int taskId;
	
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
	
}
