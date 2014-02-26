package com.personal.rents.fragment;

import java.util.List;

import com.personal.rents.R;
import com.personal.rents.adapter.ImageAdapter;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.view.DynamicGridView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class ImageGridFragment extends Fragment {
	
//	public final static String[] IMAGE_URIs = new String[] {
//		"/storage/sdcard0/Bluetooth/20130922_135834.jpg",
//         "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/"
//        		 + "A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
//         "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/"
//        		 + "Another%252520Rockaway%252520Sunset.jpg",
//         "https://lh3.googleusercontent.com/--L0Km39l5J8/URquXHGcdNI/AAAAAAAAAbs/3ZrSJNrSomQ/s1024/"
//        		 + "Antelope%252520Butte.jpg",
//         "https://lh6.googleusercontent.com/-8HO-4vIFnlw/URquZnsFgtI/AAAAAAAAAbs/WT8jViTF7vw/s1024/"
//        		 + "Antelope%252520Hallway.jpg",
//         "https://lh4.googleusercontent.com/-WIuWgVcU3Qw/URqubRVcj4I/AAAAAAAAAbs/YvbwgGjwdIQ/s1024/"
//         	+ "Antelope%252520Walls.jpg"};
	
	private int selectedPicPosition;
	
	private int imageDestSize;
	
	private List<String> imageURIs;
	
	private ImageAdapter imageAdapter;

	public int getSelectedPicPosition() {
		return selectedPicPosition;
	}

	public ImageAdapter getImageAdapter() {
		return imageAdapter;
	}

	public void setImageURIs(List<String> imageURIs) {
		this.imageURIs = imageURIs;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    return inflater.inflate(R.layout.image_grid_fragment_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getActivity().getIntent().getExtras();
		}
		
		restoreInstanceState(bundle);

		setupGridView();
	}
	
	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		selectedPicPosition = bundle.getInt(ActivitiesContract.SELECTED_IMG_POSITION);
	}
	
	private void setupGridView() {
		float imageWidth = getResources().getDimension(R.dimen.add_picture_width);
		float imageHeight = getResources().getDimension(R.dimen.add_picture_height);
		imageDestSize = (int) Math.max(imageWidth, imageHeight);
		
		final DynamicGridView imagesGridView = 
				(DynamicGridView) getActivity().findViewById(R.id.gridview);
		imageAdapter = new ImageAdapter(getActivity(), R.layout.image_grid_item_layout, imageURIs,
				imageDestSize);
		imagesGridView.setAdapter(imageAdapter);
		imagesGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
					long id) {
				selectedPicPosition = position;
				
				return false;
			}
		});
		
		registerForContextMenu(imagesGridView);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(ActivitiesContract.SELECTED_IMG_POSITION, selectedPicPosition);

		super.onSaveInstanceState(outState);
	}
}
