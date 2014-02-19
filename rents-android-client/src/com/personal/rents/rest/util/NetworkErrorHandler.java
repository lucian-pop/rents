package com.personal.rents.rest.util;

import com.personal.rents.activity.LoginActivity;
import com.personal.rents.util.ConnectionDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public final class NetworkErrorHandler {

	public static final String NETWORK_DOWN_ERROR_MSG = "Conectarea la serverele noastre a esuat."
			+ " Asteptati cateva momente si incercati din nou.";

	public static final String NETWORK_UNREACHABLE_ERROR_MSG = "Conectarea la internet a esuat."
			+ " Activati internetul si incercati din nou.";
	
	public static final String UNKNOWN_ERROR_MSG = "Operatia nu a putut fi finalizata."
			+" Va rog incercati din nou.";
	
	public static final void handleRetrofitError(RetrofitResponseStatus status, Context context) {
		switch (status) {
			case NETWORK_ERROR:
				if(ConnectionDetector.hasInternetConnectivity(context)) {
					Toast.makeText(context, NETWORK_DOWN_ERROR_MSG, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, NETWORK_UNREACHABLE_ERROR_MSG,Toast.LENGTH_LONG).show();
				}
	
				break;
			
			case UNAUTHORIZED_ERROR:
				Activity activity = (Activity) context;
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				
				break;

			case UKNOWN_ERROR:
				Toast.makeText(context, UNKNOWN_ERROR_MSG, Toast.LENGTH_LONG).show();
	
				break;
			default:
				
				break;
		}
	}

}
