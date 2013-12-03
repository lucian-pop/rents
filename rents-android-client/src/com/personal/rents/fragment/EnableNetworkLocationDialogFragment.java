package com.personal.rents.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;

public class EnableNetworkLocationDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setTitle("Dummy title");
		dialogBuilder.setMessage("Dummy Message");
		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Nothing to do.
			}
        });
		dialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int id) {
				Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(myIntent);
            }
        });
		
		return dialogBuilder.create();
	}

	
}
