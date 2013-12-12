package com.personal.rents.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

	public int id;
	
	public String streetNo;
	
	public String streetName;
	
	public String neighbourhood;
	
	public String sublocality;
	
	public String locality;
	
	public String admAreaL1;
	
	public String country;
	
	public double latitude;
	
	public double longitude;
	
	public String building;
	
	public String staircase;
	
	public int floor;
	
	public String ap;
	
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
    	id = source.readInt();
    	streetNo = source.readString();
    	streetName = source.readString();
    	neighbourhood = source.readString();
    	sublocality = source.readString();
    	locality = source.readString();
    	admAreaL1 = source.readString();
    	country = source.readString();
    	latitude = source.readDouble();
    	longitude = source.readDouble();
    	building = source.readString();
    	staircase = source.readString();
    	floor = source.readInt();
    	ap = source.readString();
    }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(streetNo);
		dest.writeString(streetName);
		dest.writeString(neighbourhood);
		dest.writeString(sublocality);
		dest.writeString(locality);
		dest.writeString(admAreaL1);
		dest.writeString(country);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(building);
		dest.writeString(staircase);
		dest.writeInt(floor);
		dest.writeString(ap);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public String toString() {
		return streetName + " " + streetNo + ", " + neighbourhood + ", " + locality + ", "
				+ admAreaL1 + ", " + country + ", " + latitude + ", " + longitude;
	}
}
