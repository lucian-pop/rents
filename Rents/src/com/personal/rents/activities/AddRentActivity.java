package com.personal.rents.activities;

import com.personal.rents.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class AddRentActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rent_activity_layout);
		
		init();
	}

	public void onLocateRentBtnClick(View view) {
		Intent intent = new Intent(this, AddLocationActivity.class);
		startActivity(intent);
	}
	
	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Adaugare chirie");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
