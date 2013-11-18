package com.personal.rents.rest.clients;

import com.personal.rents.model.Address;
import com.personal.rents.rest.GeocodeRESTClient;

import android.test.AndroidTestCase;

public class GecodingRESTClientTest extends AndroidTestCase {
	
	private double latitude  = 46.7667;
	
	private double longitude = 23.5833;
	
	private String queryAddress = "15 Strada Regele Ferdinand,Cluj-Napoca,Cluj,Romania";

	public void testGetAddressFromLocation() {
		Address address = GeocodeRESTClient.getAddressFromLocation(latitude, longitude);
		
		assertNotNull(address);
	}
	
	public void testGetAddressFromAddress() {
		//Address address = GeocodingRESTClient.getAddressFromAddress(queryAddress);
		
		Address address = GeocodeRESTClient.getAddressFromAddress(queryAddress);
		
		assertNotNull(address);
	}

}
