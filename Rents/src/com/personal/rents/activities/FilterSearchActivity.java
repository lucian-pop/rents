package com.personal.rents.activities;

import com.personal.rents.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class FilterSearchActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_search_activity_layout);
		
		init();
	}
	
	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
