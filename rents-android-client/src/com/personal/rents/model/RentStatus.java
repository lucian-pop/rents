package com.personal.rents.model;

public enum RentStatus {
	AVAILABLE((byte) 0),
	NOT_AVAILABLE((byte) 1);
	
	private byte status;
	
	private RentStatus(byte status) {
		this.status = status;
	}
	
	public byte getStatus() {
		return status;
	}
}
