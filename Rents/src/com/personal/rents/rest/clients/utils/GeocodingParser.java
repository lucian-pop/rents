package com.personal.rents.rest.clients.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.personal.rents.model.Address;

public final class GeocodingParser {
	
	private static final String STREET_NO_TYPE = "street_number";
	
	private static final String STREET_NAME_TYPE = "route";
	
	private static final String NEIGHBORHOOD_TYPE = "neighborhood";
	
	private static final String LOCALITY_TYPE = "locality";
	
	private static final String ADM_AREA_1_TYPE = "administrative_area_level_1";
	
	private static final String COUNTRY_TYPE = "country";
	
	/*JSON parse elements*/
	private static final String RESULTS_ELEM = "results";
	
	private static final String ADDRESS_COMPONENTS_ELEM = "address_components";
	
	private static final String TYPES_ELEM = "types";
	
	private static final String LONG_NAME_ELEM = "long_name";
	
	private static final String FORMATTED_ADDRESS_ELEM = "formatted_address";
	
	private static final String GEOMETRY_ELEM = "geometry";
	
	private static final String LOCATION_ELEM = "location";
	
	private static final String LATITUDE_ELEM = "lat";
	
	private static final String LONGITUDE_ELEM = "lng";

	private GeocodingParser() {
	}

	public static Address parseAddressResult(String result, String logTag) {
		Address address = null;
		try {
			JSONArray jsonResults = new JSONObject(result).getJSONArray(RESULTS_ELEM);
			if(jsonResults.length()==0) {
				return null;
			}
			
			address = new Address();
			JSONArray addressComponents = jsonResults.getJSONObject(0)
					.getJSONArray(ADDRESS_COMPONENTS_ELEM); 
			JSONObject addrComp = null;
			String addrCompType = null;
			for(int i=0; i < addressComponents.length(); i++) {
				addrComp = addressComponents.getJSONObject(i);
				addrCompType = addrComp.getJSONArray(TYPES_ELEM).getString(0);
				if(addrCompType.equals(STREET_NO_TYPE)) {
					address.streetNumber = addrComp.getInt(LONG_NAME_ELEM);
				} 
				else if(addrCompType.equals(STREET_NAME_TYPE)) {
					address.streetName = addrComp.getString(LONG_NAME_ELEM);
				} 
				else if(addrCompType.equals(NEIGHBORHOOD_TYPE)) {
					address.neighborhood = addrComp.getString(LONG_NAME_ELEM);
				} 
				else if(addrCompType.equals(LOCALITY_TYPE)) {
					address.locality = addrComp.getString(LONG_NAME_ELEM);
				} 
				else if(addrCompType.equals(ADM_AREA_1_TYPE)) {
					address.adminArea1 = addrComp.getString(LONG_NAME_ELEM);
				}
				else if(addrCompType.equals(COUNTRY_TYPE)) {
					address.country = addrComp.getString(LONG_NAME_ELEM);
				}
			}
			
			address.formattedAddress = jsonResults.getJSONObject(0)
					.getString(FORMATTED_ADDRESS_ELEM);
			
			addrComp = jsonResults.getJSONObject(0).getJSONObject(GEOMETRY_ELEM)
					.getJSONObject(LOCATION_ELEM);
			address.latitude = addrComp.getDouble(LATITUDE_ELEM);
			address.longitude = addrComp.getDouble(LONGITUDE_ELEM);
		} catch (JSONException e) {
			 Log.e(logTag, "Cannot process JSON result", e);
		}
		
		return address;
	}
}
