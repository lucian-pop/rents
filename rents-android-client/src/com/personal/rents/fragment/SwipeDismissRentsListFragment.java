package com.personal.rents.fragment;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.activity.RentDetailsActivity;
import com.personal.rents.model.Rent;
import com.personal.rents.task.LoadNextPageAsyncTask;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.view.SwipeDismissListViewTouchListener;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class SwipeDismissRentsListFragment extends RentsListFragment {
	
	private List<Integer> rentsToDelete = new ArrayList<Integer>();
	
	private OnListItemDismissListener onListItemDismissListener;
	
	public interface OnListItemDismissListener {
		public void onDismiss();
	}
	
	public List<Integer> getRentsToDelete() {
		return rentsToDelete;
	}

	public void setOnListViewItemDismissListener(OnListItemDismissListener onListItemDismissListener) {
		this.onListItemDismissListener = onListItemDismissListener;
	}

	@Override
	protected void init() {
		super.init();
		
		setupSwypeDismissListView();
	}

	@Override
	public void setupListAdapter(List<Rent> rents, int totalNoOfRents,
			LoadNextPageAsyncTask<Void, List<Rent>, Rent> loadNextPageTask) {
		buildEndlessAdapter(new ArrayList<Rent>(rents), totalNoOfRents, loadNextPageTask);
		storeEndlessAdapterConfig(rents, totalNoOfRents, loadNextPageTask);
	}

	private void setupSwypeDismissListView() {
		ListView rentsListView = getListView();
		SwipeDismissListViewTouchListener touchListener = 
				new SwipeDismissListViewTouchListener(rentsListView, 
						new SwipeDismissListViewTouchListener.DismissCallbacks() {
					
					@Override
					public void onDismiss(ListView listView, int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							if(position < endlessAdapter.getCount()) {
								Rent rentToDelete = (Rent) endlessAdapter.getItem(position);
								rentsToDelete.add(rentToDelete.rentId);
	                            endlessAdapter.remove(position);

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
	
	public void resetListAdapter() {
		setupListAdapter(rents, totalNoOfRents, loadNextPageTask);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), RentDetailsActivity.class);
		intent.putExtra(ActivitiesContract.RENT_ID, 
				((Rent) endlessAdapter.getItem(position)).rentId);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, ActivitiesContract.USER_ACCOUNT_ACTIVITY);
		
		startActivity(intent);
	}
}
