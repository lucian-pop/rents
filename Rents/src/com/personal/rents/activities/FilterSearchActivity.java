package com.personal.rents.activities;

import com.personal.rents.R;
import com.personal.rents.activities.adapters.PlacesSuggestionsAdapter;
import com.personal.rents.activities.tasks.GetGeolocationFromAddressAsyncTask;
import com.personal.rents.activities.tasks.OnGetGeolocationTaskFinishListener;
import com.personal.rents.model.Address;
import com.personal.rents.utils.ActivitiesContract;
import com.personal.rents.utils.RangeMessageBuilder;
import com.personal.rents.views.DelayAutocompleteTextView;
import com.personal.rents.views.RangeSeekBarView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class FilterSearchActivity extends ActionBarActivity {
	
	private static final int MIN_PRICE = 0;
	
	private static final int MAX_PRICE = 1000;
	
	private static final int MIN_SURFACE = 0;
	
	private static final int MAX_SURFACE = 1000;
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private double placeLatitude;
	
	private double placeLongitude;
	
	private OnGetGeolocationTaskFinishListener onGetGeolocationTaskFinishListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_search_activity_layout);
		
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filter_search_menu, menu);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.user_account_action) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			
			return true;
		}
		
		return false;
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Cautare chirii");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mapCenterLatitude = extras.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = extras.getDouble(ActivitiesContract.LONGITUDE);
		}
		
		// add OnGetPlaceLocationTaskFinishListener
		onGetGeolocationTaskFinishListener = new OnGetGeolocationTaskFinishListener() {
			@Override
			public void onGetGeolocationTaskFinish(Address address) {
				placeLatitude = address.latitude;
				placeLongitude = address.longitude;
			}
		};
		
		// Setup places search view.
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
		
		Spinner partiesSpinner = (Spinner) findViewById(R.id.rent_party);
		ArrayAdapter<CharSequence> spinnerAdapter = createSpinnerAdapter(R.array.rent_parties);
		partiesSpinner.setAdapter(spinnerAdapter);
		
		Spinner typesSpinner = (Spinner) findViewById(R.id.rent_type);
		spinnerAdapter = createSpinnerAdapter(R.array.rent_types);
		typesSpinner.setAdapter(spinnerAdapter);
		
		Spinner structSpinner = (Spinner) findViewById(R.id.rent_structure);
		spinnerAdapter = createSpinnerAdapter(R.array.rent_structures);
		structSpinner.setAdapter(spinnerAdapter);
		
		Spinner ageSpinner = (Spinner) findViewById(R.id.rent_age);
		spinnerAdapter = createSpinnerAdapter(R.array.rent_ages);
		ageSpinner.setAdapter(spinnerAdapter);
		
		Spinner roomsSpinner = (Spinner) findViewById(R.id.rent_rooms);
		spinnerAdapter = createSpinnerAdapter(R.array.rent_rooms);
		roomsSpinner.setAdapter(spinnerAdapter);
		
		Spinner bathsSpinner = (Spinner) findViewById(R.id.rent_baths);
		spinnerAdapter = createSpinnerAdapter(R.array.rent_baths);
		bathsSpinner.setAdapter(spinnerAdapter);
		
		ViewGroup priceChooserWrapper = (ViewGroup) findViewById(R.id.rent_price_chooser);
		final TextView selectedPriceRange = (TextView) findViewById(R.id.rent_price_range);
		RangeSeekBarView<Integer> priceChooser = new RangeSeekBarView<Integer>(MIN_PRICE, MAX_PRICE, this);
		priceChooser.setNotifyWhileDragging(true);
		priceChooser.setOnRangeSeekBarChangeListener(
				new RangeSeekBarView.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBarView<?> bar, Integer minValue, 
					Integer maxValue) {
				selectedPriceRange.setText(RangeMessageBuilder.priceRangeMessageBuilder(minValue,
						maxValue, MIN_PRICE, MAX_PRICE));
			}
		});
		priceChooserWrapper.addView(priceChooser);
		
		ViewGroup surfaceChooserWrapper = (ViewGroup) findViewById(R.id.rent_surface_chooser);
		final TextView selectedSurfaceRange = (TextView) findViewById(R.id.rent_surface_range);
		RangeSeekBarView<Integer> surfaceChooser = new RangeSeekBarView<Integer>(MIN_SURFACE, MAX_SURFACE,
				this);
		surfaceChooser.setNotifyWhileDragging(true);
		surfaceChooser.setOnRangeSeekBarChangeListener(new RangeSeekBarView.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBarView<?> bar, Integer minValue, 
					Integer maxValue) {
				selectedSurfaceRange.setText(RangeMessageBuilder.surfaceRangeMessageBuilder(minValue,
						maxValue, MIN_SURFACE, MAX_SURFACE));
			}
		});
		surfaceChooserWrapper.addView(surfaceChooser);
	}
	
	private ArrayAdapter<CharSequence> createSpinnerAdapter(final int arrayResourceId) {
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, arrayResourceId, 
				android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		return arrayAdapter;
	}
}
