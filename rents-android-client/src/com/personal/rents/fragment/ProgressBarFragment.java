package com.personal.rents.fragment;

import com.personal.rents.R;
import com.personal.rents.task.BaseAsyncTask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressBarFragment extends DialogFragment {
	
	private ProgressDialog progressDialog;

	private BaseAsyncTask<?, ?, ?> task;
	
	public BaseAsyncTask<?, ?, ?> getTask() {
		return task;
	}
	
	// when onTaskFinished called on listener, if dialog fragment isResumed dismiss it.
	// otherwise set task to null for the dialog to be dismissed when resumed.
	public void setTask(BaseAsyncTask<?, ?, ?> task) {
		this.task = task;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		progressDialog.setContentView(R.layout.progress_bar_fragment_layout);
		
		return progressDialog;
	}

	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.progress_bar_fragment_layout, container);
//		
//		getDialog().setCanceledOnTouchOutside(false);
//		
//		return view;
//	}
//
//	@Override
//	public Dialog getDialog() {
//		return progressDialog;
//	}

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
			task.cancel(false);
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
