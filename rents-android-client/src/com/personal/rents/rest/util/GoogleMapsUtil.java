package com.personal.rents.rest.util;

import java.util.Locale;

import android.content.Intent;
import android.net.Uri;

import com.personal.rents.model.Address;

public class GoogleMapsUtil {
	
	private static final String GOOGLE_MAPS_DIRECTIONS_URI_FORMAT = 
			"http://maps.google.com/maps?daddr=%f,%f";

	public static Intent getDirectionsIntent(Address address) {
		String uri = String.format(Locale.getDefault(), GOOGLE_MAPS_DIRECTIONS_URI_FORMAT,
				address.addressLatitude, address.addressLongitude);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));

		return intent;
	}
}
