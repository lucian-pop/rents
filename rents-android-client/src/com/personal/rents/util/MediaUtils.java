package com.personal.rents.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.personal.rents.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

public final class MediaUtils {
	
	private static final String LOG_TAG = MediaUtils.class.getSimpleName();
	
	private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
	
	private static final String IMAGE_FORMAT = ".jpg";
	
	private static final Uri IMAGE_PROVIDER_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	private MediaUtils(){
	}
	
	public static String getImagePath(Context context, Uri uri) {
		String[] projection = {MediaColumns.DATA};
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

		if(cursor != null && cursor.moveToFirst()) {
			String filePath = cursor.getString(0);
			cursor.close();
			
			return filePath; 
		}
		
		return uri.getPath();
	}
	
	public static int getImageOrientation(Context context, String imagePath) {
		Uri imageUri = getImageContentUri(context, imagePath);
		if(imageUri == null) {
			return -1;
		}
		
        String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
        	int orientation = cursor.getInt(0);
        	cursor.close();

            return orientation;
        }

        return getExifOrientationAttribute(imagePath);
    }

	public static Uri getImageContentUri(Context context, String imagePath) {
        String[] projection = new String[] {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA + "=? ";
        String[] selectionArgs = new String[] {imagePath};
        Cursor cursor = context.getContentResolver().query(IMAGE_PROVIDER_URI, projection, 
        		selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            int imageId = cursor.getInt(0);
            cursor.close();

            return Uri.withAppendedPath(IMAGE_PROVIDER_URI, Integer.toString(imageId));
        } 

        if (new File(imagePath).exists()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, imagePath);

            return context.getContentResolver().insert(
            			MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } 

        return null;
    }
	
	public static File createImageFile(Context context) {
		String timeStamp = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
			.format(new Date());
		File imageFile = null;
		try {
			imageFile = File.createTempFile(timeStamp, IMAGE_FORMAT, getAlbumDir(context));
		} catch (IOException e) {
			Log.e(LOG_TAG, "Unable to create temporary image file");
		}

		return imageFile;
	}
	
	public static void addImageToGallery(Context context, String imagePath) {
	    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri imageUri = Uri.fromFile(new File(imagePath));
	    intent.setData(imageUri);

	    context.sendBroadcast(intent);
	    
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        	Log.e(LOG_TAG, "Exception occured while waiting for the media scanner to add the image" +
        			" to gallery.", e);
        }
	}
	
	public static File getAlbumDir(Context context) {
		File albumDir = null;
		String appName = context.getString(R.string.app_name);
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			albumDir = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES), appName);
			if(!albumDir.exists()) {
				albumDir.mkdirs();
			}
		} else {
			Log.v(LOG_TAG, "External storage is not mounted.");
		}

		return albumDir;
	}
	
	/**
	 *  Test on Samsung Galaxy S3, HTC models, Sony Xperia etc.
	 * 
	 */
	private static int getExifOrientationAttribute(String imagePath) {
		int orientation = -1;
		try {
			ExifInterface exif = new ExifInterface(imagePath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 
					ExifInterface.ORIENTATION_NORMAL);
			
			switch (exifOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_270:
					orientation = 270;
	
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					orientation = 180;
					
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					orientation = 90;
					
					break;
				default:
					break;
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "Unable to get image exif orientation", e);
		}
		
		return orientation;
	}
}
