package com.personal.rents.rest.util;

import com.personal.rents.model.Address;
import com.personal.rents.model.Geometry.Coordinate;
import com.personal.rents.model.PlaceDetails;

public final class PlacesParser {

	private PlacesParser(){
	}

	
	public static Address parsePlaceDetails(PlaceDetails.Result result) {
		Address address = new Address();
		Coordinate location = result.geometry.location;
		address.addressLatitude = location.lat;
		address.addressLongitude = location.lng;
		
		return address;
	}
}
