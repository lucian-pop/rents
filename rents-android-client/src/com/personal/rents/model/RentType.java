package com.personal.rents.model;

public enum RentType {
	
	APARTMENT((byte) 0),
	HOUSE((byte) 1),
	OFFICE((byte) 2);
	
	private byte type;
	
	private RentType(byte type) {
		this.type = type;
	}
	
	public byte getType() {
		return type;
	}
}
