package com.personal.rents.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.personal.rents.R;
import com.personal.rents.adapter.SpinnerAdapterFactory;
import com.personal.rents.fragment.ImageGridFragment;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.MediaUtils;

public abstract class ManageRentActivity extends AccountActivity {

	private static final int NO_OF_IMAGES = 6;

	protected Address address;
	
	protected String selectedImagePath;
	
	protected ArrayList<String> imagesPaths = new ArrayList<String>(NO_OF_IMAGES);
	
	protected boolean taskInProgress;
	
	protected String taskName;
	
	protected ProgressBarFragment progressBarFragment;
	
	protected ImageGridFragment imagesFragment;
	
	protected String fromActivity;

	protected void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		address = bundle.getParcelable(ActivitiesContract.ADDRESS);
		selectedImagePath = bundle.getString(ActivitiesContract.SELECTED_IMG_PATH);
		imagesPaths = bundle.getStringArrayList(ActivitiesContract.IMAGES_PATHS);
		fromActivity = bundle.getString(ActivitiesContract.FROM_ACTIVITY);
		taskInProgress = bundle.getBoolean(ActivitiesContract.TASK_IN_PROGRESS, false);
		if(taskInProgress) {
			taskName = bundle.getString(ActivitiesContract.TASK_NAME);
		}	
		
		if(imagesPaths == null) {
			imagesPaths = new ArrayList<String>(NO_OF_IMAGES);
		}
	}

	protected void init() {
		setupActionBar();
		
		setupLocateRentBtn();
		
		setupSpinners();
		
		setupImagesFragment();
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Adaugare chirie");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void setupLocateRentBtn() {
		TextView locateRentBtn = (TextView) findViewById(R.id.locate_rent_btn);
		if(address != null) {
			locateRentBtn.setText(address.toString());
		}
	}

	private void setupSpinners() {
		Spinner partiesSpinner = (Spinner) findViewById(R.id.rent_party);
		ArrayAdapter<CharSequence> spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, 
				R.array.rent_parties);
		partiesSpinner.setAdapter(spinnerAdapter);
		
		Spinner typesSpinner = (Spinner) findViewById(R.id.rent_type);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, R.array.rent_types);
		typesSpinner.setAdapter(spinnerAdapter);
		
		Spinner structSpinner = (Spinner) findViewById(R.id.rent_structure);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this,
				R.array.rent_architectures);
		structSpinner.setAdapter(spinnerAdapter);
		
		Spinner ageSpinner = (Spinner) findViewById(R.id.rent_age);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, R.array.rent_ages);
		ageSpinner.setAdapter(spinnerAdapter);
	}
	
	private void setupImagesFragment() {
		imagesFragment = 
				(ImageGridFragment) getSupportFragmentManager().findFragmentById(R.id.rent_images);
		imagesFragment.setDefaultSize(NO_OF_IMAGES);
		imagesFragment.setImageURIs(imagesPaths);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		setupProgressBarFragment();
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent intent) {
		if(resCode == RESULT_OK) {
			switch (reqCode) {
				case ActivitiesContract.BROWSE_PIC_REQ_CODE:
					Uri selectedImageUri  = intent.getData();
					selectedImageUri = intent.getData();
					selectedImagePath = MediaUtils.getImagePath(this, selectedImageUri);

					break;
				case ActivitiesContract.TAKE_PIC_REQ_CODE:
					MediaUtils.addImageToGallery(this, selectedImagePath);
					
					break;
				case ActivitiesContract.ADD_LOCATION_REQ_CODE:
					address = intent.getParcelableExtra(ActivitiesContract.ADDRESS);
					setupLocateRentBtn();
					
					break;
				default:
					selectedImagePath = null;

					break;
				}
		}
		
		if(selectedImagePath != null) {
			if(imagesPaths.size() > imagesFragment.getSelectedPicPosition()) {
				imagesPaths.set(imagesFragment.getSelectedPicPosition(), selectedImagePath);
			} else {
				imagesPaths.add(selectedImagePath);
			}

			imagesFragment.getImageAdapter().notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.ADDRESS, address);
		outState.putString(ActivitiesContract.SELECTED_IMG_PATH, selectedImagePath);
		outState.putStringArrayList(ActivitiesContract.IMAGES_PATHS, imagesPaths);
		outState.putString(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		
		taskInProgress = progressBarFragment.getVisibility() == View.VISIBLE;
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, taskInProgress);
		if(taskInProgress) {
			taskName = progressBarFragment.getTask().getClass().getSimpleName();
			outState.putString(ActivitiesContract.TASK_NAME, taskName);
		}
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// Test receiving call while adding rent.
		if(progressBarFragment != null) {
			progressBarFragment.resetTaskFinishListener();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		progressBarFragment = null;
		imagesFragment = null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.signout_menu, menu);

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		getMenuInflater().inflate(R.menu.manage_rent_image_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.browse_pic_action) {
			initiatePicBrowsing();
			
			return true;
		} else if(item.getItemId() == R.id.take_pic_action) {
			initiateTakePicture();
			
			return true;
		} else if(item.getItemId() == R.id.delete_pic_action) {
			deletePicture();
			
			return true;
		}
		
        return super.onContextItemSelected(item);
	}
	
	protected void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	private void initiatePicBrowsing() {
		Intent intent = new Intent();
		intent.setType(ActivitiesContract.IMAGE_TYPE);
		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivityForResult(Intent.createChooser(intent, 
				getResources().getString(R.string.browse_pic_action_title)),
				ActivitiesContract.BROWSE_PIC_REQ_CODE);
	}
	
	private void initiateTakePicture() {
		File imageFile = MediaUtils.createImageFile(this);
		selectedImagePath = imageFile.getAbsolutePath();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		
	    startActivityForResult(intent, ActivitiesContract.TAKE_PIC_REQ_CODE);
		
	}

	protected void deletePicture() {
		imagesPaths.remove(imagesFragment.getSelectedPicPosition());
		imagesFragment.getImageAdapter().notifyDataSetChanged();
	}
	
	public abstract void onLocateRentBtnClick(View view);
	
	public void onConfirmBtnClick(View view) {
		updateAddress();
	}
	
	private void updateAddress() {
		EditText building = (EditText) findViewById(R.id.building);
		EditText staircase = (EditText) findViewById(R.id.staircase);
		EditText floor = (EditText) findViewById(R.id.floor);
		EditText ap = (EditText) findViewById(R.id.ap);
		
		if(!building.getText().toString().equals("")) {
			address.addressBuilding = building.getText().toString();
		}
		
		if(!staircase.getText().toString().equals("")) {
			address.addressStaircase = staircase.getText().toString();
		}
		
		if(!floor.getText().toString().equals("")) {
			address.addressFloor = Integer.parseInt(floor.getText().toString());
		}
		
		if(!ap.getText().toString().equals("")) {
			address.addressAp = ap.getText().toString();
		}

		address.addressCountry = getString(R.string.country);
	}
	
	protected void updateRentValues(Rent rent) {
		EditText rentPrice = (EditText)	findViewById(R.id.rent_price);
		EditText rentSurface = (EditText) findViewById(R.id.rent_surface);
		EditText rentRooms = (EditText) findViewById(R.id.rent_rooms);
		EditText rentBaths = (EditText) findViewById(R.id.rent_baths);
		Spinner rentParties = (Spinner) findViewById(R.id.rent_party);
		Spinner rentTypes = (Spinner) findViewById(R.id.rent_type);
		Spinner rentStruct = (Spinner) findViewById(R.id.rent_structure);
		Spinner rentAge = (Spinner) findViewById(R.id.rent_age);
		EditText rentDesc = (EditText) findViewById(R.id.rent_desc);
		CheckBox  petsAllowed = (CheckBox) findViewById(R.id.pets_allowed);
		EditText phone = (EditText) findViewById(R.id.rent_phone);
		
		rent.address = address;
		rent.rentPrice = Integer.parseInt(rentPrice.getText().toString());
		rent.rentSurface = Integer.parseInt(rentSurface.getText().toString());
		rent.rentRooms = Short.parseShort(rentRooms.getText().toString());
		rent.rentBaths = Short.parseShort(rentBaths.getText().toString());
		rent.rentParty = (byte) (rentParties.getSelectedItemPosition() - 1);
		rent.rentType = (byte) (rentTypes.getSelectedItemPosition() - 1);
		rent.rentArchitecture = (byte) (rentStruct.getSelectedItemPosition() - 1);
		rent.rentAge = rentAge.getSelectedItemPosition() - 1;

		if(!rentDesc.getText().toString().equals("")) {
			rent.rentDescription = rentDesc.getText().toString();
		}

		rent.rentPetsAllowed = petsAllowed.isChecked();
		rent.rentPhone = phone.getText().toString();
		rent.rentAddDate = new Date();
		rent.rentImageURIs = new ArrayList<String>(NO_OF_IMAGES);
	}
}
