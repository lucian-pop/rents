package com.personal.rents.activities;

import com.personal.rents.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class UserRentsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_rents_activity_layout);
		
		init();
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
