package com.personal.rents.fragment;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.R;
import com.personal.rents.activity.RentDetailsActivity;
import com.personal.rents.adapter.EndlessAdapter;
import com.personal.rents.adapter.RentsListAdapter;
import com.personal.rents.model.Rent;
import com.personal.rents.task.LoadNextPageAsyncTask;
import com.personal.rents.util.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class RentsListFragment extends ListFragment {

	private List<Rent> rents;

	private int totalNoOfRents;

	private EndlessAdapter<Rent> endlessAdapter;

	private LoadNextPageAsyncTask<Void, List<Rent>, Rent> loadNextPageTask;

	public void setLoadNextPageTask(
			LoadNextPageAsyncTask<Void, List<Rent>, Rent> loadNextPageTask) {
		this.loadNextPageTask = loadNextPageTask;
	}
	
	public void renewEndlessAdapter(List<Rent> rents, int totalNoOfRents,
			LoadNextPageAsyncTask<Void, List<Rent>, Rent> loadNextPageTask) {
		this.rents = rents;
		this.totalNoOfRents = totalNoOfRents;
		this.loadNextPageTask = loadNextPageTask;

		endlessAdapter = new EndlessAdapter<Rent>(getActivity(), 
				new RentsListAdapter(getActivity(), R.layout.rents_list_item_layout, rents),
				R.layout.rents_list_footer_layout, R.layout.rents_list_error_footer_layout,
				rents, totalNoOfRents, loadNextPageTask);
		setListAdapter(endlessAdapter);
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
		
		init(bundle);

		endlessAdapter = new EndlessAdapter<Rent>(getActivity(), 
				new RentsListAdapter(getActivity(), R.layout.rents_list_item_layout, rents),
				R.layout.rents_list_footer_layout, R.layout.rents_list_error_footer_layout,
				rents, totalNoOfRents, loadNextPageTask);
		setListAdapter(endlessAdapter);
		
	}

	private void init(Bundle bundle) {
		if (bundle != null) {
			rents = bundle.getParcelableArrayList(ActivitiesContract.RENTS);
			totalNoOfRents = bundle.getInt(ActivitiesContract.NO_OF_RENTS);
		}

		if (rents == null) {
			rents = new ArrayList<Rent>();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList(ActivitiesContract.RENTS,
				(ArrayList<Rent>) rents);
		outState.putInt(ActivitiesContract.NO_OF_RENTS, totalNoOfRents);

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
		rents = null;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), RentDetailsActivity.class);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY,
				ActivitiesContract.RENTS_LIST_ACTIVITY);

		startActivity(intent);
	}
	
	public void retryLoadingNextPage() {
		endlessAdapter.retryLoadingNextPage();
	}
}
