package com.personal.rents.rest.util;

import com.personal.rents.model.Address;
import com.personal.rents.model.Geocode;
import com.personal.rents.model.Geocode.AddressComponent;
import com.personal.rents.model.Geocode.Coordinate;

public final class GeocodingParser {
	
	private static final String STREET_NO_TYPE = "street_number";
	
	private static final String STREET_NAME_TYPE = "route";
	
	private static final String NEIGHBORHOOD_TYPE = "neighborhood";
	
	private static final String LOCALITY_TYPE = "locality";
	
	private static final String ADM_AREA_1_TYPE = "administrative_area_level_1";
	
	private static final String COUNTRY_TYPE = "country";
	

	private GeocodingParser() {
	}

	public static Address parseAddressResult(Geocode.Result result, String logTag) {
		Address address = new Address();
		String addrCompType = null;
		for(AddressComponent addrComp : result.address_components) {
			addrCompType = addrComp.types[0];
			if(addrCompType.equals(STREET_NO_TYPE)) {
				address.streetNumber = addrComp.long_name;
			} 
			else if(addrCompType.equals(STREET_NAME_TYPE)) {
				address.streetName = addrComp.long_name;
			} 
			else if(addrCompType.equals(NEIGHBORHOOD_TYPE)) {
				address.neighborhood = addrComp.long_name;
			} 
			else if(addrCompType.equals(LOCALITY_TYPE)) {
				address.locality = addrComp.long_name;
			} 
			else if(addrCompType.equals(ADM_AREA_1_TYPE)) {
				address.adminArea1 = addrComp.long_name;
			}
			else if(addrCompType.equals(COUNTRY_TYPE)) {
				address.country = addrComp.long_name;
			}
		}
		
		address.formattedAddress = result.formatted_address;
		
		Coordinate location = result.geometry.location;
		address.latitude = location.lat;
		address.longitude = location.lng;
		
		return address;
	}
}
