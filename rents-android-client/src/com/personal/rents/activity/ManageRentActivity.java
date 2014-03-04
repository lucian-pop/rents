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
import com.personal.rents.model.RentImage;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.MediaUtils;

public abstract class ManageRentActivity extends AccountActivity {

	protected static final int DEFAULT_NO_OF_GRID_IMAGES = 6;

	protected Rent rent;
	
	protected String fromActivity;
	
	protected String selectedImagePath;
	
	protected boolean taskInProgress;
	
	protected String taskName;
	
	protected ProgressBarFragment progressBarFragment;
	
	protected ImageGridFragment imagesFragment;

	protected void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		rent = bundle.getParcelable(ActivitiesContract.RENT);
		if(rent == null) {
			rent = new Rent();
			rent.address = new Address();
			rent.address.addressFloor = Address.ADDRESS_FLOOR_DEFAULT_VALUE;
			rent.rentAddDate = new Date();
			rent.rentImages = new ArrayList<RentImage>();
		}

		fromActivity = bundle.getString(ActivitiesContract.FROM_ACTIVITY);
		selectedImagePath = bundle.getString(ActivitiesContract.SELECTED_IMAGE_PATH);
		taskInProgress = bundle.getBoolean(ActivitiesContract.TASK_IN_PROGRESS, false);
		if(taskInProgress) {
			taskName = bundle.getString(ActivitiesContract.TASK_NAME);
		}	
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveInstanceState(outState);
		
		super.onSaveInstanceState(outState);
	}
	
	protected void saveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.RENT, rent);
		outState.putString(ActivitiesContract.SELECTED_IMAGE_PATH, selectedImagePath);
		outState.putString(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		
		taskInProgress = progressBarFragment.getVisibility() == View.VISIBLE;
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, taskInProgress);
		if(taskInProgress) {
			taskName = progressBarFragment.getTask().getClass().getSimpleName();
			outState.putString(ActivitiesContract.TASK_NAME, taskName);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
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
			if(rent.rentImages.size() > imagesFragment.getSelectedPicPosition()
					&& rent.rentImages.get(imagesFragment.getSelectedPicPosition()) != null){
				deleteRentImage();
			}
			
			return true;
		}
		
        return super.onContextItemSelected(item);
	}
	
	protected void init() {
		setupActionBar();
		
		setupLocateRentBtn();
		
		setupSpinners();
		
		setupImagesFragment();
		
		setupProgressBarFragment();
	}

	protected void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	protected void setupLocateRentBtn() {
		TextView locateRentBtn = (TextView) findViewById(R.id.locate_rent_btn);
		if(rent.address.addressLatitude != 0 && rent.address.addressLongitude != 0) {
			locateRentBtn.setText(rent.address.toString());
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
		imagesFragment.setDefaultSize(DEFAULT_NO_OF_GRID_IMAGES);
		imagesFragment.setImageURIs(rent.rentImages);
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
					rent.address = intent.getParcelableExtra(ActivitiesContract.ADDRESS);
					setupLocateRentBtn();
					showAddress();
					
					break;
				default:
					selectedImagePath = null;

					break;
				}
			
			if(selectedImagePath != null) {
				if(rent.rentImages.size() > imagesFragment.getSelectedPicPosition()) {
					replaceRentImage();
				} else {
					addRentImage();
				}
			}
		}
	}
	
	protected void replaceRentImage() {
		rent.rentImages.get(imagesFragment.getSelectedPicPosition()).rentImageURI =
				selectedImagePath;

		imagesFragment.notifyDataSetChanged();
	}
	
	protected void addRentImage() {
		RentImage rentImage = new RentImage();
		rentImage.rentId = rent.rentId;
		rentImage.rentImageURI = selectedImagePath;
		rent.rentImages.add(rentImage);

		imagesFragment.notifyDataSetChanged();
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

	protected void deleteRentImage() {
		rent.rentImages.remove(imagesFragment.getSelectedPicPosition());
		imagesFragment.notifyDataSetChanged();
	}

	public void onLocateRentBtnClick(View view) {
		Intent intent = new Intent(this, AddLocationActivity.class);
		intent.putExtra(ActivitiesContract.ADDRESS, rent.address);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, this.getClass().getSimpleName());

		startActivityForResult(intent, ActivitiesContract.ADD_LOCATION_REQ_CODE);
	}
	
	public void onConfirmBtnClick(View view) {
		updateAddressValues();
		updateRentValues();
	}
	
	protected void showAddress() {
		EditText building = (EditText) findViewById(R.id.building);
		if(rent.address.addressBuilding != "") {
			building.setText(rent.address.addressBuilding);
		}
		
		EditText staircase = (EditText) findViewById(R.id.staircase);
		if(rent.address.addressStaircase != "") {
			staircase.setText(rent.address.addressStaircase);
		}

		EditText floor = (EditText) findViewById(R.id.floor);
		if(rent.address.addressFloor > Address.ADDRESS_FLOOR_DEFAULT_VALUE) {
			floor.setText(Integer.toString(rent.address.addressFloor));
		} else {
			floor.setText("");
		}

		EditText ap = (EditText) findViewById(R.id.ap);
		if(rent.address.addressAp != "") {
			ap.setText(rent.address.addressAp);
		}
	}
	
	protected void updateAddressValues() {
		EditText building = (EditText) findViewById(R.id.building);
		rent.address.addressBuilding = building.getText().toString().trim();

		EditText staircase = (EditText) findViewById(R.id.staircase);
		rent.address.addressStaircase = staircase.getText().toString().trim();

		String addressFloorFieldValue = ((EditText) findViewById(R.id.floor))
				.getText().toString().trim();
		rent.address.addressFloor = addressFloorFieldValue.equals("") ? 
					Address.ADDRESS_FLOOR_DEFAULT_VALUE : Integer.parseInt(addressFloorFieldValue);

		EditText ap = (EditText) findViewById(R.id.ap);
		rent.address.addressAp = ap.getText().toString().trim();
		
		rent.address.addressCountry = getString(R.string.country);
	}
	
	protected void updateRentValues() {
		// provide a mechanism to validate required fields.
		String rentPriceFieldValue = 
				((EditText) findViewById(R.id.rent_price)).getText().toString().trim();
		if(!rentPriceFieldValue.equals("")) {
			rent.rentPrice = Integer.parseInt(rentPriceFieldValue);
		}
		
		String rentSurfaceFieldValue = ((EditText) findViewById(R.id.rent_surface))
				.getText().toString().trim();
		if(!rentSurfaceFieldValue.equals("")) {
			rent.rentSurface = Integer.parseInt(rentSurfaceFieldValue);
		}

		String rentRoomsFieldValue = ((EditText) findViewById(R.id.rent_rooms))
				.getText().toString().trim();
		if(!rentRoomsFieldValue.equals("")) {
			rent.rentRooms = Short.parseShort(rentRoomsFieldValue);
		}
		
		String rentBathsFieldValue = ((EditText) findViewById(R.id.rent_baths))
				.getText().toString().trim();
		if(!rentBathsFieldValue.equals("")) {
			rent.rentBaths = Short.parseShort(rentBathsFieldValue);
		}
		
		Spinner rentParties = (Spinner) findViewById(R.id.rent_party);
		rent.rentParty = (byte) (rentParties.getSelectedItemPosition() - 1);
		
		Spinner rentTypes = (Spinner) findViewById(R.id.rent_type);
		rent.rentType = (byte) (rentTypes.getSelectedItemPosition() - 1);
		
		Spinner rentStruct = (Spinner) findViewById(R.id.rent_structure);
		rent.rentArchitecture = (byte) (rentStruct.getSelectedItemPosition() - 1);
		
		Spinner rentAge = (Spinner) findViewById(R.id.rent_age);
		rent.rentAge = rentAge.getSelectedItemPosition() - 1;

		EditText rentDesc = (EditText) findViewById(R.id.rent_desc);
		rent.rentDescription = rentDesc.getText().toString().trim();
		
		CheckBox petsAllowed = (CheckBox) findViewById(R.id.pets_allowed);
		rent.rentPetsAllowed = petsAllowed.isChecked();
		
		EditText phone = (EditText) findViewById(R.id.rent_phone);
		rent.rentPhone = phone.getText().toString().trim();

		rent.rentAddDate = new Date();
	}
	
	protected void redirectToUserAddedRents() {
		Intent intent = new Intent(this, UserAddedRentsActivity.class);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		startActivity(intent);
	}
}
