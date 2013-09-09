package com.personal.rents.activities.helpers;

import com.personal.rents.R;
import com.personal.rents.utils.Constants;
import com.personal.rents.utils.Currencies;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class RentMarkerBuilder {
	
	public static Bitmap createRentMarkerIcon(View v, int rentPrice) {
		TextView priceText = (TextView) v.findViewById(R.id.rent_marker_price);
		priceText.setText(String.valueOf(rentPrice) + Constants.SPACE + Currencies.EURO);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        Bitmap bitmap = null;
        if (cacheBitmap == null) {
        	 v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	 bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        	 Canvas c = new Canvas(bitmap);
        	 v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        	 v.draw(c);
        	 
        	 return bitmap;
        }

        bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
	}
}
