package com.personal.rents.model;

public enum RentParty {
	INDIVIDUAL((byte) 0),
	REALTOR((byte) 1);
	
	private byte party;
	
	private RentParty(byte party) {
		this.party = party;
	}
	
	public byte getParty() {
		return party;
	}
}
