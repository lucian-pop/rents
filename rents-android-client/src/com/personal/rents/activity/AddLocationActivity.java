package com.personal.rents.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.personal.rents.R;
import com.personal.rents.adapter.PlacesSuggestionsAdapter;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.logic.LocationManagerWrapper;
import com.personal.rents.model.Address;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.GetGeolocationFromAddressAsyncTask;
import com.personal.rents.task.GetGeolocationFromLocationAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.LocationUtil;
import com.personal.rents.view.DelayAutocompleteTextView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

public class AddLocationActivity extends ActionBarActivity {
	
	public GoogleMap map;
	
	private LocationManagerWrapper locationHelper;

	private double placeLatitude;
	
	private double placeLongitude;
	
	private Marker droppedPin;
	
	private Address address;
	
	private int addressDetailsVisibility;
	
	private String fromActivity;
	
	private ProgressBarFragment progressBarFragment;
	
	/*Add location details panel*/
	ViewGroup addressDetailsPanel;
	
	EditText streetNameEditText;
	
	EditText streetNumberEditText;
	
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

		restoreInstanceState(bundle);

		init();
	}
	
	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		address = bundle.getParcelable(ActivitiesContract.ADDRESS);
		fromActivity = bundle.getString(ActivitiesContract.FROM_ACTIVITY);
		addressDetailsVisibility = bundle.getInt(ActivitiesContract.ADDRESS_DETAILS_VISIBILITY,
				View.GONE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.ADDRESS, address);
		outState.putString(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		
		addressDetailsVisibility = addressDetailsPanel.getVisibility();
		outState.putInt(ActivitiesContract.ADDRESS_DETAILS_VISIBILITY, addressDetailsVisibility);
		super.onSaveInstanceState(outState);
	}

	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(progressBarFragment != null) {
			progressBarFragment.reset();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		progressBarFragment = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			Intent intent;
			if(fromActivity.equals(AddRentActivity.class.getSimpleName())) {
				intent = new Intent(this, AddRentActivity.class);
			} else {
				intent = new Intent(this, EditRentActivity.class);
			}

			NavUtils.navigateUpTo(this, intent);
			
			return true;
		}
		
		return false;
	}

	public void onResetBtnClick(View view) {
		address = new Address();
		populateAddLocationDetailsPanel(address);
		addressDetailsPanel.setVisibility(View.INVISIBLE);
		
		removeDroppedPin();
	}

	public void onSaveBtnClick(View view) {
		updateAddress();

		Intent intent = new Intent(this, AddRentActivity.class);
		intent.putExtra(ActivitiesContract.ADDRESS, address);
		setResult(RESULT_OK, intent);
		
		finish();
	}
	
	private void updateAddress() {
		// Show a toast message which specifies to fill the red input fields.
		// Turn color text of unfilled input fields in red.
		address.addressStreetName = streetNameEditText.getText().toString();
		address.addressStreetNo = streetNumberEditText.getText().toString();
		address.addressNeighbourhood = neighborhoohEditText.getText().toString();
		address.addressLocality = localityEditText.getText().toString();
		address.addressAdmAreaL1 = admAreaEditText.getText().toString();
	}

	private void init() {
		setUpMap();
		
		setupActionBar();
		
		setupAddressDetailsPanel();
		
		setupProgressBarFragment();
	}
	
	private void setUpMap() {
		if(map != null) {
			return;
		}

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		View myLocationButton = mapFragment.getView().findViewById(0x2);
        myLocationButton.setBackgroundResource(R.drawable.app_my_location_btn_selector);
		
		map = mapFragment.getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng position) {
				addressDetailsPanel.setVisibility(View.GONE);
			}
		});
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng position) {
				dropPin(position);
				
				startGetGeolocationFromLocationAsyncTask(position.latitude, position.longitude);
			}
		});
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(addressDetailsPanel.getVisibility()==View.GONE) {
					addressDetailsPanel.setVisibility(View.VISIBLE);
				} else {
					addressDetailsPanel.setVisibility(View.GONE);
				}
				
				return false;
			}
		});
		
		locationHelper = new LocationManagerWrapper(this);
		moveToLastKnownLocation();
	}
	
	private void moveToLastKnownLocation() {
		if(address != null) {
			placeLatitude = address.addressLatitude;
			placeLongitude = address.addressLongitude;
			dropPin(new LatLng(placeLatitude, placeLongitude));
		} else {
			Location lastKnownLocation = locationHelper.getLastKnownLocation();
	        if(lastKnownLocation != null) {
	        	placeLatitude = lastKnownLocation.getLatitude();
	        	placeLongitude = lastKnownLocation.getLongitude();
	        	LocationUtil.moveToLocation(lastKnownLocation, map);
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
				R.layout.places_suggestions_list_layout, false);
		placesAutocompleteTextView.setAdapter(placesAdapter);
		placesAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startGetGeolocationFromAddressAsyncTask(placesAdapter.getItem(position));
			}
		});
	}
	
	private void setupAddressDetailsPanel() {
		addressDetailsPanel = (ViewGroup) findViewById(R.id.add_location_details);
		streetNameEditText = (EditText) findViewById(R.id.street_name);
		streetNumberEditText = (EditText) findViewById(R.id.street_no);
		neighborhoohEditText = (EditText) findViewById(R.id.neighbourhood);
		localityEditText = (EditText) findViewById(R.id.locality);
		admAreaEditText = (EditText) findViewById(R.id.adm_area);

		if(address != null) {
			populateAddLocationDetailsPanel(address);
		}
		
		addressDetailsPanel.setVisibility(addressDetailsVisibility);
	}
	
	protected void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	private void startGetGeolocationFromLocationAsyncTask(double latitude, double longitude) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetGeolocationFromLocationAsyncTask getGeolocationFromLocationTask =
				new GetGeolocationFromLocationAsyncTask();
		progressBarFragment.setTask(getGeolocationFromLocationTask);
		progressBarFragment.setOnTaskFinishListener(
				new OnGetGeolocationFromLocationTaskFinishListener());
		getGeolocationFromLocationTask.execute(latitude, longitude);
	}
	

	private void startGetGeolocationFromAddressAsyncTask(String address) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetGeolocationFromAddressAsyncTask getGeolocationFromAddressTask =
				new GetGeolocationFromAddressAsyncTask();
		progressBarFragment.setTask(getGeolocationFromAddressTask);
		progressBarFragment.setOnTaskFinishListener(
				new OnGetGeolocationFromAddressTaskFinishListener());
		getGeolocationFromAddressTask.execute(address);
	}
	
	private void populateAddLocationDetailsPanel(Address address) {
		addressDetailsPanel.setVisibility(View.VISIBLE);
		streetNameEditText.setText(address.addressStreetName);

		streetNumberEditText.setText(address.addressStreetNo);

		neighborhoohEditText.setText(address.addressNeighbourhood);
		localityEditText.setText(address.addressLocality);
		admAreaEditText.setText(address.addressAdmAreaL1);
	}
	
	private void dropPin(LatLng position) {
		removeDroppedPin();

		droppedPin = map.addMarker(new MarkerOptions().position(position));
		LocationUtil.moveToLocation(position, map);
		Log.e("TEST_TAG", "LATITUDE: " + position.latitude + " LONGITUDE: " + position.longitude);
	}
	
	private void removeDroppedPin() {
		if(droppedPin != null) {
			droppedPin.remove();
		}
	}
	
	private class OnGetGeolocationFromAddressTaskFinishListener
			implements OnNetworkTaskFinishListener {
		
		private static final String NO_RESULT_MSG = "Adresa specificata nu a putut fi  localizata";
		
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, AddLocationActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(AddLocationActivity.this, NO_RESULT_MSG, Toast.LENGTH_LONG).show();
				
				return;
			}
			
			address = (Address) result;
			placeLatitude = address.addressLatitude;
			placeLongitude = address.addressLongitude;
			dropPin(new LatLng(placeLatitude, placeLongitude));
			populateAddLocationDetailsPanel(address);
		}
	}
	
	private class OnGetGeolocationFromLocationTaskFinishListener 
			implements OnNetworkTaskFinishListener {

		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, AddLocationActivity.this);

				return;
			}
			
			if(result == null) {
				// show a location panel with empty fields.
				addressDetailsPanel.setVisibility(View.VISIBLE);
				
				return;
			}
			
			address = (Address) result;
			populateAddLocationDetailsPanel(address);
		}
	}
}