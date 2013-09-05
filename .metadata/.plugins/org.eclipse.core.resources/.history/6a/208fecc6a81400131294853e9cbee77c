package com.personal.rents.fragments.helpers;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableRentsMapWrapper extends FrameLayout {
	
	private PanningListener panningListener;

	public TouchableRentsMapWrapper(Context context) {
		super(context);

		try {
			panningListener = (PanningListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement " 
					+ PanningListener.class);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    	panningListener.onPanningStarted();
		        break;
		    case MotionEvent.ACTION_UP:
		    	panningListener.onPanningFinished();
		    	break;
	    }

	    return super.dispatchTouchEvent(event);
	}
	
	
	public interface PanningListener {
		public void onPanningStarted();
		public void onPanningFinished();
	}
}
