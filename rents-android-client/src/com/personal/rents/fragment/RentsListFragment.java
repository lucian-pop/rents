package com.personal.rents.fragment;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.personal.rents.R;
import com.personal.rents.activity.RentDetailsActivity;
import com.personal.rents.adapter.EndlessAdapter;
import com.personal.rents.adapter.RentsListAdapter;
import com.personal.rents.model.Rent;
import com.personal.rents.task.LoadNextPageAsyncTask;
import com.personal.rents.util.ActivitiesContract;

public class RentsListFragment extends RentsListFragmentTemplate<Rent>{

	@Override
	protected void setupEndlessAdapter(List<Rent> items, int totalNoOfItems,
			LoadNextPageAsyncTask<Void, List<Rent>, Rent> loadNextPageTask,
			int listItemLayoutId) {
		endlessAdapter = new EndlessAdapter<Rent>(getActivity(), new RentsListAdapter(getActivity(),
				listItemLayoutId, items), R.layout.rents_list_footer_layout,
				R.layout.rents_list_error_footer_layout, items, totalNoOfItems, loadNextPageTask);

		setListAdapter(endlessAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), RentDetailsActivity.class);
		intent.putExtra(ActivitiesContract.RENT_ID, 
				((Rent) endlessAdapter.getItem(position)).rentId);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, getActivity().getClass().getSimpleName());
		
		startActivity(intent);
	}
}
