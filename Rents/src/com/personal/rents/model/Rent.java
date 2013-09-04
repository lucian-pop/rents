package com.personal.rents.model;

import com.google.android.gms.maps.model.LatLng;

public class Rent {

	public LatLng position;
	
	public int price;

	public Rent(LatLng position, int price) {
		this.position = position;
		this.price = price;
	}
}
