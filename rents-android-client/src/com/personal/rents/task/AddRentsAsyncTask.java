//package com.personal.rents.task;
//
//import java.util.List;
//
//import retrofit.RetrofitError;
//
//import com.google.android.gms.maps.model.LatLng;
//import com.personal.rents.logic.ImageManager;
//import com.personal.rents.logic.UserAccountManager;
//import com.personal.rents.model.Rent;
//import com.personal.rents.rest.client.RentsClient;
//import com.personal.rents.util.GeneralConstants;
//
//import android.content.Context;
//import android.util.Log;
//
//public class AddRentsAsyncTask extends BaseAsyncTask<Object, Void, Integer>{
//	
//	private static final String NULL_IMAGE_URI_ERROR = "Returned image URI is null";
//
//	private List<LatLng> positions;
//	
//	public AddRentsAsyncTask(List<LatLng> positions) {
//		this.positions = positions;
//	}
//
//	@Override
//	protected Integer doInBackground(Object... params) {
//		Context context = (Context) params[0];
//		for(int i = 0; i < positions.size(); i++) {
//			if (isCancelled()) {
//				return null;
//			}
//		
//			// Sleep in order to test if progress indicator is kicking in.
//			// and if we can cancel the task.
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
////		List<Rent> rents = RentsGenerator.generateRents(positions, context);
////		for(Rent rent : rents) {
////			if (isCancelled()) {
////				return null;
////			}
////			
////			try {
////				// Upload images to server.
////				uploadImages(rent, context);
////				
////				// Send rent to server.
////				rent = RentsClient.addRent(rent);
////			} catch(RetrofitError error) {
////				Log.e("SMART_TAG", "Error occured while uploading rents", error);
////				cancel(false);
////			}
////		}
//
//		return positions.size();
//	}
//
//	@Override
//	protected void onCancelled(Integer result) {
//		onTaskFinishListener.onTaskFinish(result);
//	}
//
//	@Override
//	protected void onPostExecute(Integer result) {
//		onTaskFinishListener.onTaskFinish(result);
//	}
//
//	private void uploadImages(Rent rent, Context context) {
//		byte[] imageBytes = null;
//		String imageURI = null;
//		int i = 0;
//		for(String imagePath : rent.rentImageURIs) {
//			i++;
//			imageBytes = ImageManager.resizeCompressImage(context, imagePath, 
//					GeneralConstants.DEST_IMG_SIZE);
//
//			imageURI = RentsClient.uploadImage(imageBytes, i + GeneralConstants.IMG_FILE_EXT, 
//					Integer.toString(UserAccountManager.getAccount(context).accountId),
//					Long.toString(rent.rentAddDate.getTime()));
//			
//			// Cancel task if an image failed to upload.
//			if(imageURI == null) {
//				throw RetrofitError.unexpectedError(imagePath, 
//						new NullPointerException(NULL_IMAGE_URI_ERROR));
//			}
//
//			Log.e("TEST_TAG", "**********Image URI is: " + imageURI);
//			rent.rentImageURIs.set(i, imageURI);
//		}
//	}
//
//}
