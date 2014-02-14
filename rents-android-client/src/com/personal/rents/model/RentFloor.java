package com.personal.rents.model;

public enum RentFloor {

	BASEMENT("Subsol"),
	GROUND("Parter");
	
	private String floor;
	
	private RentFloor(String floor) {
		this.floor = floor;
	}
	
	public String getFloor() {
		return floor;
	}
}
