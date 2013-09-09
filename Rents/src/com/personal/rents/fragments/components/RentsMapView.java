package com.personal.rents.fragments.components;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class RentsMapView extends FrameLayout {

	public interface OnMapTouchListener {
		public void onMapTouch(boolean touched);
	}
	
	private OnMapTouchListener onMapTouchListener;

	public RentsMapView(Context context) {
		super(context);
	}

	public void setOnMapTouchListener(OnMapTouchListener onMapTouchListener) {
		this.onMapTouchListener = onMapTouchListener;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean touched = (ev.getAction() != MotionEvent.ACTION_UP);
		onMapTouchListener.onMapTouch(touched);

		return super.dispatchTouchEvent(ev);
	}
}
