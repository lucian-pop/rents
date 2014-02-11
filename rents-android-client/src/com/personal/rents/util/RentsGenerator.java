package com.personal.rents.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.GeocodeClient;

public final class RentsGenerator {
	
	private static final int RENTS_SIZE = 10;
	
	// Constants used for RENT
	private static final int MIN_PRICE = 100;
	
	private static final int MAX_PRICE = 20000;
	
	private static final int MIN_SURFACE = 20;
	
	private static final int MAX_SURFACE = 500;
	
	private static final String DEFAULT_DESC = "Apartamentul prezinta o pozitie favorabila"
			+ " accesului mijloacelor de transport in comun";
	
	private static final String DEFAULT_PHONE = "0741060776";
	
	private static final int DEFAULT_STATUS = 0;
	
	private static final int DEFAULT_IMAGE_URIS_SIZE = 1;
	
	private static final String DEFAULT_IMAGE_URI = "/storage/sdcard0/Bluetooth/rent_image_800.jpg";
	
	// Constants used for ADDRESS
	private static final String DEFAULT_STREET_NAME = "Aleea fericirii";
	
	private static final String DEFAULT_LOCALITY = "Cluj-Napoca";
	
	private static final String DEFAULT_ADM_AREA = "Cluj";
	
	private static final String DEFAULT_COUNTRY = "Romania";
	
	public static List<LatLng> generatePositions(VisibleRegion visibleRegion) {
		LatLng southwest = visibleRegion.latLngBounds.southwest;
		LatLng northeast = visibleRegion.latLngBounds.northeast;
		double x0 = southwest.longitude;
		double y0 = southwest.latitude;
		double x1 = northeast.longitude;
		double y1 = northeast.latitude;
		
		double temp = x0;
		x0 = Math.min(x0, x1);
		x1 = Math.max(temp, x1);
		temp = y0;
		y0 = Math.min(y0, y1);
		y1 = Math.max(temp, y1);
		Log.e("SMART_TAG", "********Min latitude: " + y0);
		Log.e("SMART_TAG", "********Max latitude: " + y1);
		Log.e("SMART_TAG", "********Min longitude: " + x0);
		Log.e("SMART_TAG", "********Max Longitude: " + x1);
		
		double x = 0;
		double y = 0;
		Random random = new Random();
		List<LatLng> positions = new ArrayList<LatLng>(RENTS_SIZE);
		LatLng position = null;
		for(int i = 0; i < RENTS_SIZE; i++) {
			x = x0 + (x1 - x0) * random.nextDouble();
			y = y0 + (y1 - y0) * random.nextDouble();
			
			position = new LatLng(y, x);
			positions.add(position);
		}
		
		return positions;
	}
	
	public static List<Rent> generateRents(List<LatLng> positions, Context context) {
		List<Rent> rents = new ArrayList<Rent>(positions.size());
		for(LatLng position : positions) {
			rents.add(createRent(position, context));
		}

		return rents;
	}
	
	private static Rent createRent(LatLng position, Context context) {
		List<String> imageURIs = new ArrayList<String>(DEFAULT_IMAGE_URIS_SIZE);
		imageURIs.add(DEFAULT_IMAGE_URI);
		Random random = new Random();

		Rent rent = new Rent();
		rent.accountId = UserAccountManager.getAccount(context).accountId;
		rent.address = createAddress(position);
		rent.rentPrice = MIN_PRICE + random.nextInt(MAX_PRICE - MIN_PRICE);
		rent.rentSurface = MIN_SURFACE + random.nextInt(MAX_SURFACE - MIN_SURFACE);
		rent.rentRooms = (short) (1 + random.nextInt(10));
		rent.rentBaths = (short)( 1 + random.nextInt(rent.rentRooms));
		rent.rentParty = (byte) random.nextInt(2);
		rent.rentType = (byte) random.nextInt(3);
		rent.rentArchitecture = (byte) random.nextInt(2);
		rent.rentAge = random.nextInt(2);
		rent.rentDescription = DEFAULT_DESC;
		rent.rentPetsAllowed = random.nextBoolean();
		rent.rentPhone = DEFAULT_PHONE;
		rent.rentAddDate = new Date();
		rent.rentStatus = DEFAULT_STATUS;
		rent.rentImageURIs = imageURIs;
		
		return rent;
	}
	
	private static Address createAddress(LatLng position) {
		Address address = GeocodeClient.getAddressFromLocation(position.latitude, position.longitude);
		Random random = new Random();
		
		if(address.addressStreetNo == null) {
			address.addressStreetNo = Integer.toString(random.nextInt(100));
		}
		
		if(address.addressStreetName == null) {
			address.addressStreetName = DEFAULT_STREET_NAME;
		}
		
		if(address.addressLocality == null) {
			address.addressLocality = DEFAULT_LOCALITY;
		}
		
		if(address.addressAdmAreaL1 == null) {
			address.addressAdmAreaL1 = DEFAULT_ADM_AREA;
		}
		
		if(address.addressCountry == null) {
			address.addressCountry = DEFAULT_COUNTRY;
		}

		return address; 
	}

}
