package com.personal.rents.task.listener;

import com.personal.rents.webservice.response.ResponseStatusReason;

public interface OnNetworkTaskFinishListener {
	
	public void onTaskFinish(Object result, ResponseStatusReason status);

}
