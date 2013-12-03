package com.personal.rents.util;

import com.personal.rents.util.BitmapUtils;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

public class BitmapUtilsTest extends AndroidTestCase {

	// Set to path of a phone internal storage image.
	private static final String IMAGE_PATH = "/storage/sdcard0/Bluetooth/20130922_135834.jpg";
	
	private static final int DEST_SIZE = 800;

	public void testCalculateInSampleSizeWithMaxSizePowerOfTwoGrThanDestSize() {
		int inSampleSize = BitmapUtils.calculateInSampleSize(3400, 2400, DEST_SIZE);
		
		assertTrue(inSampleSize / 2 >= 1);
	}
	
	public void testCalculateInSampleSizeWithMaxSizeNotPowerOfTwoGrThanDestSize() {
		int inSampleSize = BitmapUtils.calculateInSampleSize(2400, 1600, DEST_SIZE);
		
		
		assertTrue(inSampleSize % 2 == 0);
	}
	
	public void testCalculateInSampleSizeWithMaxSizeLessThanPowerOfTwoGrThanDestSize() {
		int inSampleSize = BitmapUtils.calculateInSampleSize(1500, 780, DEST_SIZE);
		
		assertTrue(inSampleSize == 1);
	}
	
	public void testCalculateInSampleSizeWithMaxSizeLessThanDestSize() {
		int inSampleSize = BitmapUtils.calculateInSampleSize(640, 380, DEST_SIZE);
		
		assertTrue(inSampleSize == 1);
	}
	
	public void testGetRelativeScaledImg() {
		Bitmap result = BitmapUtils.getRelativeScaledImg(IMAGE_PATH, DEST_SIZE);

		int maxSize = Math.max(result.getWidth(), result.getHeight());
		assertTrue(maxSize < 2*DEST_SIZE);
		assertTrue(maxSize >= DEST_SIZE);
		
		result.recycle();
		result = null;
	}
	
	public void testGetAbsoluteScaledImg() {
		Bitmap image = BitmapUtils.getRelativeScaledImg(IMAGE_PATH, DEST_SIZE);
		assertTrue(image != null);
		
		image = BitmapUtils.getAbsoluteScaledImg(getContext(), image, DEST_SIZE, IMAGE_PATH);
		assertTrue(image != null);
		assertTrue(image.getWidth() <= DEST_SIZE);
		assertTrue(image.getHeight() <= DEST_SIZE);
	}
}
