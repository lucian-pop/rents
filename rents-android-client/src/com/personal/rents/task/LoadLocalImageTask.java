package com.personal.rents.task;

import com.personal.rents.logic.ImageManager;
import com.personal.rents.rest.client.RentsImageClient;

import android.content.Context;
import android.graphics.Bitmap;

public class LoadLocalImageTask {
	
	private String imagePath;
	
	private int destSize;
	
	public LoadLocalImageTask(String imagePath, int destSize) {
		this.imagePath = imagePath;
		this.destSize = destSize;
	}
	
	public Bitmap execute(Context context) {
		Bitmap bitmap = RentsImageClient.getImageCache().get(imagePath);
		if(bitmap != null) {
			return bitmap;
		}
		
		bitmap = ImageManager.resizeImage(context, imagePath, destSize);
		if(bitmap != null) {
			RentsImageClient.getImageCache().put(imagePath, bitmap);
		}
		
		return bitmap;
	}
}
