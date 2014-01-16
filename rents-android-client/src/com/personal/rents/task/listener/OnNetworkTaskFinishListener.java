package com.personal.rents.task.listener;

import com.personal.rents.rest.util.RetrofitResponseStatus;

public interface OnNetworkTaskFinishListener {
	
	public void onTaskFinish(Object result, int taskId, RetrofitResponseStatus status);

}
