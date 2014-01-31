package com.personal.rents.model;

public class Geocode {
	
	public Result[] results;
	
	public String status;
	
	public class Result {
		public AddressComponent[] address_components;
		
		public Geometry geometry;
	}
	
	public class AddressComponent {
		public String long_name;
		
		public String short_name;
		
		public String[] types;
	}

}
