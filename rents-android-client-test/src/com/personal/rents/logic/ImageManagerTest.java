package com.personal.rents.logic;

import android.test.AndroidTestCase;

public class ImageManagerTest extends AndroidTestCase {
	
	private static final String IMAGE_PATH = "/storage/sdcard0/Bluetooth/20130922_135834.jpg";
	
	private static final int DEST_SIZE = 800;
	
	public void testResizeCompressImage() {
		byte[] imageBytes;
		for(int i=0; i < 10; i++) {
			imageBytes = ImageManager.resizeCompressImage(getContext(), IMAGE_PATH, DEST_SIZE);
		}
//		
//		assertTrue(imageBytes.length > 0);
	}

}
