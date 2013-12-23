package com.personal.rents.task;

import java.util.List;

import retrofit.RetrofitError;

import com.personal.rents.logic.ImageManager;
import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsClient;
import com.personal.rents.task.listener.OnAddRentTaskFinishListener;
import com.personal.rents.util.ConnectionDetector;
import com.personal.rents.util.GeneralConstants;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AddRentAsyncTask extends AsyncTask<Object, Void, Rent> {
	
	private static final String NULL_IMAGE_URI_ERROR = "Returned image URI is null";
	
	private OnAddRentTaskFinishListener onAddRentTaskFinishListener;
	
	private List<String> imagesPaths;
	
	public AddRentAsyncTask(List<String> imagesPaths, 
			OnAddRentTaskFinishListener onAddRentTaskFinishListener) {
		this.imagesPaths = imagesPaths;
		this.onAddRentTaskFinishListener = onAddRentTaskFinishListener;
	}

	@Override
	protected Rent doInBackground(Object... params) {
		Rent rent = (Rent) params[0];
		Context context = (Context) params[1];
		
		try {
			// Upload images to server.
			uploadImages(rent, context);
			
			// Send rent to server.
			rent = RentsClient.addRent(rent);
		} catch(RetrofitError error) {
			if(error.isNetworkError()) {
				boolean internetConnected = ConnectionDetector.hasInternetConnectivity(context);
				if(internetConnected) {
					rent.rentUploadStatus = 1;
				} else {
					rent.rentUploadStatus = 2;
				}
			} else {
				rent.rentUploadStatus = 3;
			}
		}
		
		return rent;
	}

	@Override
	protected void onPostExecute(Rent result) {
		onAddRentTaskFinishListener.onTaskFinish(result);
	}
	
	private void uploadImages(Rent rent, Context context) {
		byte[] imageBytes = null;
		String imageURI = null;
		int i = 0;
		for(String imagePath : imagesPaths) {
			i++;
			imageBytes = ImageManager.resizeCompressImage(context, imagePath, 
					GeneralConstants.DEST_IMG_SIZE);

			imageURI = RentsClient.uploadImage(imageBytes, i + GeneralConstants.IMG_FILE_EXT, 
					Integer.toString(UserAccountManager.getAccount(context).accountId),
					Long.toString(rent.rentAddDate.getTime()));
			
			// Cancel task if an image failed to upload.
			if(imageURI == null) {
				throw RetrofitError.unexpectedError(imagePath, 
						new NullPointerException(NULL_IMAGE_URI_ERROR));
			}

			Log.e("TEST_TAG", "**********Image URI is: " + imageURI);
			rent.rentImageURIs.add(imageURI);
		}
	}

}
