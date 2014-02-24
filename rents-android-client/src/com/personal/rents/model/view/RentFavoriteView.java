package com.personal.rents.model.view;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class RentFavoriteView extends RentView implements Parcelable {

	public Date rentFavoriteAddDate;
	
	public static final Parcelable.Creator<RentFavoriteView> CREATOR;
    
    static {
    	CREATOR = new Parcelable.Creator<RentFavoriteView>() {
    	    public RentFavoriteView createFromParcel(Parcel source) {
    	        return new RentFavoriteView(source);
    	    }

    	    public RentFavoriteView[] newArray(int size) {
    	        return new RentFavoriteView[size];
    	    }
    	};
    }

    public RentFavoriteView() {
	}
    
	public RentFavoriteView(Parcel source) {
		super(source);
		rentFavoriteAddDate = new Date(source.readLong());
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeLong(rentFavoriteAddDate.getTime());
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}
}
