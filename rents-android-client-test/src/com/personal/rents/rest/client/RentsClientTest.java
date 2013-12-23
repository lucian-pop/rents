package com.personal.rents.rest.client;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.personal.rents.model.Rent;

public class RentsClientTest extends AndroidTestCase {

	private static final double MIN_LATITUDE = 46.7379424563698;

	private static final double MAX_LATITUDE = 46.76499396368981;

	private static final double MIN_LONGITUDE = 23.56791313737631;
	
	private static final double MAX_LONGITUDE = 23.59537862241268;
	
	public void testGetLightRentsByMapBoundaries() {
		List<Rent> rents = RentsClient.getRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE,
				MIN_LONGITUDE, MAX_LONGITUDE);
		
		Log.e("SMART_TAG", "No. of rents is " + rents.size());
		
		assertTrue(rents.size() > 0);
	}

}
