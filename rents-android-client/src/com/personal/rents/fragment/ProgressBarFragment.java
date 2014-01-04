package com.personal.rents.fragment;

import com.personal.rents.R;
import com.personal.rents.task.BaseAsyncTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class ProgressBarFragment extends Fragment {
	
	private ProgressBar progressBar;
	
	private int visibility = View.GONE;
	
	private int currentTaskId = -1;
	
	private BaseAsyncTask<?, ?, ?> task;
	
	public BaseAsyncTask<?, ?, ?> getTask() {
		return task;
	}

	public int getCurrentTaskId() {
		return currentTaskId;
	}

	public void setTask(BaseAsyncTask<?, ?, ?> task) {
		if(task != null) {
			task.setTaskId(++currentTaskId);
		}

		this.task = task;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		progressBar = (ProgressBar) inflater.inflate(R.layout.progress_bar_fragment_layout,
				container, true);
		progressBar.setVisibility(visibility);

		return progressBar;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(task == null) {
			dismiss();
		}
	}

	public void show() {
		progressBar.setVisibility(View.VISIBLE);
		
		visibility = View.VISIBLE;
	}
	
	public void dismiss() {
		cancelCurrentlyAssociatedTask();
		
		progressBar.setVisibility(View.GONE);
		
		visibility = View.GONE;
	}
	
	public void cancelCurrentlyAssociatedTask() {
		if(task != null) {
			task.cancel(true);
		}
	}
	
}
