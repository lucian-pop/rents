package com.personal.rents.tasks;

import android.os.AsyncTask;

import com.personal.rents.model.Address;
import com.personal.rents.rest.clients.GeocodingRESTClient;

public class GetGeolocationFromAddressAsyncTask extends AsyncTask<String, Void, Address> {
	
	private OnGetGeolocationTaskFinishListener onGetGeolocationTaskFinishListener;

	public GetGeolocationFromAddressAsyncTask(
			OnGetGeolocationTaskFinishListener onGetGeolocationTaskFinishListener) {
		super();

		this.onGetGeolocationTaskFinishListener = onGetGeolocationTaskFinishListener;
	}

	@Override
	protected Address doInBackground(String... params) {
		return GeocodingRESTClient.getAddressFromAddress(params[0]);
	}

	@Override
	protected void onPostExecute(Address result) {
		if(onGetGeolocationTaskFinishListener != null) {
			onGetGeolocationTaskFinishListener.onGetGeolocationTaskFinish(result);
		}
	}
}
