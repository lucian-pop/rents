package com.personal.rents.model;

public enum RentAge {
	NEW((short) 0),
	OLD((short) 1);
	
	private short age;
	
	private RentAge(short age) {
		this.age = age;
	}
	
	public short getAge() {
		return age;
	}
}
