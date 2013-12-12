package com.personal.rents.model;

import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Rent implements Parcelable {
	
	public int id;
	
	public int accountId;
	
	public Address address;
	
	public int price;
	
	public int surface;
	
	public int rooms;
	
	public int baths;
	
	public byte party;
	
	public byte rentType;
	
	public byte architecture;
	
	public int age;
	
	public String description;
	
	public boolean petsAllowed;
	
	public String phone;
	
	public Date creationDate;
	
	public byte rentStatus;
	
	public List<String> imageURIs;
	
	public transient int uploadStatus;
	
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
		id = source.readInt();
		accountId = source.readInt();
		address = source.readParcelable(Address.class.getClassLoader());
		price = source.readInt();
		surface = source.readInt();
		rooms = source.readInt();
		baths = source.readInt();
		party = source.readByte();
		rentType = source.readByte();
		architecture = source.readByte();
		age = source.readInt();
		description = source.readString();
		petsAllowed = (Boolean) source.readValue(Boolean.class.getClassLoader());
		phone = source.readString();
		creationDate = new Date(source.readLong());
		rentStatus = source.readByte();
		source.readStringList(imageURIs);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(accountId);
		dest.writeParcelable(address, flags);
		dest.writeInt(price);
		dest.writeInt(surface);
		dest.writeInt(rooms);
		dest.writeInt(baths);
		dest.writeByte(party);
		dest.writeByte(rentType);
		dest.writeByte(architecture);
		dest.writeInt(age);
		dest.writeString(description);
		dest.writeValue(petsAllowed);
		dest.writeString(phone);
		dest.writeLong(creationDate.getTime());
		dest.writeByte(rentStatus);
		dest.writeStringList(imageURIs);
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}
	
	public void addImageURI(String imageURI) {
		imageURIs.add(imageURI);
	}

}
