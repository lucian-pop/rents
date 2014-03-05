package com.personal.rents.task.listener;

import java.util.List;

import com.personal.rents.webservice.response.ResponseStatusReason;

public interface OnLoadNextPageTaskFinishListener<T> {

	public void onTaskFinish(List<T> nextPageItems, ResponseStatusReason status);

}
