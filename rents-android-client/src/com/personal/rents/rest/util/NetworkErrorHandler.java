package com.personal.rents.rest.util;

import com.personal.rents.activity.LoginActivity;
import com.personal.rents.util.ConnectionDetector;
import com.personal.rents.webservice.response.ResponseStatusReason;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public final class NetworkErrorHandler {

	public static final String NETWORK_DOWN_ERROR_MSG = "Conectarea la serverele noastre a esuat."
			+ " Asteptati cateva momente si incercati din nou.";

	public static final String NETWORK_UNREACHABLE_ERROR_MSG = "Conectarea la internet a esuat."
			+ " Activati internetul si incercati din nou.";
	
	public static final String UNKNOWN_ERROR_MSG = "Operatia nu a putut fi finalizata."
			+" Va rog incercati din nou.";
	
	public static final void handleRetrofitError(ResponseStatusReason status, Object result,
			Activity activity) {
		switch (status) {
			case NETWORK_ERROR:
				if(ConnectionDetector.hasInternetConnectivity(activity)) {
					Toast.makeText(activity, NETWORK_DOWN_ERROR_MSG, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity, NETWORK_UNREACHABLE_ERROR_MSG,Toast.LENGTH_LONG)
						.show();
				}
	
				break;
			
			case UNAUTHORIZED_ERROR:
				// Use start activity for result to retrieve you to the previous activity.
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				
				break;
				

			case OPERATION_FAILED_ERROR:
				Toast.makeText(activity, UNKNOWN_ERROR_MSG, Toast.LENGTH_LONG).show();

				break;
			
			case ACCOUNT_CONFLICT_ERROR:
				Toast.makeText(activity, "Exista deja un cont cu acest email sau numar de telefon.",
						Toast.LENGTH_LONG).show();
				
				break;
				
			case BAD_CREDENTIALS_ERROR:
				Toast.makeText(activity, "Emailul sau parola sunt gresite. Va rugam incercati din nou.",
						Toast.LENGTH_LONG).show();
				
				break;

			case UNKNOWN_ERROR:
				Toast.makeText(activity, UNKNOWN_ERROR_MSG, Toast.LENGTH_LONG).show();

				break;
			
			default:
				
				break;
		}
	}
}
