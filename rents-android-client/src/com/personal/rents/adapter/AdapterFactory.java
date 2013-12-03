package com.personal.rents.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

public final class AdapterFactory {

	private AdapterFactory() {
	}
	
	public static ArrayAdapter<CharSequence> createSpinnerAdapter(Context context, 
			int arrayResourceId) {
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context, 
				arrayResourceId, android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		return arrayAdapter;
	}

}
