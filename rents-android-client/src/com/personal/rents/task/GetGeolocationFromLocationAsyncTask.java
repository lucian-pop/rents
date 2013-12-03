package com.personal.rents.task;

import com.personal.rents.model.Address;
import com.personal.rents.rest.client.GeocodeClient;
import com.personal.rents.task.listener.OnGetGeolocationTaskFinishListener;

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
		Address address = GeocodeClient.getAddressFromLocation(params[0], params[1]);

		return address;
	}

	@Override
	protected void onPostExecute(Address result) {
		onGetGeolocationTaskFinishListener.onGetGeolocationTaskFinish(result);
	}
}
