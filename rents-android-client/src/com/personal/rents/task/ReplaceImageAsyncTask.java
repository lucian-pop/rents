package com.personal.rents.task;

import retrofit.RetrofitError;

import android.content.Context;

import com.personal.rents.logic.ImageManager;
import com.personal.rents.model.RentImage;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.OperationFailedException;
import com.personal.rents.rest.error.UnauthorizedException;
import com.personal.rents.util.GeneralConstants;

public class ReplaceImageAsyncTask
		extends ProgressBarFragmentAsyncTask<Object, Void, RentImage> {

	@Override
	protected RentImage doInBackground(Object... params) {
		String selectedImagePath = (String) params[0];
		RentImage rentImage = (RentImage) params[1];
		String tokenKey = (String) params[2];
		Context context = (Context) params[3];

		byte[] imageBytes = ImageManager.resizeCompressImage(context, selectedImagePath, 
				GeneralConstants.DEST_IMG_SIZE);
		RentImage updatedRentImage = null;
		try {
			updatedRentImage = RentsClient.replaceRentImage(imageBytes, rentImage, tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
		} catch(UnauthorizedException unauthorizedError) {
			handleUnauthorizedError();
		} catch (OperationFailedException operationFailedError) {
			handleOperationFailedError();
		}

		return updatedRentImage;
	}
}
