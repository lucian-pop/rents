package com.personal.rents.task.listener;

public interface OnTaskFinishListener<T> {

	public void onTaskFinish(T result, int taskId);
}
