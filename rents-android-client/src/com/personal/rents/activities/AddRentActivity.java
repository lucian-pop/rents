package com.personal.rents.activities;

import java.io.File;
import java.util.ArrayList;

import com.personal.rents.R;
import com.personal.rents.adapters.AdapterFactory;
import com.personal.rents.adapters.ImageArrayAdapter;
import com.personal.rents.model.Address;
import com.personal.rents.utils.ActivitiesContract;
import com.personal.rents.utils.BitmapUtils;
import com.personal.rents.utils.MediaUtils;
import com.personal.rents.views.DynamicGridView;

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
import android.widget.Spinner;
import android.widget.TextView;

public class AddRentActivity extends ActionBarActivity {
	
	private static final int NO_OF_PICS = 5;

	private Address address;

	private ArrayList<Bitmap> pics;
	
	private ImageArrayAdapter imageAdapter;
	
	private int selectedPicPosition;
	
	private Bitmap selectedBitmap;
	
	private String selectedPicPath;

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
				selectedBitmap = BitmapUtils.getRelativeScaledImg(selectedPicPath, 
						imageAdapter.getImageMaxSize());
				selectedBitmap  = BitmapUtils.getAbsoluteScaledImg(this, selectedBitmap, 
						imageAdapter.getImageMaxSize(), selectedPicPath);
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
		ArrayAdapter<CharSequence> spinnerAdapter = AdapterFactory.createSpinnerAdapter(this, 
				R.array.rent_parties);
		partiesSpinner.setAdapter(spinnerAdapter);
		
		Spinner typesSpinner = (Spinner) findViewById(R.id.rent_type);
		spinnerAdapter = AdapterFactory.createSpinnerAdapter(this, R.array.rent_types);
		typesSpinner.setAdapter(spinnerAdapter);
		
		Spinner structSpinner = (Spinner) findViewById(R.id.rent_structure);
		spinnerAdapter = AdapterFactory.createSpinnerAdapter(this, R.array.rent_structures);
		structSpinner.setAdapter(spinnerAdapter);
		
		Spinner ageSpinner = (Spinner) findViewById(R.id.rent_age);
		spinnerAdapter = AdapterFactory.createSpinnerAdapter(this, R.array.rent_ages);
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
}
