package com.personal.rents.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.personal.rents.model.Rent;

public class RentView implements Parcelable {
	public Rent rent;
	
	public static final Parcelable.Creator<RentView> CREATOR;
    
    static {
    	CREATOR = new Parcelable.Creator<RentView>() {
    	    public RentView createFromParcel(Parcel source) {
    	        return new RentView(source);
    	    }

    	    public RentView[] newArray(int size) {
    	        return new RentView[size];
    	    }
    	};
    }

    public RentView() {
	}
    
	public RentView(Parcel source) {
		rent = source.readParcelable(Rent.class.getClassLoader());
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(rent, flags);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}
}
