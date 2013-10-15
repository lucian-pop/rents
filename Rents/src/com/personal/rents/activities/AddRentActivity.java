package com.personal.rents.activities;

import com.personal.rents.R;
import com.personal.rents.model.Address;
import com.personal.rents.utils.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AddRentActivity extends ActionBarActivity {
	
	private Address address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rent_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}
		address = bundle != null 
				? (Address) bundle.getParcelable(ActivitiesContract.ADDRESS) : null;

		init();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(ActivitiesContract.ADDRESS, address);
	}

	public void onLocateRentBtnClick(View view) {
		Intent intent = new Intent(this, AddLocationActivity.class);
		intent.putExtra(ActivitiesContract.ADDRESS, address);

		startActivity(intent);
	}
	
	private void init() {
		setupActionBar();
		
		TextView locateRentBtn = (TextView) findViewById(R.id.locate_rent_btn);
		if(address != null) {
			locateRentBtn.setText(address.toString());
		}
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Adaugare chirie");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
