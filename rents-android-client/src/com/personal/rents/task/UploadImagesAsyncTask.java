package com.personal.rents.task;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.RetrofitError;

import android.content.Context;

import com.personal.rents.logic.ImageManager;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.util.GeneralConstants;

public class UploadImagesAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Rent> {
	
	public static AtomicBoolean completed;

	@Override
	protected Rent doInBackground(Object... params) {
		Rent rent = (Rent) params[0];
		Account account = (Account) params[1];
		Context context = (Context) params[2];

		try {
			uploadImages(rent, account, context);
			completed = new AtomicBoolean(true);
		} catch (RetrofitError error) {
			handleError(error);
		}
		
		return rent;
	}

	private void uploadImages(Rent rent, Account account, Context context) {
		byte[] imageBytes = null;
		int i = 0;
		for(RentImage image : rent.rentImages)  {
			imageBytes = ImageManager.resizeCompressImage(context, image.rentImageURI, 
					GeneralConstants.DEST_IMG_SIZE);
			RentImage uploadedImage = RentsClient.uploadRentImage(imageBytes, rent.rentId,
					account.tokenKey);

			if(uploadedImage != null) {
				rent.rentImages.set(i, uploadedImage);
			}
			
			++i;
		}
	}
}
