package com.personal.rents.fragment;

import com.personal.rents.R;
import com.personal.rents.task.listener.OnProgressDialogDismissListener;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;

public class ProgressDialogFragment extends DialogFragment {

	private int messageId;
	
	private long timeout;
	
	private OnProgressDialogDismissListener onProgressDialogDismissListener;
	
	private ProgressDialog progressDialog;
	
	private boolean timeoutReached = false;
	
	private Handler handler = new Handler();
	
	private Runnable onTimeoutTask = new Runnable() {
		@Override
		public void run() {
			timeoutReached = true;
			ProgressDialogFragment.this.dismiss();
		}
	};
	
	public static ProgressDialogFragment newInstance(int messageId, long timeout, 
			OnProgressDialogDismissListener onProgressDialogDismissListener) {
		
		ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
		progressDialogFragment.messageId = messageId;
		progressDialogFragment.timeout = timeout;
		progressDialogFragment.onProgressDialogDismissListener = onProgressDialogDismissListener;

		return progressDialogFragment;
	}

	public void setOnProgressDialogDismissListener(OnProgressDialogDismissListener
			onProgressDialogDismissListener) {
		this.onProgressDialogDismissListener = onProgressDialogDismissListener;
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
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(getResources().getString(messageId));
		progressDialog.show();

		return progressDialog;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		timeoutReached = false;
		handler.postDelayed(onTimeoutTask, timeout);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		resetOnTimeoutTask();
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

		resetOnTimeoutTask();
		onProgressDialogDismissListener.onDialogDismiss(timeoutReached);
	}
	
	public void reset() {
		resetOnTimeoutTask();
		onProgressDialogDismissListener = null;
	}
	
	private void resetOnTimeoutTask() {
		if(handler.hasMessages(0)) {
			handler.removeCallbacks(onTimeoutTask);
		}
	}

}
