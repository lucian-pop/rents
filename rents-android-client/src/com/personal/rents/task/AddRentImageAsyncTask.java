package com.personal.rents.task;

import retrofit.RetrofitError;
import android.content.Context;

import com.personal.rents.logic.ImageManager;
import com.personal.rents.model.RentImage;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.util.GeneralConstants;

public class AddRentImageAsyncTask 
		extends ProgressBarFragmentAsyncTask<Object, Void, RentImage> {

	@Override
	protected RentImage doInBackground(Object... params) {
		String selectedImagePath = (String) params[0];
		int rentId = (Integer) params[1];
		String tokenKey = (String) params[2];
		Context context = (Context) params[3];

		byte[] imageBytes = ImageManager.resizeCompressImage(context, selectedImagePath, 
				GeneralConstants.DEST_IMG_SIZE);
		RentImage addedRentImage = null;
		try {
			addedRentImage = RentsClient.uploadRentImage(imageBytes, rentId, tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		}

		return addedRentImage;
	}

}
