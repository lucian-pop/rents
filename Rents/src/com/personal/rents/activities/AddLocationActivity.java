package com.personal.rents.activities;

import com.personal.rents.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;

public class AddLocationActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_location_activity_layout);
		
		init();
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		LayoutInflater inflater = this.getLayoutInflater();
		View autocompleteSearchView = inflater.inflate(R.layout.add_location_menu_layout, null);
		getSupportActionBar().setCustomView(autocompleteSearchView);
	}
}
