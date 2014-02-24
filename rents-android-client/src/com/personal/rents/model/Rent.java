package com.personal.rents.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Rent implements Parcelable {
	
	public int rentId;
	
	public int accountId;
	
	public Address address;
	
	public int rentPrice;
	
	public int rentSurface;
	
	public short rentRooms;
	
	public short rentBaths;
	
	public byte rentParty;
	
	public byte rentType;
	
	public byte rentArchitecture;
	
	public int rentAge;
	
	public String rentDescription;
	
	public boolean rentPetsAllowed;
	
	public String rentPhone;
	
	public Date rentAddDate;
	
	public byte rentStatus;
	
	public List<String> rentImageURIs;
	
	public static final Parcelable.Creator<Rent> CREATOR;
    
    static {
    	CREATOR = new Parcelable.Creator<Rent>() {
    	    public Rent createFromParcel(Parcel source) {
    	        return new Rent(source);
    	    }

    	    public Rent[] newArray(int size) {
    	        return new Rent[size];
    	    }
    	};
    }

	public Rent() {
	}
	
	public Rent(Parcel source) {
		rentId = source.readInt();
		accountId = source.readInt();
		address = source.readParcelable(Address.class.getClassLoader());
		rentPrice = source.readInt();
		rentSurface = source.readInt();
		rentRooms = (short) source.readInt();
		rentBaths = (short) source.readInt();
		rentParty = source.readByte();
		rentType = source.readByte();
		rentArchitecture = source.readByte();
		rentAge = source.readInt();
		rentDescription = source.readString();
		rentPetsAllowed = (Boolean) source.readValue(Boolean.class.getClassLoader());
		rentPhone = source.readString();
		rentAddDate = new Date(source.readLong());
		rentStatus = source.readByte();
		rentImageURIs = new ArrayList<String>();
		source.readStringList(rentImageURIs);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(rentId);
		dest.writeInt(accountId);
		dest.writeParcelable(address, flags);
		dest.writeInt(rentPrice);
		dest.writeInt(rentSurface);
		dest.writeInt(rentRooms);
		dest.writeInt(rentBaths);
		dest.writeByte(rentParty);
		dest.writeByte(rentType);
		dest.writeByte(rentArchitecture);
		dest.writeInt(rentAge);
		dest.writeString(rentDescription);
		dest.writeValue(rentPetsAllowed);
		dest.writeString(rentPhone);
		dest.writeLong(rentAddDate.getTime());
		dest.writeByte(rentStatus);
		dest.writeStringList(rentImageURIs);
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}
}
