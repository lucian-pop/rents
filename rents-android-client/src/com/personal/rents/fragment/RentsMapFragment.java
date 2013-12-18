package com.personal.rents.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import com.personal.rents.view.TouchableMapView;

public class RentsMapFragment extends SupportMapFragment {
	
	private TouchableMapView rentsMapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View originalMapView = super.onCreateView(inflater, container, savedInstanceState);
		rentsMapView = new TouchableMapView(getActivity());
		rentsMapView.addView(originalMapView);
		
		return rentsMapView;
	}

	@Override
	public View getView() {
		return rentsMapView;
	}

}