package com.personal.rents.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.personal.rents.R;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.fragment.SwipeDismissRentsListFragmentTemplate;
import com.personal.rents.task.LogoutAsyncTask;
import com.personal.rents.util.ActivitiesContract;

public abstract class UserRentsActivity extends AccountActivity {

	protected int totalNoOfItems;
	
	protected int dismissedCounter;
	
	protected String fromActivity;
	
	protected ViewGroup delConfirmationBtnsViewGroup;
	
	protected TextView noOfDelRentsTextView;
	
	protected ProgressBarFragment progressBarFragment;

	protected void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}

		totalNoOfItems = bundle.getInt(ActivitiesContract.NO_OF_RENTS);
		fromActivity = bundle.getString(ActivitiesContract.FROM_ACTIVITY);
	}
	
	protected void init() {
		setupActionBar();
		
		initDelConfirmationBtnsViewGroup();

		setupListFragment();
	}
	
	protected void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Chiriile mele");
	}
	
	private void initDelConfirmationBtnsViewGroup() {
		delConfirmationBtnsViewGroup = 
				(ViewGroup) findViewById(R.id.delete_confirmation_btns);
		noOfDelRentsTextView = (TextView)findViewById(R.id.no_of_del_rents);
	}
	
	protected abstract void setupListFragment();
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(dismissedCounter > 0) {
			delConfirmationBtnsViewGroup.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfItems);
		outState.putString(ActivitiesContract.FROM_ACTIVITY, fromActivity);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(progressBarFragment != null) {
			progressBarFragment.reset();
			progressBarFragment.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		progressBarFragment = null;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			Intent intent;
			if(fromActivity.equals(RentsListActivity.class.getSimpleName())) {
				intent = new Intent(this, RentsListActivity.class);
			} else {
				intent = new Intent(this, RentsMapActivity.class);
			}

			NavUtils.navigateUpTo(this, intent);
			
			return true;
		} else if(item.getItemId() == R.id.edit_account_action) {
			Intent intent = new Intent(this, EditUserAccountActivity.class);
			intent.putExtra(ActivitiesContract.FROM_ACTIVITY, this.getClass().getSimpleName());
			startActivity(intent);
			
			return true;
		} else if(item.getItemId() == R.id.signout_action) {
			LogoutAsyncTask logoutTask = new LogoutAsyncTask();
			logoutTask.execute(this.getApplicationContext());
			
			Intent intent = new Intent(this, RentsMapActivity.class);
			startActivity(intent);
			
			return true;
		}
		
		return false;
	}

	protected void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	public void onUndoDelBtnClick(View view) {
		dismissDeleteMenu();
	}
	
	public abstract void onRetryLoadNextPageBtnClick(View view);
	
	protected void dismissDeleteMenu() {
		dismissedCounter = 0;
		delConfirmationBtnsViewGroup.setVisibility(View.GONE);
	}
	
	protected class OnListItemDismissListenerImpl implements SwipeDismissRentsListFragmentTemplate.OnListItemDismissListener{
		@Override
		public void onDismiss() {
			dismissedCounter++;
			if(delConfirmationBtnsViewGroup.getVisibility() == View.GONE){
				delConfirmationBtnsViewGroup.setVisibility(View.VISIBLE);
			}

			noOfDelRentsTextView.setText(Integer.toString(dismissedCounter));
		}
		
	}
}
