package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnLoadNextPageTaskFinishListener;
import com.personal.rents.util.GeneralConstants;

public class GetUserAddedRentsNextPageAsyncTask 
		extends LoadNextPageAsyncTask<Void, List<Rent>, Rent> {
	
	private Account account;
	
	public GetUserAddedRentsNextPageAsyncTask(Account account) {
		this.account = account;
	}

	@Override
	public LoadNextPageAsyncTask<Void, List<Rent>, Rent> newInstance(
			OnLoadNextPageTaskFinishListener<Rent> onLoadNextPageTaskFinishListener) {
		GetUserAddedRentsNextPageAsyncTask instance = 
				new GetUserAddedRentsNextPageAsyncTask(account);
		instance.setOnLoadNextPageTaskFinishListener(onLoadNextPageTaskFinishListener);
		
		return instance;
	}

	@Override
	protected List<Rent> doInBackground(Object... params) {
		Rent lastRent = (Rent) params[0];
		List<Rent> result = null;
		try {
			result = RentsClient.getUserAddedRentsNextPage(lastRent.rentAddDate, lastRent.rentId,
					GeneralConstants.PAGE_SIZE, account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return result;
	}
}
