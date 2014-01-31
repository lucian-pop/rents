package com.personal.rents.rest.client;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.personal.rents.rest.util.BitmapImageCache;

public final class RentsImageClient {
	
	private static RequestQueue requestQueue;
	
	private static ImageLoader imageLoader;
	
	private static BitmapImageCache imageCache;

	private RentsImageClient() {
	}
	
	public static BitmapImageCache getImageCache() {
		if(imageCache == null) {
			imageCache = new BitmapImageCache();
		}
		
		return imageCache;
	}
	
	public static RequestQueue getRequestQueue(Context context) {
		if(requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context);
		}
		
		return requestQueue;
	}
	
	public static ImageLoader getImageLoader(Context context) {
		if(imageLoader == null) {
			imageLoader = new ImageLoader(getRequestQueue(context), getImageCache());
		}

		return imageLoader;
	}
}
