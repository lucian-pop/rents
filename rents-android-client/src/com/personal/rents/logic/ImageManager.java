package com.personal.rents.logic;

import android.content.Context;
import android.graphics.Bitmap;

import com.personal.rents.rest.client.RentsImageClient;
import com.personal.rents.util.BitmapUtils;

public final class ImageManager {
	
	private static final int QUALITY = 60;

	public static byte[] resizeCompressImage(Context context, String imagePath, int destSize) {
		Bitmap image = BitmapUtils.getRelativeScaledImg(imagePath, destSize);
		Bitmap scaledImage = BitmapUtils.getAbsoluteScaledImg(context, image, imagePath, destSize);
		byte[] imageBytes = BitmapUtils.compressImg(scaledImage, QUALITY);
		
		// Recycle images.
		image.recycle();
		image = null;
		scaledImage.recycle();
		scaledImage = null;
		
		return imageBytes;
	}
	
	public static Bitmap resizeImage(Context context, String imagePath, int imageDestSize) {
		Bitmap image = BitmapUtils.getRelativeScaledImg(imagePath, imageDestSize);
		Bitmap scaledImage = BitmapUtils.getAbsoluteScaledImg(context, image, imagePath,
				imageDestSize);
		
		// Recycle image.
		image.recycle();
		image = null;
		
		return scaledImage;
	}

	public Bitmap execute(Context context, String imagePath, int imageDestSize) {
		Bitmap bitmap = RentsImageClient.getImageCache().get(imagePath);
		if(bitmap != null) {
			return bitmap;
		}
		
		bitmap = ImageManager.resizeImage(context, imagePath, imageDestSize);
		if(bitmap != null) {
			RentsImageClient.getImageCache().put(imagePath, bitmap);
		}
		
		return bitmap;
	}
}
