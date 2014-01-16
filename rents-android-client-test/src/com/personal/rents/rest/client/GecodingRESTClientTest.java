package com.personal.rents.rest.client;

import com.personal.rents.model.Address;

import android.test.AndroidTestCase;

public class GecodingRESTClientTest extends AndroidTestCase {
	
	private static final double LATITUDE  = 46.7667;
	
	private static final double LONGITUDE = 23.5833;
	
	private static final String QUERY_ADDRESS =
			"15 Strada Regele Ferdinand,Cluj-Napoca,Cluj,Romania";

	public void testGetAddressFromLocation() {
		Address address = GeocodeClient.getAddressFromLocation(LATITUDE, LONGITUDE);
		
		assertNotNull(address);
	}
	
	public void testGetAddressFromAddress() {
		Address address = GeocodeClient.getAddressFromAddress(QUERY_ADDRESS);
		
		assertNotNull(address);
	}

}
