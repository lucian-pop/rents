package com.personal.rents.activity;

import java.util.Date;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.adapter.SpinnerAdapterFactory;
import com.personal.rents.adapter.PlacesSuggestionsAdapter;
import com.personal.rents.fragment.EnableLocationServicesDialogFragment;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.logic.UserPreferencesManager;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentAge;
import com.personal.rents.model.RentArchitecture;
import com.personal.rents.model.RentParty;
import com.personal.rents.model.RentSearch;
import com.personal.rents.model.RentStatus;
import com.personal.rents.model.RentType;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.GetGeolocationFromAddressAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.GeneralConstants;
import com.personal.rents.util.LocationUtil;
import com.personal.rents.util.RangeMessageBuilder;
import com.personal.rents.view.DelayAutocompleteTextView;
import com.personal.rents.view.RangeSeekBarView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FilterSearchActivity extends LocationActivity {
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private double placeLatitude;
	
	private double placeLongitude;
	
	private String fromActivity;
	
	private VisibleRegion visibleRegion;
	
	private String placeDescription;
	
	private boolean taskInProgress;
	
	private int minPrice;
	
	private int maxPrice = Integer.MAX_VALUE;
	
	private int minSurface;
	
	private int maxSurface = Integer.MAX_VALUE;
	
	private ProgressBarFragment progressBarFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_search_activity_layout);
		
		Bundle bundle = null;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}

		restoreInstanceState(bundle);
		
		init();
	}
	
	private void restoreInstanceState(Bundle bundle) {
		if (bundle != null) {
			mapCenterLatitude = bundle.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = bundle.getDouble(ActivitiesContract.LONGITUDE);
			placeLatitude = bundle.getDouble(ActivitiesContract.PLACE_LATITUDE, -200);
			placeLongitude = bundle.getDouble(ActivitiesContract.PLACE_LONGITUDE, -200);
			visibleRegion = bundle.getParcelable(ActivitiesContract.VISIBLE_REGION);
			fromActivity = bundle.getString(ActivitiesContract.FROM_ACTIVITY);
			taskInProgress = bundle.getBoolean(ActivitiesContract.TASK_IN_PROGRESS, false);
			placeDescription = bundle.getString(ActivitiesContract.PLACE_DESCRIPTION);
			requestedCurrentLocation = bundle.getBoolean(
					ActivitiesContract.REQUESTED_CURRENT_LOCATION, false);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Log.e("TEST_TAG", "***********Filter search: On START called***********");
		if(taskInProgress) {
			startGetPlaceLocationFromAddressTask(placeDescription);
		}
		
		setupProgressDialogFragment();
		setupLocationManager();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TEST_TAG", "*********Filter search: On ACTIVITY RESULT called**********");
		switch (requestCode) {
			case  ActivitiesContract.LOCATION_SERVICES_REQ_CODE:
				setupLocationManager();
				locationServicesEnabled = locationManager.isLocationServicesEnabled();
				if(locationServicesEnabled) {
					requestedCurrentLocation = true;
				}

				break;
			default:
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(requestedCurrentLocation) {
			getCurrentLocation();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.e("TEST_TAG", "*********Filtersearch activity: On saved instance state called**********");

		outState.putDouble(ActivitiesContract.LATITUDE, mapCenterLatitude);
		outState.putDouble(ActivitiesContract.LONGITUDE, mapCenterLongitude);
		outState.putDouble(ActivitiesContract.PLACE_LATITUDE, placeLatitude);
		outState.putDouble(ActivitiesContract.PLACE_LONGITUDE, placeLongitude);
		outState.putParcelable(ActivitiesContract.VISIBLE_REGION, visibleRegion);
		outState.putString(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, taskInProgress);
		outState.putString(ActivitiesContract.PLACE_DESCRIPTION, placeDescription);
		outState.putBoolean(ActivitiesContract.REQUESTED_CURRENT_LOCATION, 
				requestedCurrentLocation);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		Log.e("TEST_TAG", "***********Filter search: On STOP called***********");	
		
		if(progressBarFragment !=  null) {
			progressBarFragment.reset();
		}

		resetProgressDialogFragment();
		if(locationClient != null) {
			locationClient.reset();
		}
		
		locationManager = null;
		locationClient = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.e("TEST_TAG", "***********Filter search: On DESTROYS called***********");	
		
		progressDialogFragment = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			Intent intent;
			if(fromActivity.equals(RentsListActivity.class.getSimpleName())){
				intent = new Intent(this, RentsListActivity.class);
			} else {
				intent = new Intent(this, RentsMapActivity.class);
			}

			NavUtils.navigateUpTo(this, intent);
			
			return true;
		}
		
		return false;
	}

	private void init() {
		setupActionBar();
		
		setupProgressBarFragment();
		
		setupPlacesAutocomplete();

		setupPriceChooser();
		
		setupSurfaceChooser();
		
		setupSpinners();
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Cautare chirii");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	private void setupPlacesAutocomplete() {
		final PlacesSuggestionsAdapter placesSuggestionsAdapter = new PlacesSuggestionsAdapter(
				this, R.layout.places_suggestions_list_layout, true);
		final DelayAutocompleteTextView placesAutocompleteTextView = 
				(DelayAutocompleteTextView) findViewById(R.id.autocomplete_places_input);
		placesAutocompleteTextView.setAdapter(placesSuggestionsAdapter);
		placesAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				placesAutocompleteTextView.setSelection(0);

				if(position==0) {
					placeLatitude = mapCenterLatitude;
					placeLongitude = mapCenterLongitude;

					return;
				} else if(position==1) {
					Log.e("TEST_TAG", "***********Get current location***********");
					requestedCurrentLocation = true;
					getCurrentLocation();

					return;
				}
				
				placeDescription = placesSuggestionsAdapter.getItem(position);
				startGetPlaceLocationFromAddressTask(placeDescription);
			}
		});
		placesAutocompleteTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				placesAutocompleteTextView.showDropDown();
			}
		});
	}

	private void setupPriceChooser() {
		ViewGroup priceChooserWrapper = (ViewGroup) findViewById(R.id.rent_price_chooser);
		final TextView selectedPriceRange = (TextView) findViewById(R.id.rent_price_range);
		RangeSeekBarView<Integer> priceChooser = new RangeSeekBarView<Integer>(GeneralConstants.MIN_PRICE, 
				GeneralConstants.MAX_PRICE, this);
		priceChooser.setNotifyWhileDragging(true);
		priceChooser.setOnRangeSeekBarChangeListener(
				new RangeSeekBarView.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBarView<?> bar, Integer minValue, 
					Integer maxValue) {
				selectedPriceRange.setText(RangeMessageBuilder.priceRangeMessageBuilder(minValue,
						maxValue, GeneralConstants.MIN_PRICE, GeneralConstants.MAX_PRICE));
				minPrice = minValue;
				maxPrice = maxValue;
			}
		});
		priceChooserWrapper.addView(priceChooser);
	}
	
	private void setupSurfaceChooser() {
		ViewGroup surfaceChooserWrapper = (ViewGroup) findViewById(R.id.rent_surface_chooser);
		final TextView selectedSurfaceRange = (TextView) findViewById(R.id.rent_surface_range);
		RangeSeekBarView<Integer> surfaceChooser = new RangeSeekBarView<Integer>(GeneralConstants.MIN_SURFACE, 
				GeneralConstants.MAX_SURFACE, this);
		surfaceChooser.setNotifyWhileDragging(true);
		surfaceChooser.setOnRangeSeekBarChangeListener(new RangeSeekBarView.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBarView<?> bar, Integer minValue, 
					Integer maxValue) {
				selectedSurfaceRange.setText(RangeMessageBuilder.surfaceRangeMessageBuilder(minValue,
						maxValue, GeneralConstants.MIN_SURFACE, GeneralConstants.MAX_SURFACE));
				minSurface = minValue; 
				maxSurface = maxValue;
			}
		});
		surfaceChooserWrapper.addView(surfaceChooser);
	}
	
	private void setupSpinners() {
		Spinner partiesSpinner = (Spinner) findViewById(R.id.rent_party);
		ArrayAdapter<CharSequence> spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, 
				R.array.rent_parties);
		partiesSpinner.setAdapter(spinnerAdapter);
		
		Spinner typesSpinner = (Spinner) findViewById(R.id.rent_type);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, R.array.rent_types);
		typesSpinner.setAdapter(spinnerAdapter);
		
		Spinner structSpinner = (Spinner) findViewById(R.id.rent_architecture);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this,
				R.array.rent_architectures);
		structSpinner.setAdapter(spinnerAdapter);
		
		Spinner ageSpinner = (Spinner) findViewById(R.id.rent_age);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, R.array.rent_ages);
		ageSpinner.setAdapter(spinnerAdapter);
		
		Spinner roomsSpinner = (Spinner) findViewById(R.id.rent_rooms);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, R.array.rent_rooms);
		roomsSpinner.setAdapter(spinnerAdapter);
		
		Spinner bathsSpinner = (Spinner) findViewById(R.id.rent_baths);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, R.array.rent_baths);
		bathsSpinner.setAdapter(spinnerAdapter);
	}
	
	protected void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	private void startGetPlaceLocationFromAddressTask(String placeDescription) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		taskInProgress = true;
		GetGeolocationFromAddressAsyncTask getPlaceLocationFromAddressTask
			= new GetGeolocationFromAddressAsyncTask();
		progressBarFragment.setTask(getPlaceLocationFromAddressTask);
		progressBarFragment.setOnTaskFinishListener(
				new OnGetGeolocationFromAddressTaskFinishListener());
		getPlaceLocationFromAddressTask.execute(placeDescription);
	}
	
	private void getCurrentLocation() {
		if(!locationServicesEnabled) {
			(EnableLocationServicesDialogFragment.newInstance(false, false))
					.show(getSupportFragmentManager(), ENABLE_LOCATION_SERVICES_FRAGMENT_TAG);
			
			return;
		} else if(!locationManager.isNetworkLocationServicesEnabled()) {
			if(UserPreferencesManager.getUserPreferences(this).showEnableNetworkLocationService)
			(EnableLocationServicesDialogFragment.newInstance(true, true))
				.show(getSupportFragmentManager(), ENABLE_LOCATION_SERVICES_FRAGMENT_TAG);
			
			return;
		}

		showGetLocationProgressDialog();
		setupLocationClient(new LocationListenerImpl());
		setupLocationManager();
	}
	
	public void onSearchResetBtnClick(View view) {
	}
	
	public void onSearchBtnClick(View view) {
		if(placeLatitude == -200 || placeLongitude == -200) {
			Toast.makeText(this, "Alegeti va rog o locatie", Toast.LENGTH_LONG).show();
			
			return;
		}

		RentSearch rentSearch = buildRentSearch();

		Intent intent = new Intent();
		intent.putExtra(ActivitiesContract.PLACE_LATITUDE, placeLatitude);
		intent.putExtra(ActivitiesContract.PLACE_LONGITUDE, placeLongitude);
		intent.putExtra(ActivitiesContract.RENT_SEARCH, rentSearch);
		setResult(RESULT_OK, intent);
		
		finish();
	}
	
	private RentSearch buildRentSearch() {
		RentSearch rentSearch = new RentSearch();
		Rent lowRent = new Rent();
		Rent highRent = new Rent();
		
		//setup address
		Address lowAddress = new Address();
		Address highAddress = new Address();
		lowAddress.addressLatitude = placeLatitude 
				- LocationUtil.getVisibleRegionLatitudeSize(visibleRegion) / 2;
		lowAddress.addressLongitude = placeLongitude 
				- LocationUtil.getVisibleRegionLongitudeSize(visibleRegion) / 2;
		highAddress.addressLatitude = placeLatitude 
				+ LocationUtil.getVisibleRegionLatitudeSize(visibleRegion) / 2;
		highAddress.addressLongitude = placeLongitude 
				+ LocationUtil.getVisibleRegionLongitudeSize(visibleRegion) / 2;
		lowRent.address = lowAddress;
		highRent.address = highAddress;
		
		//setup price range
		lowRent.rentPrice = minPrice;
		highRent.rentPrice = maxPrice;
		
		//setup surface range
		lowRent.rentSurface = minSurface;
		highRent.rentSurface = maxSurface;
		
		//setup party
		int partyPosition = ((Spinner) findViewById(R.id.rent_party)).getSelectedItemPosition();
		switch (partyPosition) {
			case 0:
				lowRent.rentParty = RentParty.INDIVIDUAL.getParty();
				highRent.rentParty = 1;
				
				break;
	        case 1:  
	        	lowRent.rentParty = RentParty.INDIVIDUAL.getParty();
	            highRent.rentParty = RentParty.INDIVIDUAL.getParty();
	            		
	        	break;
	        case 2:  
	        	lowRent.rentParty = RentParty.REALTOR.getParty();
	            highRent.rentParty = RentParty.REALTOR.getParty();
	            
	        	break;
	        default:
	        	break;
		}
		
		//setup type
		int typePosition = ((Spinner) findViewById(R.id.rent_type)).getSelectedItemPosition();
		switch (typePosition) {
			case 0:
				lowRent.rentType = RentType.APARTMENT.getType();
				highRent.rentType = RentType.OFFICE.getType();

				break;
			case 1:
				lowRent.rentType = RentType.APARTMENT.getType();
				highRent.rentType = RentType.APARTMENT.getType();
				
				break;
			case 2:
				lowRent.rentType = RentType.HOUSE.getType();
				highRent.rentType = RentType.HOUSE.getType();
				
				break;
			case 3:
				lowRent.rentType = RentType.OFFICE.getType();
				highRent.rentType = RentType.OFFICE.getType();
				
				break;
			default:
				break;
		}
		
		// setup architecture
		int arhitecturePosition = ((Spinner) findViewById(R.id.rent_architecture))
				.getSelectedItemPosition();
		switch (arhitecturePosition) {
			case 0:
				lowRent.rentArchitecture = RentArchitecture.DETACHED.getArchitecture();
				highRent.rentArchitecture = RentArchitecture.UNDETACHED.getArchitecture();
	
				break;
			case 1:
				lowRent.rentArchitecture = RentArchitecture.DETACHED.getArchitecture();
				highRent.rentArchitecture = RentArchitecture.DETACHED.getArchitecture();
				
				break;
			case 2:
				lowRent.rentArchitecture = RentArchitecture.UNDETACHED.getArchitecture();
				highRent.rentArchitecture = RentArchitecture.UNDETACHED.getArchitecture();
				
				break;
			default:
				break;
		}
		
		//setup age
		int agePosition = ((Spinner) findViewById(R.id.rent_age)).getSelectedItemPosition();
		switch (agePosition) {
			case 0:
				lowRent.rentAge = RentAge.NEW.getAge();
				highRent.rentAge = RentAge.OLD.getAge();
	
				break;
			case 1:
				lowRent.rentAge = RentAge.NEW.getAge();
				highRent.rentAge = RentAge.NEW.getAge();
				
				break;
			case 2:
				lowRent.rentAge = RentAge.OLD.getAge();
				highRent.rentAge = RentAge.OLD.getAge();
				
				break;
			default:
				break;
		}
		
		// setup rooms
		int roomsPosition = ((Spinner) findViewById(R.id.rent_rooms)).getSelectedItemPosition();
		if(roomsPosition == 0) {
			lowRent.rentRooms = 1;
			highRent.rentRooms = Short.MAX_VALUE;
		} else if(roomsPosition == 5) {
			lowRent.rentRooms = 5;
			highRent.rentRooms = Short.MAX_VALUE;
		} else {
			lowRent.rentRooms = (short) roomsPosition;
			highRent.rentRooms = (short) roomsPosition;
		}
		
		// setup baths
		int bathsPosition = ((Spinner) findViewById(R.id.rent_baths)).getSelectedItemPosition();
		if(bathsPosition == 0) {
			lowRent.rentBaths = 1;
			highRent.rentBaths = Short.MAX_VALUE;
		} else if(bathsPosition == 4) {
			lowRent.rentBaths = 4;
			highRent.rentBaths = Short.MAX_VALUE;
		} else {
			lowRent.rentBaths = (short) bathsPosition;
			highRent.rentBaths = (short) bathsPosition;
		}
		
		// setup pets allowed 
		boolean petsAllowed = ((CheckBox) findViewById(R.id.rent_pets_allowed)).isChecked();
		lowRent.rentPetsAllowed = petsAllowed;
		
		// setup date
		lowRent.rentAddDate = new Date();
		highRent.rentAddDate = new Date();
		
		// setup status
		lowRent.rentStatus = RentStatus.AVAILABLE.getStatus();
		
		rentSearch.lowRent = lowRent;
		rentSearch.highRent = highRent;
		rentSearch.pageSize = GeneralConstants.PAGE_SIZE;
		rentSearch.sortBy = 0;
		
		return rentSearch;
	}
	
	private class OnGetGeolocationFromAddressTaskFinishListener implements OnNetworkTaskFinishListener {

		private static final String NO_RESULT_MSG = "Adresa specificata nu a putut fi localizata";
		
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			taskInProgress = false;
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, FilterSearchActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(FilterSearchActivity.this, NO_RESULT_MSG, Toast.LENGTH_LONG).show();
				
				return;
			}
			
			Address address = (Address) result;
			placeLatitude = address.addressLatitude;
			placeLongitude = address.addressLongitude;
		}
	}
	
    private class LocationListenerImpl implements LocationListener{
    	@Override
		public void onLocationChanged(Location location) {
    		Log.e("TEST_TAG", "*************Received LOCATION UPDATES*************");
			if(location == null) {
				return;
			}
			
			placeLatitude = location.getLatitude();
			placeLongitude = location.getLongitude();

			requestedCurrentLocation = false;
			dismissGetLocationProgressDialog();
			locationClient.removeLocationUpdates();
		}
    }
}
