package com.personal.rents.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RentSearch implements Parcelable {

	public Rent lowRent;
	
	public Rent highRent;
	
	public int pageSize;
	
	public byte sortBy;
	
	public static final Parcelable.Creator<RentSearch> CREATOR;
    
    static {
    	CREATOR = new Parcelable.Creator<RentSearch>() {
    	    public RentSearch createFromParcel(Parcel source) {
    	        return new RentSearch(source);
    	    }

    	    public RentSearch[] newArray(int size) {
    	        return new RentSearch[size];
    	    }
    	};
    }

    public RentSearch() {
	}
	
	public RentSearch(Parcel source) {
		lowRent = source.readParcelable(Rent.class.getClassLoader());
		highRent = source.readParcelable(Rent.class.getClassLoader());
		pageSize = source.readInt();
		sortBy = (byte) source.readInt();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(lowRent, flags);
		dest.writeParcelable(highRent, flags);
		dest.writeInt(pageSize);
		dest.writeInt(sortBy);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}
}
