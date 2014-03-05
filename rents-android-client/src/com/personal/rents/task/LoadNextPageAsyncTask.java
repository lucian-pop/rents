package com.personal.rents.task;

import java.util.List;

import com.personal.rents.task.listener.OnLoadNextPageTaskFinishListener;

// Make class abstract in order to be able to remove the doInBackgroundMethod.
public abstract class LoadNextPageAsyncTask<Progress, Result extends List<T>, T> 
		extends NetworkAsyncTask<Object, Progress, Result> {
	
	private OnLoadNextPageTaskFinishListener<T> onLoadNextPageTaskFinishListener;
	
	public void setOnLoadNextPageTaskFinishListener(
			OnLoadNextPageTaskFinishListener<T> onLoadNextPageTaskFinishListener) {
		this.onLoadNextPageTaskFinishListener = onLoadNextPageTaskFinishListener;
	}

	public abstract LoadNextPageAsyncTask<Progress, List<T>, T> newInstance(
			OnLoadNextPageTaskFinishListener<T> onLoadNextPageTaskFinishListener);


	@Override
	protected void onPostExecute(Result result) {
		onLoadNextPageTaskFinishListener.onTaskFinish(result, statusReason);
	}
	
}
