package com.personal.rents.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.personal.rents.R;
import com.personal.rents.adapter.SpinnerAdapterFactory;
import com.personal.rents.adapter.ImageArrayAdapter;
import com.personal.rents.logic.ImageManager;
import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.task.AddRentAsyncTask;
import com.personal.rents.task.AuthorizationAsyncTask;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.MediaUtils;
import com.personal.rents.view.DynamicGridView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddRentActivity extends ActionBarActivity {
	
	private static final int NO_OF_PICS = 5;

	private Address address;
	
	private int selectedPicPosition;
	
	private Bitmap selectedBitmap;
	
	private String selectedPicPath;

	private ArrayList<Bitmap> pics;
	
	private ArrayList<String> imagesPaths = new ArrayList<String>(NO_OF_PICS);
	
	private ImageArrayAdapter imageAdapter;
	
	/**
	 * Recycle selectedBitmap and pics on onDestroy().
	 * Recreate pics from paths using an AsyncTask.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rent_activity_layout);
		
		Bundle bundle = null;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
			selectedPicPosition = savedInstanceState.getInt(
					ActivitiesContract.SELECTED_PIC_POSITION);
			selectedPicPath = savedInstanceState.getString(ActivitiesContract.SELECTED_PIC_PATH);
			pics = savedInstanceState.getParcelableArrayList(ActivitiesContract.SELECTED_PICS);
			imagesPaths = savedInstanceState.getStringArrayList(ActivitiesContract.IMAGES_PATHS);
		} else {
			bundle = getIntent().getExtras();
		}

		address = bundle != null 
				? (Address) bundle.getParcelable(ActivitiesContract.ADDRESS) : null;

		init();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.ADDRESS, address);
		outState.putInt(ActivitiesContract.SELECTED_PIC_POSITION, selectedPicPosition);
		outState.putString(ActivitiesContract.SELECTED_PIC_PATH, selectedPicPath);
		outState.putParcelableArrayList(ActivitiesContract.SELECTED_PICS, imageAdapter.getImages());
		outState.putStringArrayList(ActivitiesContract.IMAGES_PATHS, imagesPaths);
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_rent_menu, menu);

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		getMenuInflater().inflate(R.menu.add_rent_pic_menu, menu);
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

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent intent) {
		Uri selectedPicUri = null;
		if(resCode == RESULT_OK) {
			switch (reqCode) {
				case ActivitiesContract.BROWSE_PIC_REQ_CODE:
					selectedPicUri = intent.getData();
					selectedPicPath = MediaUtils.getImagePath(this, selectedPicUri);

					break;
				case ActivitiesContract.TAKE_PIC_REQ_CODE:
					MediaUtils.addImageToGallery(this, selectedPicPath);
					
					break;
				default:
					selectedPicPath = null;

					break;
				}
			
			if(selectedPicPath != null) {
				if(imagesPaths.size() > selectedPicPosition) {
					imagesPaths.set(selectedPicPosition, selectedPicPath);
				} else {
					imagesPaths.add(selectedPicPath);
				}

				selectedBitmap = ImageManager.resizeImage(this, selectedPicPath, 
						imageAdapter.getImageMaxSize());
			}
			 
			if(selectedBitmap != null) {
				imageAdapter.replaceImage(selectedBitmap, selectedPicPosition);
			}
		}
	}

	public void onLocateRentBtnClick(View view) {
		Intent intent = new Intent(this, AddLocationActivity.class);
		intent.putExtra(ActivitiesContract.ADDRESS, address);

		startActivity(intent);
	}
	
	public void onAddRentBtnClick(View view) {
		Account account = UserAccountManager.getAccount(this);
		if(account == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			
			return;
		}

		AuthorizationAsyncTask authorizationTask = new AuthorizationAsyncTask();
		//authorizationTask.setOnTaskFinishListener(new OnAuthorizationTaskFinishListener());
		authorizationTask.execute(account);
	}
	
	private void init() {
		setupActionBar();
		
		setupLocateRentBtn();
		
		setupSpinners();
		
		setupPicsGridView();
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
	
	private void setupPicsGridView() {
		DynamicGridView picsGridView = (DynamicGridView) findViewById(R.id.rent_pics);
		imageAdapter = new ImageArrayAdapter(this, R.layout.add_picture_layout, pics, NO_OF_PICS);
		picsGridView.setAdapter(imageAdapter);
		
		picsGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, 
					long id) {
				selectedPicPosition = position;
				
				return false;
			}
		});
		
		registerForContextMenu(picsGridView);
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
		selectedPicPath = imageFile.getAbsolutePath();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		
	    startActivityForResult(intent, ActivitiesContract.TAKE_PIC_REQ_CODE);
		
	}

	private void deletePicture() {
		imageAdapter.replaceImage(null, selectedPicPosition);
	}
	
	private void addRent() {
		updateAddress();
		Rent rent = initRent();
		AddRentAsyncTask addRentTask = new AddRentAsyncTask(imagesPaths);
		//addRentTask.setOnTaskFinishListener(new OnAddRentTaskFinishListener());
		addRentTask.execute(rent, this.getApplicationContext());
	}
	
	private Rent initRent() {
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
		
		Rent rent = new Rent();
		rent.accountId = UserAccountManager.getAccount(this).accountId;
		rent.address = address;
		rent.rentPrice = Integer.parseInt(rentPrice.getText().toString());
		rent.rentSurface = Integer.parseInt(rentSurface.getText().toString());
		rent.rentRooms = Short.parseShort(rentRooms.getText().toString());
		rent.rentBaths = Short.parseShort(rentBaths.getText().toString());
		rent.rentParty = (byte) rentParties.getSelectedItemPosition();
		rent.rentType = (byte) rentTypes.getSelectedItemPosition();
		rent.rentArchitecture = (byte) rentStruct.getSelectedItemPosition();
		rent.rentAge = rentAge.getSelectedItemPosition();

		if(!rentDesc.getText().toString().equals("")) {
			rent.rentDescription = rentDesc.getText().toString();
		}

		rent.rentPetsAllowed = petsAllowed.isChecked();
		rent.rentPhone = phone.getText().toString();
		rent.rentAddDate = new Date();
		rent.rentImageURIs = new ArrayList<String>(NO_OF_PICS);
		
		return rent;
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
	
//	private class OnAuthorizationTaskFinishListener extends OnNetworkTaskFinishListener<Boolean> {
//		@Override
//		public void onTaskFinish(Boolean authorized, int taskId, RetrofitResponseStatus status) {
//			handleResponse(authorized, taskId, status, AddRentActivity.this);
//		}
//
//		@Override
//		protected void handleOkStatus(Boolean authorized, int taskId) {
//			if(!authorized) {
//				Intent intent = new Intent(AddRentActivity.this, LoginActivity.class);
//				startActivity(intent);
//			} else {
//				addRent();
//			}
//		}
//	}
//	
//	private class OnAddRentTaskFinishListener extends OnNetworkTaskFinishListener<Rent> {
//
//		private static final String RENT_ADDED = "Chiria a fost adaugata cu success";
//		
//		private static final String RENT_NOT_ADDED = "Chiria nu a putut fi adaugata. Va rugam"
//				+ " incercati din nou.";
//
//		@Override
//		public void onTaskFinish(Rent result, int taskId, RetrofitResponseStatus status) {
//			handleResponse(result, taskId, status, AddRentActivity.this);
//		}
//
//		@Override
//		protected void handleOkStatus(Rent result, int taskId) {
//			if(result != null) {
//				Toast.makeText(AddRentActivity.this, RENT_ADDED, Toast.LENGTH_LONG).show();
//			} else {
//				Toast.makeText(AddRentActivity.this, RENT_NOT_ADDED, Toast.LENGTH_LONG).show();
//			}
//		}	
//	}

}
