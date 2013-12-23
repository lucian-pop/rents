package com.personal.rents.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

	public int addressId;
	
	public String addressStreetNo;
	
	public String addressStreetName;
	
	public String addressNeighbourhood;
	
	public String addressSublocality;
	
	public String addressLocality;
	
	public String addressAdmAreaL1;
	
	public String addressCountry;
	
	public double addressLatitude;
	
	public double addressLongitude;
	
	public String addressBuilding;
	
	public String addressStaircase;
	
	public int addressFloor;
	
	public String addressAp;
	
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
    	addressId = source.readInt();
    	addressStreetNo = source.readString();
    	addressStreetName = source.readString();
    	addressNeighbourhood = source.readString();
    	addressSublocality = source.readString();
    	addressLocality = source.readString();
    	addressAdmAreaL1 = source.readString();
    	addressCountry = source.readString();
    	addressLatitude = source.readDouble();
    	addressLongitude = source.readDouble();
    	addressBuilding = source.readString();
    	addressStaircase = source.readString();
    	addressFloor = source.readInt();
    	addressAp = source.readString();
    }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(addressId);
		dest.writeString(addressStreetNo);
		dest.writeString(addressStreetName);
		dest.writeString(addressNeighbourhood);
		dest.writeString(addressSublocality);
		dest.writeString(addressLocality);
		dest.writeString(addressAdmAreaL1);
		dest.writeString(addressCountry);
		dest.writeDouble(addressLatitude);
		dest.writeDouble(addressLongitude);
		dest.writeString(addressBuilding);
		dest.writeString(addressStaircase);
		dest.writeInt(addressFloor);
		dest.writeString(addressAp);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public String toString() {
		return addressStreetName + " " + addressStreetNo + ", " + addressNeighbourhood + ", " 
				+ addressLocality + ", " + addressAdmAreaL1 + ", " + addressCountry + ", " 
				+ addressLatitude + ", " + addressLongitude;
	}
}
