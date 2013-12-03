package com.personal.rents.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.personal.rents.R;
import com.personal.rents.adapter.PlacesSuggestionsAdapter;
import com.personal.rents.logic.LocationManagerWrapper;
import com.personal.rents.model.Address;
import com.personal.rents.task.GetGeolocationFromAddressAsyncTask;
import com.personal.rents.task.GetGeolocationFromLocationAsyncTask;
import com.personal.rents.task.listener.OnGetGeolocationTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.view.DelayAutocompleteTextView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;

public class AddLocationActivity extends ActionBarActivity {
	
	private SupportMapFragment mapFragment;
	
	public GoogleMap map;
	
	private LocationManagerWrapper locationHelper;
	
	private OnGetGeolocationTaskFinishListener onGetGeolocationFromAddrTaskFinishListener;
	
	private OnGetGeolocationTaskFinishListener onGetGeolocationFromLocationTaskFinishListener;

	private double placeLatitude;
	
	private double placeLongitude;
	
	private Marker droppedPin;
	
	private Address address;
	
	/*Add location details panel*/
	ViewGroup addLocationDetailsPanel;
	
	EditText streetNameEditText;
	
	EditText streetNumberEditText;
	
	EditText otherDetailsEditText;
	
	EditText neighborhoohEditText;
	
	EditText localityEditText;
	
	EditText admAreaEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_location_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}
		address = bundle != null 
				? (Address) bundle.getParcelable(ActivitiesContract.ADDRESS) : null;

		init();
	}

	public void onResetBtnClick(View view) {
		address = new Address();
		populateAddLocationDetailsPanel(address);
		addLocationDetailsPanel.setVisibility(View.INVISIBLE);
		
		removeDroppedPin();
	}

	public void onSaveBtnClick(View view) {
		address.streetName = streetNameEditText.getText().toString();
		address.streetNumber = streetNumberEditText.getText().toString();
		address.otherDetails = otherDetailsEditText.getText().toString();
		address.neighborhood = neighborhoohEditText.getText().toString();
		address.locality = localityEditText.getText().toString();
		address.adminArea1 = admAreaEditText.getText().toString();
		
		// Show a toast message which specifies to fill the red input fields.
		// Turn color text of unfilled input fields in red.
		Intent intent = new Intent(this, AddRentActivity.class);
		intent.putExtra(ActivitiesContract.ADDRESS, address);
		startActivity(intent);
	}

	private void init() {
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		if(map==null) {
			map = mapFragment.getMap();
			if(map!=null) {
				setUpMap();
			}
		}
		
		// add OnGetGeolocationFromAddrTaskFinishListener
		onGetGeolocationFromAddrTaskFinishListener = new OnGetGeolocationTaskFinishListener() {
			@Override
			public void onGetGeolocationTaskFinish(Address address) {
				// If geolocation is not found (address is null), app crashes.
				placeLatitude = address.latitude;
				placeLongitude = address.longitude;
				AddLocationActivity.this.address = address;

				dropPin(new LatLng(placeLatitude, placeLongitude));

				populateAddLocationDetailsPanel(address);
			}
		};

		
		// add OnGetGeolocationFromLocationTaskFinishListener
		onGetGeolocationFromLocationTaskFinishListener = new OnGetGeolocationTaskFinishListener() {
			@Override
			public void onGetGeolocationTaskFinish(Address address) {
				AddLocationActivity.this.address = address;
				populateAddLocationDetailsPanel(address);
			}
		};
		
		setupActionBar();
		
		setupAddLocationDetailsPanel();
	}
	
	private void setUpMap() {
		View myLocationButton = mapFragment.getView().findViewById(0x2);
        myLocationButton.setBackgroundResource(R.drawable.app_my_location_btn_selector);
		
		map.setMyLocationEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng position) {
				addLocationDetailsPanel.setVisibility(View.GONE);
			}
		});
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng position) {
				dropPin(position);
				
				GetGeolocationFromLocationAsyncTask getGeolocationTask = 
						new GetGeolocationFromLocationAsyncTask(
								onGetGeolocationFromLocationTaskFinishListener);
				getGeolocationTask.execute(position.latitude, position.longitude);
			}
		});
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(addLocationDetailsPanel.getVisibility()==View.GONE) {
					addLocationDetailsPanel.setVisibility(View.VISIBLE);
				} else {
					addLocationDetailsPanel.setVisibility(View.GONE);
				}
				
				return false;
			}
		});
		
		locationHelper = new LocationManagerWrapper(getApplicationContext());
		moveToLastKnownLocation();
	}
	
	private void moveToLastKnownLocation() {
		if(address != null) {
			placeLatitude = address.latitude;
			placeLongitude = address.longitude;
			dropPin(new LatLng(placeLatitude, placeLongitude));
		} else {
			Location lastKnownLocation = locationHelper.getLastKnownLocation();
	        if(lastKnownLocation != null) {
	        	placeLatitude = lastKnownLocation.getLatitude();
	        	placeLongitude = lastKnownLocation.getLongitude();
	        	locationHelper.moveToLocation(lastKnownLocation, map);
	        }
		}
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		LayoutInflater inflater = this.getLayoutInflater();
		View autocompleteSearchView = inflater.inflate(R.layout.add_location_menu_layout, null);
		getSupportActionBar().setCustomView(autocompleteSearchView);
		
		DelayAutocompleteTextView placesAutocompleteTextView = 
				(DelayAutocompleteTextView) findViewById(R.id.autocomplete_places_input);
		final PlacesSuggestionsAdapter placesAdapter = new PlacesSuggestionsAdapter(this, 
				R.layout.places_suggestions_list_layout, placeLatitude, placeLongitude, false);
		placesAutocompleteTextView.setAdapter(placesAdapter);
		placesAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GetGeolocationFromAddressAsyncTask getPlaceLocationTask = 
						new GetGeolocationFromAddressAsyncTask(
								onGetGeolocationFromAddrTaskFinishListener);
				getPlaceLocationTask.execute(placesAdapter.getItem(position));
			}
		});
	}
	
	private void setupAddLocationDetailsPanel() {
		addLocationDetailsPanel = (ViewGroup) findViewById(R.id.add_location_details);
		streetNameEditText = (EditText) findViewById(R.id.street_name);
		streetNumberEditText = (EditText) findViewById(R.id.street_no);
		otherDetailsEditText = (EditText) findViewById(R.id.other_details);
		neighborhoohEditText = (EditText) findViewById(R.id.neighborhood);
		localityEditText = (EditText) findViewById(R.id.locality);
		admAreaEditText = (EditText) findViewById(R.id.adm_area);

		if(address != null) {
			populateAddLocationDetailsPanel(address);
			addLocationDetailsPanel.setVisibility(View.GONE);
		}
	}
	
	private void populateAddLocationDetailsPanel(Address address) {
		addLocationDetailsPanel.setVisibility(View.VISIBLE);
		streetNameEditText.setText(address.streetName);

		streetNumberEditText.setText(address.streetNumber);

		neighborhoohEditText.setText(address.neighborhood);
		localityEditText.setText(address.locality);
		admAreaEditText.setText(address.adminArea1);
	}
	
	private void dropPin(LatLng position) {
		removeDroppedPin();

		droppedPin = map.addMarker(new MarkerOptions().position(position));
		locationHelper.moveToLocation(position, map);
	}
	
	private void removeDroppedPin() {
		if(droppedPin != null) {
			droppedPin.remove();
		}
	}
}
