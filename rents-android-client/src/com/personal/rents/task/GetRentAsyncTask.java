package com.personal.rents.task;

import retrofit.RetrofitError;

import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;

public class GetRentAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Rent>{

	@Override
	protected Rent doInBackground(Object... params) {
		Integer rentId = (Integer) params[0];
		
		Rent rent = null;
		try {
			rent = RentsClient.getDetailedRent(rentId);
		} catch(RetrofitError error) {
			handleError(error);
		}
		
		return rent;
	}

	@Override
	protected void onCancelled(Rent result) {
		if(progressBarFragment != null) {
			progressBarFragment.taskFinished(null, taskId, status);
		}
	}
}
