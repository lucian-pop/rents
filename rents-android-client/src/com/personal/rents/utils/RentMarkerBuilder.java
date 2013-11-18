package com.personal.rents.utils;

import com.personal.rents.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class RentMarkerBuilder {
	
	public static Bitmap createRentMarkerIcon(View v, int rentPrice) {
		TextView priceText = (TextView) v.findViewById(R.id.rent_marker_price);
		priceText.setText(String.valueOf(rentPrice) + GeneralConstants.SPACE + GeneralConstants.EURO);
		
		v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
		v.draw(c);
	 
	 return bitmap;
	}
}
