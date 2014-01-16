package com.personal.rents.util;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.personal.rents.fragment.ErrorDialogFragment;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;

public class GoogleServicesUtil {
	
	private static final String ERROR_DIALOG_FRAGMENT_TAG = "ERROR_DIALOG_FRAGMENT_TAG";
	
	public static final String UNABLE_TO_RESOLVE_ERROR_MSG = "Serviciile Google sunt momentan"
			+ " indisponibile";
	
	public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	public static boolean servicesConnected(FragmentActivity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            showErrorDialog(resultCode, activity);

            return false;
        }
    }
	
	public static boolean checkServicesConnectedWithoutResolution(FragmentActivity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }

	public static void showErrorDialog(int errorCode, FragmentActivity activity) {
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity,
        		CONNECTION_FAILURE_RESOLUTION_REQUEST);
        if (errorDialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(activity.getSupportFragmentManager(), ERROR_DIALOG_FRAGMENT_TAG);
        }
	}

}
