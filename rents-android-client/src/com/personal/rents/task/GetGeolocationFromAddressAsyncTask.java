package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.model.Address;
import com.personal.rents.rest.client.GeocodeClient;

public class GetGeolocationFromAddressAsyncTask 
		extends ProgressBarFragmentAsyncTask<String, Void, Address> {

	@Override
	protected Address doInBackground(String... params) {
		
		Address address = null;
		try {
			address = GeocodeClient.getAddressFromAddress(params[0]);
		} catch(RetrofitError error) {
			handleError(error);
		}
		
		return address;
	}

}
