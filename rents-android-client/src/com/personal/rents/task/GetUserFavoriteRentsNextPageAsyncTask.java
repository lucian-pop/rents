package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.personal.rents.model.Account;
import com.personal.rents.model.view.RentFavoriteView;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnLoadNextPageTaskFinishListener;
import com.personal.rents.util.GeneralConstants;

public class GetUserFavoriteRentsNextPageAsyncTask 
		extends LoadNextPageAsyncTask<Void, List<RentFavoriteView>, RentFavoriteView> {
	
	private Account account;
	
	public GetUserFavoriteRentsNextPageAsyncTask(Account account) {
		this.account = account;
	}

	@Override
	public LoadNextPageAsyncTask<Void, List<RentFavoriteView>, RentFavoriteView> newInstance(
			OnLoadNextPageTaskFinishListener<RentFavoriteView> onLoadNextPageTaskFinishListener) {
		GetUserFavoriteRentsNextPageAsyncTask instance = 
				new GetUserFavoriteRentsNextPageAsyncTask(account);
		instance.setOnLoadNextPageTaskFinishListener(onLoadNextPageTaskFinishListener);
		
		return instance;
	}

	@Override
	protected List<RentFavoriteView> doInBackground(Object... params) {
		RentFavoriteView lastRentFavoriteView = (RentFavoriteView) params[0];
		List<RentFavoriteView> result = null;
		try {
			result = RentsClient.getUserFavoriteRentsNextPage(
					lastRentFavoriteView.rentFavoriteAddDate, GeneralConstants.PAGE_SIZE,
					account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return result;
	}
}
