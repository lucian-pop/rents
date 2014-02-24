package com.personal.rents.fragment;

import com.personal.rents.R;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.ProgressBarFragmentAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class ProgressBarFragment extends Fragment {
	
	private ProgressBar progressBar;
	
	private int visibility = View.GONE;
	
	private int currentTaskId = -1;
	
	private ProgressBarFragmentAsyncTask<?, ?, ?> task;
	
	private OnNetworkTaskFinishListener onTaskFinishListener;
	
	private static OnNetworkTaskFinishListener dummyTaskFinishListener =
			new OnNetworkTaskFinishListener() {
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
		}
	};
	
	public int getVisibility() {
		return visibility;
	}

	public void setTask(ProgressBarFragmentAsyncTask<?, ?, ?> task) {
		if(task != null) {
			task.setTaskId(++currentTaskId);
			task.setProgressBarFragment(this);
		}

		this.task = task;
	}
	
	public void setOnTaskFinishListener(OnNetworkTaskFinishListener onTaskFinishListener) {
		this.onTaskFinishListener = onTaskFinishListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		progressBar = (ProgressBar) inflater.inflate(R.layout.progress_bar_layout,
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

	public void dismiss() {
		cancelCurrentlyAssociatedTask();
		
		progressBar.setVisibility(View.GONE);
		visibility = View.GONE;
	}
	
	public void reset() {
		cancelCurrentlyAssociatedTask();
		resetTaskFinishListener();
	}
	
	public void resetTaskFinishListener() {
		onTaskFinishListener = dummyTaskFinishListener;
	}

	public void show() {
		progressBar.setVisibility(View.VISIBLE);
		visibility = View.VISIBLE;
	}
	
	public void cancelCurrentlyAssociatedTask() {
		if(task != null) {
			task.cancel(true);
			task.setProgressBarFragment(null);
		}
	}
	
    public synchronized void taskFinished(Object result, int taskId, 
    		RetrofitResponseStatus status) {
    	// ProgressBarFragment is associated with a different task.
    	if(taskId != currentTaskId) {
    		Log.e("TEST_TAG", "***********DIFFERENT Task ids");
    		return;
    	}

    	Log.e("TEST_TAG", "***********SAME Task ids");
        if (isResumed()) {
            dismiss();
        }

        // If we aren't resumed, setting the task to null will allow us to dismiss ourselves in
        // onResume().
        task = null;

        // Tell the fragment(or in our case the activity) that we are done.
        onTaskFinishListener.onTaskFinish(result, status);
    }

}
