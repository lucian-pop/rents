package com.personal.rents.rest.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapImageCache extends LruCache<String, Bitmap>  implements ImageCache {
	
	private static final int IMAGES_CACHE_ALLOC_FACTOR = 8;
	
	public BitmapImageCache() {
		 this((int) (Runtime.getRuntime().maxMemory() / 1024) / IMAGES_CACHE_ALLOC_FACTOR);
	}

	public BitmapImageCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
    protected int sizeOf(String key, Bitmap bitmap) {
        return  bitmap == null ? 0 : (bitmap.getRowBytes() * bitmap.getHeight() / 1024);
    }

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}
	
	public void removeBitmap(String url) {
		remove(url);
	}
}
