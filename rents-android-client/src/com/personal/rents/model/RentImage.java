package com.personal.rents.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RentImage implements Parcelable {

	public int rentImageId;
	
	public int rentId;
	
	public String rentImageURI;
	
	public static final Parcelable.Creator<RentImage> CREATOR;
    
    static {
    	CREATOR = new Parcelable.Creator<RentImage>() {
    	    public RentImage createFromParcel(Parcel source) {
    	        return new RentImage(source);
    	    }

    	    public RentImage[] newArray(int size) {
    	        return new RentImage[size];
    	    }
    	};
    }

    public RentImage() {
	}
	
	public RentImage(Parcel source) {
		rentImageId = source.readInt();
		rentId = source.readInt();
		rentImageURI = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(rentImageId);
		dest.writeInt(rentId);
		dest.writeString(rentImageURI);
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}
}