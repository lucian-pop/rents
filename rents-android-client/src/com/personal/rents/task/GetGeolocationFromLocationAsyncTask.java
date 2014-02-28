package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.model.Address;
import com.personal.rents.rest.client.GeocodeClient;

public class GetGeolocationFromLocationAsyncTask 
		extends ProgressBarFragmentAsyncTask<Double, Void, Address> {

	@Override
	protected Address doInBackground(Double... params) {		
		Address address = null;
		try{
			address = GeocodeClient.getAddressFromLocation(params[0], params[1]);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return address;
	}
}
