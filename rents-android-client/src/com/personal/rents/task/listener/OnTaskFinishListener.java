package com.personal.rents.task.listener;

import android.content.Context;
import android.widget.Toast;

import com.personal.rents.rest.util.RetrofitResponseStatus;

public abstract class OnTaskFinishListener<T> {
	
	public abstract void onTaskFinish(T result, int taskId, RetrofitResponseStatus status);
	
	protected abstract void handleOkStatus(T result, int taskId);
	
	protected void handleResponse(T result, int taskId, RetrofitResponseStatus status, 
			Context context) {
		switch (status) {
			case NETWORK_DOWN_ERROR:
				Toast.makeText(context, RetrofitResponseStatus.NETWORK_DOWN_ERROR.getMessage(), 
						Toast.LENGTH_LONG).show();
	
				break;
			case NETWORK_UNREACHABLE_ERROR:
				Toast.makeText(context, RetrofitResponseStatus.NETWORK_UNREACHABLE_ERROR.getMessage(),
						Toast.LENGTH_LONG).show();
	
				break;
			case UKNOWN_ERROR:
				Toast.makeText(context, RetrofitResponseStatus.UKNOWN_ERROR.getMessage(), 
						Toast.LENGTH_LONG).show();
	
				break;
			default:
				handleOkStatus(result, taskId);
				
				break;
		}
	}

}
