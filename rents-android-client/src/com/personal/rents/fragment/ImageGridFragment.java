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
	
	private int selectedPicPosition;
	
	private int defaultSize;
	
	private List<String> imageURIs;
	
	private ImageAdapter imageAdapter;
	
	private int imageDestSize;

	public int getSelectedPicPosition() {
		return selectedPicPosition;
	}
	
	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public void setImageURIs(List<String> imageURIs) {
		this.imageURIs = imageURIs;
	}
	
	public ImageAdapter getImageAdapter() {
		return imageAdapter;
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
		imageAdapter = new ImageAdapter(getActivity(), R.layout.image_grid_item_layout,
				defaultSize, imageURIs, imageDestSize);
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
