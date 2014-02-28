package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.fragment.SwipeDismissRentsListFragment;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.DeleteUserAddedRentsAsyncTask;
import com.personal.rents.task.GetUserAddedRentsAsyncTask;
import com.personal.rents.task.GetUserAddedRentsNextPageAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class UserAddedRentsActivity extends UserRentsActivity {
	
	private SwipeDismissRentsListFragment rentsListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_added_rents_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}
		
		restoreInstanceState(bundle);
		
		init();
	}
	
	@Override
	protected void setupListFragment() {
		rentsListFragment = (SwipeDismissRentsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
		rentsListFragment.setOnListViewItemDismissListener(new OnListItemDismissListenerImpl());
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(rentsListFragment.getItems().size() == 0) {
			setupProgressBarFragment();
			startGetUserAddedRentsAsyncTask();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_added_rents_menu, menu);

		return true;
	}
	
	public void onConfirmDelBtnClick(View view) {
		setupProgressBarFragment();
		startDeleteUserAddedRentsAsyncTask();
	}
	
	@Override
	public void onUndoDelBtnClick(View view) {
		super.onUndoDelBtnClick(view);

		rentsListFragment.resetListAdapter();
	}
	
	public void onRetryLoadNextPageBtnClick(View view) {
		rentsListFragment.retryLoadingNextPage();
	}
	
	public void onAddRentBtnClick(View view) {
		Intent intent = new Intent(this, AddRentActivity.class);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		startActivity(intent);
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

	private class OnGetUserAddedRentsTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String NO_RESULTS_FOUND_MSG = "Nu aveti nici o chirie adaugata.";

		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result,
						UserAddedRentsActivity.this);

				return;
			}

			if(result == null) {
				Toast.makeText(UserAddedRentsActivity.this, NO_RESULTS_FOUND_MSG, Toast.LENGTH_LONG)
						.show();
				
				return;
			}
			
			totalNoOfItems = ((RentsCounter) result).counter;
			getSupportActionBar().setTitle(totalNoOfItems + " chirii aveti adaugate");
			rentsListFragment.setupListAdapter(((RentsCounter) result).rents, totalNoOfItems,
					new GetUserAddedRentsNextPageAsyncTask(account),
					R.layout.rents_list_item_layout);
		}
	}
	
	private class OnDeleteUserAddedRentsTaskFinishListener implements OnNetworkTaskFinishListener {

		private static final String NO_RESULTS_FOUND_MSG = "Chiriile nu au putut fi sterse.";
		
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, 
						UserAddedRentsActivity.this);

				return;
			}
			
			Integer deletesCount = (Integer) result;
			if(!(deletesCount > 0)) {
				Toast.makeText(UserAddedRentsActivity.this, NO_RESULTS_FOUND_MSG, Toast.LENGTH_LONG)
					.show();
		
				return;
			}
			
			totalNoOfItems = totalNoOfItems - deletesCount;
			getSupportActionBar().setTitle(totalNoOfItems + " chirii aveti adaugate");
			dismissDeleteMenu();
		}
	}
}
