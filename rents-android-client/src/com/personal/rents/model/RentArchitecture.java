package com.personal.rents.model;

public enum RentArchitecture {
	DETACHED((byte) 0),
	UNDETACHED((byte) 1);
	
	private byte architecture;
	
	private RentArchitecture(byte architecture) {
		this.architecture = architecture;
	}
	
	public byte getArchitecture() {
		return architecture;
	}
}
