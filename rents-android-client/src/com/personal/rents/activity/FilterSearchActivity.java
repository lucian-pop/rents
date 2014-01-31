package com.personal.rents.activity;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.R;
import com.personal.rents.adapter.SpinnerAdapterFactory;
import com.personal.rents.adapter.PlacesSuggestionsAdapter;
import com.personal.rents.fragment.EnableLocationServicesDialogFragment;
import com.personal.rents.logic.UserPreferencesManager;
import com.personal.rents.model.Address;
import com.personal.rents.task.GetGeolocationFromAddressAsyncTask;
import com.personal.rents.task.listener.OnGetGeolocationTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.GeneralConstants;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FilterSearchActivity extends LocationActivity {
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private double placeLatitude;
	
	private double placeLongitude;
	
	private int fromActivity;
	
	private VisibleRegion visibleRegion;
	
	private String placeDescription;
	
	private boolean taskInProgress;
	
	private GetGeolocationFromAddressAsyncTask getPlaceLocationFromAddressTask;;
	
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
		
		Log.e("TEST_TAG", "***********Latitude: " + mapCenterLatitude + ", longitude " + mapCenterLongitude);
	}
	
	private void restoreInstanceState(Bundle bundle) {
		if (bundle != null) {
			mapCenterLatitude = bundle.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = bundle.getDouble(ActivitiesContract.LONGITUDE);
			visibleRegion = bundle.getParcelable(ActivitiesContract.VISIBLE_REGION);
			fromActivity = bundle.getInt(ActivitiesContract.FROM_ACTIVITY);
			taskInProgress = bundle.getBoolean(ActivitiesContract.TASK_IN_PROGRESS, false);
			placeDescription = bundle.getString(ActivitiesContract.PLACE_DESCRIPTION);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(taskInProgress) {
			startGetPlaceLocationFromAddressTask(placeDescription);
		}
		
		setupProgressDialogFragment();
		setupLocationServices(new LocationListenerImpl());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putDouble(ActivitiesContract.LATITUDE, mapCenterLatitude);
		outState.putDouble(ActivitiesContract.LONGITUDE, mapCenterLongitude);
		outState.putParcelable(ActivitiesContract.VISIBLE_REGION, visibleRegion);
		outState.putInt(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, taskInProgress);
		outState.putString(ActivitiesContract.PLACE_DESCRIPTION, placeDescription);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		resetGetPlaceLocationFromAddressTask();
		resetProgressDialogFragment();
		locationClient.reset();
		
		getPlaceLocationFromAddressTask = null;
		locationManager = null;
		locationClient = null;
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		progressDialogFragment = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			Intent intent;
			if(fromActivity == ActivitiesContract.RENTS_LIST_ACTIVITY){
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
		
		Spinner structSpinner = (Spinner) findViewById(R.id.rent_structure);
		spinnerAdapter = SpinnerAdapterFactory.createSpinnerAdapter(this, R.array.rent_structures);
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
	
	private void startGetPlaceLocationFromAddressTask(String placeDescription) {
		resetGetPlaceLocationFromAddressTask();
		
		taskInProgress = true;
		getPlaceLocationFromAddressTask = new GetGeolocationFromAddressAsyncTask(
				new OnGetGeolocationTaskFinishListener() {
					@Override
					public void onGetGeolocationTaskFinish(Address address) {
						taskInProgress = false;
						if(address == null) {
							return;
						}

						placeLatitude = address.addressLatitude;
						placeLongitude = address.addressLongitude;
						Toast.makeText(FilterSearchActivity.this, "Latitude: " + placeLatitude 
								+ ", Longitude: " + placeLongitude, Toast.LENGTH_LONG).show();
						
					}
				});
		getPlaceLocationFromAddressTask.execute(placeDescription);
	}
	
	private void resetGetPlaceLocationFromAddressTask() {
		if(getPlaceLocationFromAddressTask != null) {
			getPlaceLocationFromAddressTask.cancel(true);
		}
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
		
		updateCameraPosition();
	}
	
    private class LocationListenerImpl implements LocationListener{
    	@Override
		public void onLocationChanged(Location location) {
			if(location == null) {
				return;
			}
			
			placeLatitude = location.getLatitude();
			placeLongitude = location.getLongitude();
			if(progressDialogFragment.isResumed()) {
				progressDialogFragment.dismiss();
			}
		}
    }
}
