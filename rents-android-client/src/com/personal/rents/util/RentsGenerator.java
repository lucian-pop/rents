package com.personal.rents.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;

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
	
	private static final List<String> IMAGE_URIS;
	
	// Constants used for ADDRESS
	private static final String DEFAULT_STREET_NAME = "Aleea fericirii";
	
	private static final String DEFAULT_LOCALITY = "Cluj-Napoca";
	
	private static final String DEFAULT_ADM_AREA = "Cluj";
	
	private static final String DEFAULT_COUNTRY = "Romania";
	
	static {
		IMAGE_URIS = new ArrayList<String>(DEFAULT_IMAGE_URIS_SIZE);
		IMAGE_URIS.add(DEFAULT_IMAGE_URI);
	}
	
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
		rent.accountId = UserAccountManager.getAccount(context).getId();
		rent.address = createAddress(position);
		rent.price = MIN_PRICE + random.nextInt(MAX_PRICE - MIN_PRICE);
		rent.surface = MIN_SURFACE + random.nextInt(MAX_SURFACE - MIN_SURFACE);
		rent.rooms = 1 + random.nextInt(10);
		rent.baths = 1 + random.nextInt(rent.rooms);
		rent.party = (byte) random.nextInt(2);
		rent.rentType = (byte) random.nextInt(3);
		rent.architecture = (byte) random.nextInt(2);
		rent.age = random.nextInt(2);
		rent.description = DEFAULT_DESC;
		rent.petsAllowed = random.nextBoolean();
		rent.phone = DEFAULT_PHONE;
		rent.creationDate = new Date();
		rent.rentStatus = DEFAULT_STATUS;
		rent.imageURIs = imageURIs;
		
		return rent;
	}
	
	private static Address createAddress(LatLng position) {
		Address address = GeocodeClient.getAddressFromLocation(position.latitude, position.longitude);
		Random random = new Random();
		
		if(address.streetNo == null) {
			address.streetNo = Integer.toString(random.nextInt(100));
		}
		
		if(address.streetName == null) {
			address.streetName = DEFAULT_STREET_NAME;
		}
		
		if(address.locality == null) {
			address.locality = DEFAULT_LOCALITY;
		}
		
		if(address.admAreaL1 == null) {
			address.admAreaL1 = DEFAULT_ADM_AREA;
		}
		
		if(address.country == null) {
			address.country = DEFAULT_COUNTRY;
		}

		return address; 
	}

}
