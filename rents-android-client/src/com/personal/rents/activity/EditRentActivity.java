package com.personal.rents.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.personal.rents.R;
import com.personal.rents.util.ActivitiesContract;

public class EditRentActivity extends ManageRentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_rent_activity_layout);
		
		restoreInstanceState(savedInstanceState);

		init();
	}

	@Override
	public void onLocateRentBtnClick(View view) {
		Intent intent = new Intent(this, AddLocationActivity.class);
		intent.putExtra(ActivitiesContract.ADDRESS, address);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, EditRentActivity.class.getSimpleName());

		startActivityForResult(intent, ActivitiesContract.ADD_LOCATION_REQ_CODE);
	}
}
