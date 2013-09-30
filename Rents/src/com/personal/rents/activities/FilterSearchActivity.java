package com.personal.rents.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.personal.rents.R;
import com.personal.rents.activities.adapters.PlacesSuggestionsAdapter;
import com.personal.rents.rest.clients.PlacesRESTClient;
import com.personal.rents.utils.ActivitiesContract;
import com.personal.rents.utils.Constants;
import com.personal.rents.utils.RangeMessageBuilder;
import com.personal.rents.views.DelayAutocompleteTextView;
import com.personal.rents.views.RangeSeekBar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class FilterSearchActivity extends ActionBarActivity {
	
	private static final String LOG_TAG = FilterSearchActivity.class.getSimpleName();
	
	private static final int MIN_PRICE = 0;
	
	private static final int MAX_PRICE = 1000;
	
	private static final int MIN_SURFACE = 0;
	
	private static final int MAX_SURFACE = 1000;
	
	private double mapCenterLatitude;
	
	private double mapCenterLongitude;
	
	private double queryLatitude;
	
	private double queryLongitude;

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
	
	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mapCenterLatitude = extras.getDouble(ActivitiesContract.LATITUDE);
			mapCenterLongitude = extras.getDouble(ActivitiesContract.LONGITUDE);
		}
		
		// Setup places search view.
		DelayAutocompleteTextView placesAutocompleteTextView = 
				(DelayAutocompleteTextView) findViewById(R.id.autocomplete_places_input);
		final PlacesSuggestionsAdapter placesAdapter = new PlacesSuggestionsAdapter(this, 
				R.layout.places_suggestions_list_layout, mapCenterLatitude, mapCenterLongitude);
		placesAutocompleteTextView.setAdapter(placesAdapter);
		placesAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position==0) {
					queryLatitude = mapCenterLatitude;
					queryLongitude = mapCenterLongitude;

					return;
				} else if(position==1) {
					// Get user current location.
					// Use module for getting current location (will be implemented).
					return;
				}
				
				GetPlaceLocationAsyncTask getPlaceLocationTask = new GetPlaceLocationAsyncTask();
				getPlaceLocationTask.execute(placesAdapter.getPlacesRefs().get(position - 2));
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
		RangeSeekBar<Integer> priceChooser = new RangeSeekBar<Integer>(MIN_PRICE, MAX_PRICE, this);
		priceChooser.setNotifyWhileDragging(true);
		priceChooser.setOnRangeSeekBarChangeListener(
				new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, 
					Integer maxValue) {
				selectedPriceRange.setText(RangeMessageBuilder.priceRangeMessageBuilder(minValue,
						maxValue, MIN_PRICE, MAX_PRICE));
			}
		});
		priceChooserWrapper.addView(priceChooser);
		
		ViewGroup surfaceChooserWrapper = (ViewGroup) findViewById(R.id.rent_surface_chooser);
		final TextView selectedSurfaceRange = (TextView) findViewById(R.id.rent_surface_range);
		RangeSeekBar<Integer> surfaceChooser = new RangeSeekBar<Integer>(MIN_SURFACE, MAX_SURFACE,
				this);
		surfaceChooser.setNotifyWhileDragging(true);
		surfaceChooser.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, 
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
	
	private class GetPlaceLocationAsyncTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... refs) {
			JSONObject location = PlacesRESTClient.getPlaceLocation(refs[0]);
			return location;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				queryLatitude = result.getDouble(Constants.LATITUDE);
				queryLongitude = result.getDouble(Constants.LONGITUDE);
			} catch (JSONException e) {
				Log.e(LOG_TAG, "Cannot process JSON location object");
			}
		}
	}
}
