package com.personal.rents.activity;

import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.util.ConnectionDetector;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class BaseActivity extends ActionBarActivity {

	@Override
	protected void onStart() {
		super.onStart();
		
		if(!ConnectionDetector.hasInternetConnectivity(this)) {
			Toast.makeText(this, NetworkErrorHandler.NETWORK_UNREACHABLE_ERROR_MSG, Toast.LENGTH_LONG)
				.show();
		}
	}

}
