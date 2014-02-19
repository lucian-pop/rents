package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.fragment.SwipeDismissRentsListFragment;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.DeleteUserAddedRentsAsyncTask;
import com.personal.rents.task.GetUserAddedRentsAsyncTask;
import com.personal.rents.task.GetUserAddedRentsNextPageAsyncTask;
import com.personal.rents.task.LogoutAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UserAddedRentsActivity extends AccountActivity {
	
	private int totalNoOfRents;
	
	private int dismissedCounter;
	
	private int fromActivity;
	
	private ViewGroup delConfirmationBtnsViewGroup;
	
	private SwipeDismissRentsListFragment rentsListFragment;
	
	private ProgressBarFragment progressBarFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_added_rents_activity_layout);
		
		if(account == null) {
			return;
		}
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}
		
		restoreInstanceState(bundle);
		
		init();
	}
	
	private void init() {
		setupActionBar();
		
		setupListFragment();
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Chiriile mele");
	}
	
	private void setupListFragment() {
		delConfirmationBtnsViewGroup = 
				(ViewGroup) findViewById(R.id.delete_confirmation_btns);
		final TextView noOfDelRentsTextView = (TextView)findViewById(R.id.no_of_del_rents);

		rentsListFragment = (SwipeDismissRentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
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
		rentsListFragment.setLoadNextPageTask(new GetUserAddedRentsNextPageAsyncTask(account));
	}

	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}

		totalNoOfRents = bundle.getInt(ActivitiesContract.NO_OF_RENTS);
		fromActivity = bundle.getInt(ActivitiesContract.FROM_ACTIVITY);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(dismissedCounter > 0) {
			delConfirmationBtnsViewGroup.setVisibility(View.VISIBLE);
		}
		
		if(rentsListFragment.getRents().size() == 0) {
			setupProgressBarFragment();
			startGetUserAddedRentsAsyncTask();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfRents);
		outState.putInt(ActivitiesContract.FROM_ACTIVITY, fromActivity);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.e("TEST_TAG", "*********UserAddedRentsActivity onSTOP called**************");
		
		if(progressBarFragment != null) {
			progressBarFragment.reset();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("TEST_TAG", "*********UserAddedRentsActivity onDESTROY called**************");
		
		progressBarFragment = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_added_rents_menu, menu);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			Intent intent;
			if(fromActivity == ActivitiesContract.RENTS_LIST_ACTIVITY){
				intent = new Intent(this, RentsListActivity.class);
			} else {
				intent = new Intent(this, RentsMapActivity.class);
			}

			NavUtils.navigateUpTo(this, intent);
			
			return true;
		} else if(item.getItemId() == R.id.edit_account_action) {
			Intent intent = new Intent(this, EditUserAccountActivity.class);
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
	
	private void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	private void startGetUserAddedRentsAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetUserAddedRentsAsyncTask getUserAddedRentsTask = new GetUserAddedRentsAsyncTask();
		progressBarFragment.setTask(getUserAddedRentsTask);
		progressBarFragment.setOnTaskFinishListener(new OnGetUserAddedRentsTaskFinishListener());
		getUserAddedRentsTask.execute(account);
	}
	
	private void startDeleteUserAddedRentsAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		DeleteUserAddedRentsAsyncTask deleteUserAddedRentsTask = 
				new DeleteUserAddedRentsAsyncTask(rentsListFragment.getRentsToDelete());
		progressBarFragment.setTask(deleteUserAddedRentsTask);
		progressBarFragment.setOnTaskFinishListener(new OnDeleteUserAddedRentsTaskFinishListener());
		deleteUserAddedRentsTask.execute(account);
	}

	public void onUndoDelBtnClick(View view) {
		dismissedCounter = 0;
		delConfirmationBtnsViewGroup.setVisibility(View.GONE);
		rentsListFragment.resetListAdapter();
	}
	
	public void onConfirmDelBtnClick(View view) {
		setupProgressBarFragment();
		startDeleteUserAddedRentsAsyncTask();
	}
	
	public void onAddRentBtnClick(View view) {
		Intent intent = new Intent(this, AddRentActivity.class);
		startActivity(intent);
	}
	
	public void onRetryLoadNextPageBtnClick(View view) {
		((SwipeDismissRentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment)).retryLoadingNextPage();
	}
	
	private class OnGetUserAddedRentsTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String NO_RESULTS_FOUND_MSG = "Nu aveti nici o chirie adaugata.";

		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, UserAddedRentsActivity.this);

				return;
			}

			if(result == null) {
				Toast.makeText(UserAddedRentsActivity.this, NO_RESULTS_FOUND_MSG, Toast.LENGTH_LONG)
						.show();
				
				return;
			}
			
			totalNoOfRents = ((RentsCounter) result).counter;
			getSupportActionBar().setTitle(totalNoOfRents + " chirii aveti adaugate");
			rentsListFragment.setupListAdapter(((RentsCounter) result).rents, totalNoOfRents,
					new GetUserAddedRentsNextPageAsyncTask(account));
		}
	}
	
	private class OnDeleteUserAddedRentsTaskFinishListener implements OnNetworkTaskFinishListener {

		private static final String NO_RESULTS_FOUND_MSG = "Chiriile nu au putut fi sterse.";
		
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, UserAddedRentsActivity.this);

				return;
			}
			
			Integer noOfDeletedRents = (Integer) result;
			if(!(noOfDeletedRents > 0)) {
				Toast.makeText(UserAddedRentsActivity.this, NO_RESULTS_FOUND_MSG, Toast.LENGTH_LONG)
					.show();
		
				return;
			}
			
			dismissedCounter = 0;
			delConfirmationBtnsViewGroup.setVisibility(View.GONE);
		}
		
	}
}
