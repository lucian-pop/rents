package com.personal.rents.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.personal.rents.fragments.components.RentsMapView;

public class RentsMapFragment extends SupportMapFragment {
	
	private RentsMapView rentsMapView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View originalMapView = super.onCreateView(inflater, container, savedInstanceState);
		rentsMapView = new RentsMapView(getActivity());
		rentsMapView.addView(originalMapView);
		
		return rentsMapView;
	}

	@Override
	public View getView() {
		return rentsMapView;
	}
}
