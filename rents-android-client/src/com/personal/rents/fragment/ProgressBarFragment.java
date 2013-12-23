package com.personal.rents.fragment;

import com.personal.rents.R;
import com.personal.rents.task.BaseAsyncTask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;

public class ProgressBarFragment extends DialogFragment {
	
	private int currentTaskId = -1;
	
	private ProgressDialog progressDialog;

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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), 
				R.style.Theme_Background_Not_Dimmed);
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(true);
		progressDialog.show();
		progressDialog.setContentView(R.layout.progress_bar_fragment_layout);

		return progressDialog;
	}

	@Override
	public void onDestroyView() {
		// This is a workaround for what apparently is a bug. If you don't have it, here the dialog
		// will be dismissed on rotation, so tell it not to dismiss.
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		
		if(task != null) {
			task.cancel(true);
		}
	}

    @Override
    public void onResume()
    {
        super.onResume();

        // The task has finished while the dialog wasn't in the resume state (paused etc.).
        if (task == null)
            dismiss();
    }

}
