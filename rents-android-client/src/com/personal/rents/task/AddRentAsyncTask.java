package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.personal.rents.logic.ImageManager;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.UnauthorizedException;
import com.personal.rents.util.GeneralConstants;

import android.content.Context;

public class AddRentAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Rent> {
	
	private static final String NULL_IMAGE_URI_ERROR = "Returned image URI is null";
	
	private List<String> imagesPaths;
	
	public AddRentAsyncTask(List<String> imagesPaths) {
		this.imagesPaths = imagesPaths;
	}

	@Override
	protected Rent doInBackground(Object... params) {
		Rent rent = (Rent) params[0];
		Account account = (Account) params[1];
		Context context = (Context) params[2];
		
		try {
			// Upload images to server.
			uploadImages(rent, account, context);
			
			// Send rent to server.
			rent = RentsClient.addRent(rent, account.tokenKey);
		} catch(RetrofitError error) {
			handleError(error);
			
			rent = null;
		} catch(UnauthorizedException unauthorizedError) {
			handleUnauthorizedError();
			
			rent = null;
		}
		
		return rent;
	}
	
	private void uploadImages(Rent rent, Account account, Context context)
			throws UnauthorizedException {
		byte[] imageBytes = null;
		String imageURI = null;
		int i = 0;
		for(String imagePath : imagesPaths) {
			i++;
			imageBytes = ImageManager.resizeCompressImage(context, imagePath, 
					GeneralConstants.DEST_IMG_SIZE);

			imageURI = RentsClient.uploadImage(imageBytes, i + GeneralConstants.IMG_FILE_EXT, 
					Long.toString(rent.rentAddDate.getTime()), account.tokenKey);
			
			// Cancel task if an image failed to upload.
			if(imageURI == null) {
				throw RetrofitError.unexpectedError(imagePath, 
						new NullPointerException(NULL_IMAGE_URI_ERROR));
			}

			rent.rentImageURIs.add(imageURI);
		}
	}

}
