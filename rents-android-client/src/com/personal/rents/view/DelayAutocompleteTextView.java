package com.personal.rents.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class DelayAutocompleteTextView extends AutoCompleteTextView {

	private static final int delay = 500;
	
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			DelayAutocompleteTextView.super.performFiltering((CharSequence)msg.obj, msg.arg1);
		}
	};

	public DelayAutocompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void performFiltering(CharSequence text, int keyCode) {
		handler.removeMessages(0);
		handler.sendMessageDelayed(handler.obtainMessage(0, keyCode, 0, text), delay);
	}
}
