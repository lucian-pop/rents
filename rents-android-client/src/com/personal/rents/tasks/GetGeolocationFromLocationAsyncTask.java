package com.personal.rents.tasks;

import com.personal.rents.model.Address;
import com.personal.rents.rest.GeocodeRESTClient;

import android.os.AsyncTask;

public class GetGeolocationFromLocationAsyncTask extends AsyncTask<Double, Void, Address> {
	
	private OnGetGeolocationTaskFinishListener onGetGeolocationTaskFinishListener;
	
	public GetGeolocationFromLocationAsyncTask(
			OnGetGeolocationTaskFinishListener onGetGeolocationTaskFinishListener) {
		super();
		this.onGetGeolocationTaskFinishListener = onGetGeolocationTaskFinishListener;
	}

	@Override
	protected Address doInBackground(Double... params) {		
		Address address = GeocodeRESTClient.getAddressFromLocation(params[0], params[1]);

		return address;
	}

	@Override
	protected void onPostExecute(Address result) {
		onGetGeolocationTaskFinishListener.onGetGeolocationTaskFinish(result);
	}
}
