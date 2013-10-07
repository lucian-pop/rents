package com.personal.rents.fragments;
import java.util.List;

import com.personal.rents.R;
import com.personal.rents.activities.RentDetailsActivity;
import com.personal.rents.fragments.adapters.RentsListFragmentAdapter;
import com.personal.rents.model.Rent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class RentsListFragment extends ListFragment {
	
	protected List<Rent> rents;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Populate the fragment by using a list adapter
		setListAdapter(new RentsListFragmentAdapter(getActivity(), 
				R.layout.rents_list_item_layout, rents));
	}

	public void setRents(List<Rent> rents) {
		this.rents = rents;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), RentDetailsActivity.class);
		startActivity(intent);
	}
}
