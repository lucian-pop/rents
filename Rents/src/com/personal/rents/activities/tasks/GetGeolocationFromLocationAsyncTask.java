package com.personal.rents.activities.tasks;

import com.personal.rents.model.Address;
import com.personal.rents.rest.clients.GeocodingRESTClient;

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
		Address address = GeocodingRESTClient.getAddressFromLocation(params[0], params[1]);

		return address;
	}

	@Override
	protected void onPostExecute(Address result) {
		onGetGeolocationTaskFinishListener.onGetGeolocationTaskFinish(result);
	}
}
