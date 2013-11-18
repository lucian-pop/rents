package com.personal.rents.fragments;

import com.personal.rents.R;
import com.personal.rents.utils.ActivitiesContract;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RentPictureFragment extends Fragment {
	
	private ImageView rentPictureView;
	
	private Bitmap rentPicture;
	
	private String pictureId;

	public static RentPictureFragment getNewInstance(String pictureId) {
		RentPictureFragment rentPictureFragment = new RentPictureFragment();
		Bundle args = new Bundle();
		args.putString(ActivitiesContract.PICTURE_ID, pictureId);
		rentPictureFragment.setArguments(args);
		
		return rentPictureFragment;
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
		
		pictureId = bundle.getString(ActivitiesContract.PICTURE_ID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rent_picture_fragment_layout, container, false);
		rentPictureView = (ImageView) rootView.findViewById(R.id.rent_picture);
		init();
		
		return rootView;
	}
	
	private void init() {
		// This will change when integrating with the back-end.
		// Picture will be loaded from MEM cache if available.
		// If not an AsyncTask will load the picture from disk/disk-cache or network.
		rentPictureView.setImageDrawable(getActivity().getResources().getDrawable(
				R.drawable.app_rent_img));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(ActivitiesContract.PICTURE_ID, pictureId);

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(rentPicture != null) {
			rentPicture.recycle();
			rentPicture = null;
		}
	}

}
