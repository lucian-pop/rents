package com.personal.rents.util;

import com.personal.rents.model.Address;
import com.personal.rents.model.RentFloor;

public final class AddressBuilder {
	
	public static final String STREET_NO_TAG = "Nr. ";
	
	public static final String BUILDING_TAG = "Bl. ";
	
	public static final String STAIRCASE_TAG = "Sc. ";
	
	public static final String FLOOR_TAG = "Etj. ";
	
	public static final String APARTMENT_TAG = "Ap. ";
	
	public static final String NEIGHBOURHOOD_TAG = "Cart. ";

	private AddressBuilder(){
	}

	public static final String buildAddressString(final Address address) {
		StringBuilder addressBuilder = new StringBuilder();
		if(address.addressStreetName != null) {
			addressBuilder.append(address.addressStreetName + GeneralConstants.SPACE);
		}
		
		if(address.addressStreetNo != null) {
			addressBuilder.append(STREET_NO_TAG + address.addressStreetNo);
		}
		
		if(address.addressBuilding != null) {
			addressBuilder.append(GeneralConstants.COMMA + BUILDING_TAG + address.addressBuilding);
		}
		
		if(address.addressStaircase != null) {
			addressBuilder.append(GeneralConstants.COMMA + STAIRCASE_TAG + address.addressStaircase);
		}
		
		if(address.addressFloor > GeneralConstants.UNSPECIFIED_FLOOR_VALUE) {
			switch (address.addressFloor) {
			case -1:
				addressBuilder.append(GeneralConstants.COMMA + FLOOR_TAG 
						+ RentFloor.BASEMENT.getFloor());
				
				break;
			case 0:
				addressBuilder.append(GeneralConstants.COMMA + FLOOR_TAG 
						+ RentFloor.GROUND.getFloor());

				break;
			default:
				addressBuilder.append(GeneralConstants.COMMA + FLOOR_TAG + address.addressFloor);

				break;
			}
		}
		
		if(address.addressAp != null) {
			addressBuilder.append(GeneralConstants.COMMA + APARTMENT_TAG + address.addressAp);
		}
		
		if(address.addressNeighbourhood != null) {
			addressBuilder.append(GeneralConstants.COMMA + NEIGHBOURHOOD_TAG 
					+ address.addressNeighbourhood);
		}
		
		if(address.addressSublocality != null) {
			addressBuilder.append(GeneralConstants.COMMA + address.addressSublocality);
		}
		
		return addressBuilder.toString();
	}

}
