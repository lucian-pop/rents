package com.personal.rents.fragment;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.adapter.EndlessAdapter;
import com.personal.rents.task.LoadNextPageAsyncTask;
import com.personal.rents.util.ActivitiesContract;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.util.Log;

public abstract class RentsListFragmentTemplate<T extends Parcelable> extends ListFragment {
	
	protected List<T> items;

	protected int totalNoOfItems;

	protected LoadNextPageAsyncTask<Void, List<T>, T> loadNextPageTask;
	
	protected int listItemLayoutId;
	
	protected EndlessAdapter<T> endlessAdapter;
	
	public List<T> getItems() {
		return items;
	}

	public void setListItemLayoutId(int listItemLayoutId) {
		this.listItemLayoutId = listItemLayoutId;
	}

	public void setLoadNextPageTask(
			LoadNextPageAsyncTask<Void, List<T>, T> loadNextPageTask) {
		this.loadNextPageTask = loadNextPageTask;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getActivity().getIntent().getExtras();
		}
		
		restoreInstanceState(bundle);
		
		setupListAdapter(items, totalNoOfItems, loadNextPageTask, listItemLayoutId);
		
		init();
	}

	protected void init() {
	}

	public void setupListAdapter(List<T> items, int totalNoOfItems,
			LoadNextPageAsyncTask<Void, List<T>, T> loadNextPageTask, int listItemLayoutId) {
		setupEndlessAdapter(items, totalNoOfItems, loadNextPageTask, listItemLayoutId);
		storeEndlessAdapterConfig(items, totalNoOfItems, loadNextPageTask, listItemLayoutId);
	}
	
	protected abstract void setupEndlessAdapter(List<T> items, int totalNoOfItems,
			LoadNextPageAsyncTask<Void, List<T>, T> loadNextPageTask, int listItemLayoutId);

	
	protected void storeEndlessAdapterConfig(List<T> items, int totalNoOfItems,
			LoadNextPageAsyncTask<Void, List<T>, T> loadNextPageTask, int listItemLayoutId) {
		this.items = items;
		this.totalNoOfItems = totalNoOfItems;
		this.loadNextPageTask = loadNextPageTask;
		this.listItemLayoutId = listItemLayoutId;
	}

	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}

		items = bundle.getParcelableArrayList(ActivitiesContract.RENTS);
		totalNoOfItems = bundle.getInt(ActivitiesContract.NO_OF_RENTS);

		if (items == null) {
			items = new ArrayList<T>();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList(ActivitiesContract.RENTS,
				(ArrayList<T>) items);
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfItems);

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		endlessAdapter.restartTaskIfLoading();
	}

	@Override
	public void onStop() {
		Log.e("TEST_TAG", "*****************On fragment STOP executed****************");
		super.onStop();

		endlessAdapter.resetTask();
	}

	@Override
	public void onDestroy() {
		Log.e("TEST_TAG", "*****************On fragment DESTROY executed****************");
		super.onDestroy();

		endlessAdapter = null;
		items = null;
	}
	
	public void retryLoadingNextPage() {
		endlessAdapter.retryLoadingNextPage();
	}
}
