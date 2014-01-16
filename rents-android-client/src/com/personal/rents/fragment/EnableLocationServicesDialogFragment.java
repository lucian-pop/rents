package com.personal.rents.fragment;

import com.personal.rents.R;
import com.personal.rents.logic.UserPreferencesManager;
import com.personal.rents.util.ActivitiesContract;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.CheckBox;

public class EnableLocationServicesDialogFragment extends DialogFragment {
	
	private boolean enableDoNotShowDialog;
	
	private boolean enableNetworkLocationService;
	
	private CheckBox rememberSelectionCheckBox;;
	
	public static EnableLocationServicesDialogFragment newInstance(boolean enableDoNotShowDialog,
			boolean enableNetworkLocationService) {
		EnableLocationServicesDialogFragment enableLocationServicesDialogFragment = 
				new EnableLocationServicesDialogFragment();
		enableLocationServicesDialogFragment.enableDoNotShowDialog = enableDoNotShowDialog;
		enableLocationServicesDialogFragment.enableNetworkLocationService =
				enableNetworkLocationService;
		
		return enableLocationServicesDialogFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

		if(enableDoNotShowDialog) {
			rememberSelectionCheckBox = (CheckBox) LayoutInflater.from(getActivity())
					.inflate(R.layout.enable_location_services_dialog_content_layout, null);
			dialogBuilder.setView(rememberSelectionCheckBox);
		}
		
		if(!enableNetworkLocationService) {
			dialogBuilder.setTitle(R.string.enable_location_services_dialog_title);
			dialogBuilder.setMessage(R.string.enable_location_services_dialog_message);
		} else {
			dialogBuilder.setTitle(R.string.enable_nt_location_service_dialog_title);
			dialogBuilder.setMessage(R.string.enable_nt_location_service_dialog_message);
		}

		dialogBuilder.setNegativeButton("Anulati", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveUserPreferences();
			}
        });
		dialogBuilder.setPositiveButton("Activati", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivityForResult(intent, 
                		ActivitiesContract.LOCATION_SERVICES_REQ_CODE);

                saveUserPreferences();
            }
        });
		
		return dialogBuilder.create();
	}
	
	private void saveUserPreferences() {
		if(!enableDoNotShowDialog) {
			return;
		}

        if(!enableNetworkLocationService) {
        	UserPreferencesManager.updateShowEnableLocationServices(
    				!rememberSelectionCheckBox.isChecked(),getActivity());
        } else {
        	UserPreferencesManager.updateShowEnableNetworkLocationService(
    				!rememberSelectionCheckBox.isChecked(), getActivity());
        }
	}

}
