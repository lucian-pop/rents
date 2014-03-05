package com.personal.rents.task;

import android.util.Log;

import com.personal.rents.fragment.ProgressBarFragment;

public abstract class ProgressBarFragmentAsyncTask<Params, Progress, Result> 
		extends NetworkAsyncTask<Params, Progress, Result> {
	
	protected int taskId;
	
	protected ProgressBarFragment progressBarFragment;

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public void setProgressBarFragment(ProgressBarFragment progressBarFragment) {
		this.progressBarFragment = progressBarFragment;
	}

	@Override
	protected void onCancelled(Result result) {
		Log.e("TEST_TAG", "*********On CANCELED Task called");
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(null, taskId, statusReason); 
		}
	}
	
	@Override
	protected void onPostExecute(Result result) {
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(result, taskId, statusReason); 
		}
	}
}
