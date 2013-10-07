package com.personal.rents.fragments;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.R;
import com.personal.rents.activities.RentDetailsActivity;
import com.personal.rents.fragments.adapters.RentsListFragmentAdapter;
import com.personal.rents.model.Rent;
import com.personal.rents.views.SwipeDismissListViewTouchListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class SwipeDismissRentsListFragment extends ListFragment {
	
	private List<Rent> rents;
	
	private List<Rent> initialRents;
	
	private RentsListFragmentAdapter rentsListAdapter;
	
	private OnListItemDismissListener onListItemDismissListener;
	
	public interface OnListItemDismissListener {
		public void onDismiss();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		rentsListAdapter = new RentsListFragmentAdapter(getActivity(), 
				R.layout.rents_list_item_layout, rents);
		setListAdapter(rentsListAdapter);

		ListView rentsListView = getListView();
		SwipeDismissListViewTouchListener touchListener = 
				new SwipeDismissListViewTouchListener(rentsListView, 
						new SwipeDismissListViewTouchListener.DismissCallbacks() {
					
					@Override
					public void onDismiss(ListView listView, int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							if(position < rentsListAdapter.getCount()) {
	                            rentsListAdapter.remove(rentsListAdapter.getItem(position));
	                            onListItemDismissListener.onDismiss();
							}
                        }
					}
					
					@Override
					public boolean canDismiss(int position) {
						return true;
					}
				});
		
		rentsListView.setOnTouchListener(touchListener);
		rentsListView.setOnScrollListener(touchListener.makeScrollListener());
	}

	public void setRents(List<Rent> rents) {
		this.rents = rents;
		initialRents = new ArrayList<Rent>(rents);
	}

	public void setOnListViewItemDismissListener(OnListItemDismissListener onListItemDismissListener) {
		this.onListItemDismissListener = onListItemDismissListener;
	}
	
	public void resetRentsListAdapter() {
		rentsListAdapter.clear();
		for(Rent rent : initialRents) {
			rentsListAdapter.add(rent);
		}
		rentsListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), RentDetailsActivity.class);
		startActivity(intent);
	}
}
