package com.personal.rents.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {

	private Dialog dialog;
	
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialog;
    }

}
