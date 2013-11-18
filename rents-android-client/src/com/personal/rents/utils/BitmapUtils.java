package com.personal.rents.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public final class BitmapUtils {

	private BitmapUtils(){
	}
	
	public static Bitmap getRelativeScaledImg(String imagePath, int destSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, destSize);
		options.inScaled = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		
		return BitmapFactory.decodeFile(imagePath, options);
	}
	
	public static Bitmap getAbsoluteScaledImg(Context context, Bitmap image, int destSize, 
			String imagePath) {
		int maxSize = Math.max(image.getWidth(), image.getHeight());
		float scale = (float) destSize / maxSize;
		if(scale > 1) {
			scale = 1;
		}
		
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.postScale(scale, scale);
		
		// Solves the bug of device rotated image.
		int orientation = MediaUtils.getImageOrientation(context,  imagePath);

		if(orientation > 0) {
			scaleMatrix.postRotate(orientation);
		}
		
		Bitmap scaledImg = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(),
				scaleMatrix, true);
		image.recycle();
		image = null;

		return scaledImg;
	}
	
	protected static int calculateInSampleSize(int srcWidth, int srcHeight, int destSize) {
		int maxSize = Math.max(srcWidth, srcHeight);
	    int inSampleSize = 1;
	    while (maxSize / inSampleSize / 2 >= destSize) {
	    	inSampleSize *= 2;
	    }
		
		return inSampleSize;
	}
}
