package com.personal.rents.task;

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

}
