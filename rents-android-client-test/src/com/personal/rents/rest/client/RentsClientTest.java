package com.personal.rents.rest.client;

import android.test.AndroidTestCase;
import android.util.Log;

import com.personal.rents.dto.RentsCounter;

public class RentsClientTest extends AndroidTestCase {

	private static final double MIN_LATITUDE = 46.7379424563698;

	private static final double MAX_LATITUDE = 46.76499396368981;

	private static final double MIN_LONGITUDE = 23.56791313737631;
	
	private static final double MAX_LONGITUDE = 23.59537862241268;
	
	public void testGetLightRentsByMapBoundaries() {
		RentsCounter rentsCounter = RentsClient.getRentsByMapBoundaries(MIN_LATITUDE, MAX_LATITUDE,
				MIN_LONGITUDE, MAX_LONGITUDE);
		
		Log.e("SMART_TAG", "No. of rents is " + rentsCounter.rents.size());
		
		assertTrue(rentsCounter.rents.size() > 0);
	}

}
