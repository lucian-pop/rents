package com.personal.rents.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.personal.rents.fragments.helpers.TouchableRentsMapWrapper;

public class RentsMapFragment extends SupportMapFragment {
	
	private View originalMapView;
	
	private TouchableRentsMapWrapper touchableMapView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		originalMapView = super.onCreateView(inflater, container, savedInstanceState);
		touchableMapView = new TouchableRentsMapWrapper(getActivity());
		touchableMapView.addView(originalMapView);
		
		return touchableMapView;
	}

	@Override
	public View getView() {
		return originalMapView;
	}
}
