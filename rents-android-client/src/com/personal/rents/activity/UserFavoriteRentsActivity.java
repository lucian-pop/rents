package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.dto.RentFavoriteViewsCounter;
import com.personal.rents.fragment.SwipeDismissRentFavoriteViewsListFragment;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.DeleteUserFavoriteRentsAsyncTask;
import com.personal.rents.task.GetUserFavoriteRentsAsyncTask;
import com.personal.rents.task.GetUserFavoriteRentsNextPageAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class UserFavoriteRentsActivity extends UserRentsActivity {
	
	private SwipeDismissRentFavoriteViewsListFragment rentsListFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_favorite_rents_activity_layout);
		
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
		rentsListFragment = (SwipeDismissRentFavoriteViewsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.rents_list_fragment);
		rentsListFragment.setOnListViewItemDismissListener(new OnListItemDismissListenerImpl());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(rentsListFragment.getItems().size() == 0) {
			setupProgressBarFragment();
			startGetUserFavoriteRentsAsyncTask();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_favorite_rents_menu, menu);

		return true;
	}

	public void onConfirmDelBtnClick(View view) {
		setupProgressBarFragment();
		startDeleteUserFavoriteRentsAsyncTask();
	}
	
	@Override
	public void onUndoDelBtnClick(View view) {
		super.onUndoDelBtnClick(view);

		rentsListFragment.resetListAdapter();
	}
	
	public void onRetryLoadNextPageBtnClick(View view) {
		rentsListFragment.retryLoadingNextPage();
	}

	private void startGetUserFavoriteRentsAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetUserFavoriteRentsAsyncTask getUserFavoriteRentsTask 
			= new GetUserFavoriteRentsAsyncTask();
		progressBarFragment.setTask(getUserFavoriteRentsTask);
		progressBarFragment.setOnTaskFinishListener(new OnGetUserFavoriteRentsTaskFinishListener());
		getUserFavoriteRentsTask.execute(account);
	}
	
	private void startDeleteUserFavoriteRentsAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		DeleteUserFavoriteRentsAsyncTask deleteUserAddedRentsTask = 
				new DeleteUserFavoriteRentsAsyncTask(rentsListFragment.getRentsToDelete());
		progressBarFragment.setTask(deleteUserAddedRentsTask);
		progressBarFragment.setOnTaskFinishListener(
				new OnDeleteUserFavoriteRentsTaskFinishListener());
		deleteUserAddedRentsTask.execute(account);
	}
	
	private class OnGetUserFavoriteRentsTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String NO_RESULTS_FOUND_MSG = "Nu aveti nici o chirie salvata.";

		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result,
						UserFavoriteRentsActivity.this);

				return;
			}

			if(result == null) {
				Toast.makeText(UserFavoriteRentsActivity.this, NO_RESULTS_FOUND_MSG,
						Toast.LENGTH_LONG).show();
				
				return;
			}
			
			totalNoOfItems = ((RentFavoriteViewsCounter) result).counter;
			getSupportActionBar().setTitle(totalNoOfItems + " chirii aveti adaugate");
			rentsListFragment.setupListAdapter(
					((RentFavoriteViewsCounter) result).rentFavoriteViews, totalNoOfItems,
					new GetUserFavoriteRentsNextPageAsyncTask(account),
					R.layout.rents_favorite_list_item_layout);

		}
	}

	private class OnDeleteUserFavoriteRentsTaskFinishListener 
			implements OnNetworkTaskFinishListener {

		private static final String NO_RESULTS_FOUND_MSG = "Chiriile nu au putut fi sterse.";
		
		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result,
						UserFavoriteRentsActivity.this);

				return;
			}
			
			Integer deletesCount = (Integer) result;
			if(!(deletesCount > 0)) {
				Toast.makeText(UserFavoriteRentsActivity.this, NO_RESULTS_FOUND_MSG, Toast.LENGTH_LONG)
					.show();
		
				return;
			}
			
			totalNoOfItems = totalNoOfItems - deletesCount;
			getSupportActionBar().setTitle(totalNoOfItems + " chirii aveti adaugate");
			dismissDeleteMenu();
		}
	}
}
