package com.personal.rents.fragments;

import java.util.List;

import com.personal.rents.R;
import com.personal.rents.fragments.adapters.RentsListFragmentAdapter;
import com.personal.rents.model.Rent;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class RentsListFragment extends ListFragment {
	
	private List<Rent> rents;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null){
			// We get the rents from the fragment saved instance state.
		} else {
			// Fragment rents were assigned by the activity.
		}
		
		// Populate the fragment by using a list adaper
		setListAdapter(new RentsListFragmentAdapter(getActivity(), R.layout.rents_list_item_layout, rents));
	}

	public void setRents(List<Rent> rents) {
		this.rents = rents;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}
}
