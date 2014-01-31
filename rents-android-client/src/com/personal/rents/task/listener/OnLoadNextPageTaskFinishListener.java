package com.personal.rents.task.listener;

import java.util.List;

import com.personal.rents.rest.util.RetrofitResponseStatus;

public interface OnLoadNextPageTaskFinishListener<T> {

	public void onTaskFinish(List<T> nextPageItems, RetrofitResponseStatus status);

}
