package com.personal.rents.activities;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.personal.rents.R;
import com.personal.rents.fragments.SwipeDismissRentsListFragment;
import com.personal.rents.model.Rent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserAddedRentsActivity extends ActionBarActivity {
	
	private List<Rent> rents;
	
	private int dismissedCounter;
	
	private ViewGroup delConfirmationBtnsViewGroup;
	
	private SwipeDismissRentsListFragment rentsListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_added_rents_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			// get bundle from the intent (send by search activity or map activity).
			bundle = getIntent().getExtras();
		}
		
		// populate rents from the bundle.
		
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_added_rents_menu, menu);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.edit_account_action) {
			Intent intent = new Intent(this, EditUserAccountActivity.class);
			startActivity(intent);
			
			return true;
		}
		
		return false;
	}

	public void onUndoDelBtnClick(View view) {
		dismissedCounter = 0;
		delConfirmationBtnsViewGroup.setVisibility(View.GONE);
		rentsListFragment.resetRentsListAdapter();
	}
	
	public void onConfirmDelBtnClick(View view) {
		dismissedCounter = 0;
		delConfirmationBtnsViewGroup.setVisibility(View.GONE);
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Chiriile mele");
		
		delConfirmationBtnsViewGroup = 
				(ViewGroup) findViewById(R.id.delete_confirmation_btns);
		final TextView noOfDelRentsTextView = (TextView)findViewById(R.id.no_of_del_rents);

		rents = new ArrayList<Rent>(6);
		Rent rent = new Rent(new LatLng(10, 10), 160);
		for(int i=0; i < 6; i++) {
			rents.add(rent);
		}
		rentsListFragment = (SwipeDismissRentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
		rentsListFragment.setRents(rents);
		rentsListFragment.setOnListViewItemDismissListener(
				new SwipeDismissRentsListFragment.OnListItemDismissListener() {
			@Override
			public void onDismiss() {
				dismissedCounter++;
				if(delConfirmationBtnsViewGroup.getVisibility() == View.GONE){
					delConfirmationBtnsViewGroup.setVisibility(View.VISIBLE);
				}

				noOfDelRentsTextView.setText(Integer.toString(dismissedCounter));
			}
		});
	}
}
