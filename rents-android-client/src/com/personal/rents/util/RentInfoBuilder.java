package com.personal.rents.util;

import com.personal.rents.R;

import android.content.Context;

public final class RentInfoBuilder {

	private static String[] rentTypes;
	
	private static String[] rentAges;

	public static String buildRentTypeDesc(Context context, int rentType, int rentAge) {
		initResources(context);
		
		return rentTypes[rentType] + ", " + rentAges[rentAge];
	}
	
	private static void initResources(Context context) {
		if(rentTypes == null) {
			rentTypes = context.getResources().getStringArray(R.array.rent_types);
		}
		
		if(rentAges == null) {
			rentAges = context.getResources().getStringArray(R.array.rent_ages);
		}
	}
}
