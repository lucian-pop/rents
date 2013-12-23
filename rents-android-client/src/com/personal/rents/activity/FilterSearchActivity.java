package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.adapter.AdapterFactory;
import com.personal.rents.adapter.PlacesSuggestionsAdapter;
import com.personal.rents.model.Address;
import com.personal.rents.task.GetGeolocationFromAddressAsyncTask;
import com.personal.rents.task.listener.OnGetGeolocationTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.util.GeneralConstants;
import com.personal.rents.util.RangeMessageBuilder;
import com.personal.rents.view.DelayAutocompleteTextView;
import com.personal.rents.view.RangeSeekBarView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class FilterSearchActivity extends ActionBarActivity {
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private double placeLatitude;
	
	private double placeLongitude;
	
	private int fromActivity;
	
	private OnGetGeolocationTaskFinishListener onGetGeolocationTaskFinishListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_search_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}

		if (bundle != null) {
			mapCenterLatitude = bundle.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = bundle.getDouble(ActivitiesContract.LONGITUDE);
			fromActivity = bundle.getInt(ActivitiesContract.FROM_ACTIVITY);
		}
		
		init();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putDouble(ActivitiesContract.LATITUDE, mapCenterLatitude);
		outState.putDouble(ActivitiesContract.LONGITUDE, mapCenterLongitude);
		outState.putInt(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		
		super.onSaveInstanceState(outState);
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
		
		// add OnGetPlaceLocationTaskFinishListener
		onGetGeolocationTaskFinishListener = new OnGetGeolocationTaskFinishListener() {
			@Override
			public void onGetGeolocationTaskFinish(Address address) {
				placeLatitude = address.addressLatitude;
				placeLongitude = address.addressLongitude;
			}
		};
		
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
		DelayAutocompleteTextView placesAutocompleteTextView = 
				(DelayAutocompleteTextView) findViewById(R.id.autocomplete_places_input);
		final PlacesSuggestionsAdapter placesAdapter = new PlacesSuggestionsAdapter(this, 
				R.layout.places_suggestions_list_layout, mapCenterLatitude, mapCenterLongitude, true);
		placesAutocompleteTextView.setAdapter(placesAdapter);
		placesAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position==0) {
					placeLatitude = mapCenterLatitude;
					placeLongitude = mapCenterLongitude;

					return;
				} else if(position==1) {
					// Get user current location.
					// Use module for getting current location (will be implemented).
					return;
				}
				
				GetGeolocationFromAddressAsyncTask getPlaceLocationTask = 
						new GetGeolocationFromAddressAsyncTask(onGetGeolocationTaskFinishListener);
				getPlaceLocationTask.execute(placesAdapter.getItem(position));
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
		
		Spinner roomsSpinner = (Spinner) findViewById(R.id.rent_rooms);
		spinnerAdapter = AdapterFactory.createSpinnerAdapter(this, R.array.rent_rooms);
		roomsSpinner.setAdapter(spinnerAdapter);
		
		Spinner bathsSpinner = (Spinner) findViewById(R.id.rent_baths);
		spinnerAdapter = AdapterFactory.createSpinnerAdapter(this, R.array.rent_baths);
		bathsSpinner.setAdapter(spinnerAdapter);
	}
}
