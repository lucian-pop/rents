package com.personal.rents.fragment;

import com.android.volley.toolbox.NetworkImageView;
import com.personal.rents.R;
import com.personal.rents.model.RentImage;
import com.personal.rents.rest.client.RentsImageClient;
import com.personal.rents.util.ActivitiesContract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RentImageFragment extends Fragment {
	
	private RentImage image;

	public static RentImageFragment getNewInstance(RentImage image) {
		RentImageFragment rentImageFragment = new RentImageFragment();
		Bundle args = new Bundle();
		args.putParcelable(ActivitiesContract.IMAGE, image);
		rentImageFragment.setArguments(args);
		
		return rentImageFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = null;
		if(savedInstanceState == null) {
			bundle = getArguments();
		} else {
			bundle = savedInstanceState;
		}
		
		image = bundle.getParcelable(ActivitiesContract.IMAGE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rent_image_fragment_layout, container, false);
		NetworkImageView rentImageView = (NetworkImageView) rootView.findViewById(R.id.rent_img);
		if(image != null) {
			rentImageView.setImageUrl(image.rentImageURI, RentsImageClient.getImageLoader(
					getActivity().getApplicationContext()));
		}
		
		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.IMAGE, image);

		super.onSaveInstanceState(outState);
	}

}
