package com.personal.rents.task;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.RetrofitError;

import android.content.Context;

import com.personal.rents.logic.ImageManager;
import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.rest.error.OperationFailedException;
import com.personal.rents.rest.error.UnauthorizedException;
import com.personal.rents.util.GeneralConstants;

public class UploadImagesAsyncTask extends ProgressBarFragmentAsyncTask<Object, Void, Rent>{
	
	public static AtomicBoolean completed;
	
	private List<String> imagesPaths;
	
	public UploadImagesAsyncTask(List<String> imagesPaths) {
		this.imagesPaths = imagesPaths;
	}

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
		} catch (UnauthorizedException unauthorizedError) {
			handleUnauthorizedError();
		} catch (OperationFailedException e) {
			handleOperationFailedError();
		}
		
		return rent;
	}

	private void uploadImages(Rent rent, Account account, Context context) 
			throws UnauthorizedException, OperationFailedException {
		byte[] imageBytes = null;
		for(String imagePath : imagesPaths)  {
			imageBytes = ImageManager.resizeCompressImage(context, imagePath, 
					GeneralConstants.DEST_IMG_SIZE);
			String imageURI = RentsClient.uploadImage(imageBytes, rent.rentId, account.tokenKey);

			if(imageURI != null) {
				rent.rentImageURIs.add(imageURI);
			}
		}
	}
}
