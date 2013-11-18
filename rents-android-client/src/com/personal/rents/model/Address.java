package com.personal.rents.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

	public String streetNumber;
	
	public String streetName = "";
	
	public String neighborhood = "";

	public String locality = "";
	
	public String adminArea1 = "";

	public String country = "";
	
	public String formattedAddress = "";
	
	public String otherDetails = "";
	
	public double latitude;
	
	public double longitude;
	
	private static final String COMMA= ", ";
	
	public static final Parcelable.Creator<Address> CREATOR;
	    
    static {
    	CREATOR = new Parcelable.Creator<Address>() {
    	    public Address createFromParcel(Parcel source) {
    	        return new Address(source);
    	    }

    	    public Address[] newArray(int size) {
    	        return new Address[size];
    	    }
    	};
    }
    
    public Address(){
    }
    
    public Address(Parcel source) {
    	streetNumber = source.readString();
    	streetName = source.readString();
    	neighborhood = source.readString();
    	locality = source.readString();
    	adminArea1 = source.readString();
    	country = source.readString();
    	formattedAddress = source.readString();
    	otherDetails = source.readString();
    	latitude = source.readDouble();
    	longitude = source.readDouble();
    }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(streetNumber);
    	dest.writeString(streetName);
    	dest.writeString(neighborhood);
    	dest.writeString(locality);
    	dest.writeString(adminArea1);
    	dest.writeString(country);
    	dest.writeString(formattedAddress);
    	dest.writeString(otherDetails);
    	dest.writeDouble(latitude);
    	dest.writeDouble(longitude);
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}
	
	@Override
	public String toString() {
		return streetName + " " + streetNumber + COMMA +  neighborhood + COMMA + locality + COMMA
				+ adminArea1 + COMMA + country + COMMA + latitude + COMMA + longitude;
	}
}
