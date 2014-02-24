package com.personal.rents.fragment;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.model.Rent;
import com.personal.rents.task.LoadNextPageAsyncTask;
import com.personal.rents.view.SwipeDismissListViewTouchListener;

import android.os.Parcelable;
import android.widget.ListView;

public abstract class SwipeDismissRentsListFragmentTemplate<T extends Parcelable> 
		extends RentsListFragmentTemplate<T> {
	
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
		setupSwypeDismissListView();
	}

	@Override
	public void setupListAdapter(List<T> items, int totalNoOfItems,
			LoadNextPageAsyncTask<Void, List<T>, T> loadNextPageTask, int listItemLayoutId) {
		setupEndlessAdapter(new ArrayList<T>(items), totalNoOfItems, loadNextPageTask,
				listItemLayoutId);
		storeEndlessAdapterConfig(items, totalNoOfItems, loadNextPageTask, listItemLayoutId);
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
								Rent rentToDelete = getRentFromAdapter(position);//(Rent) endlessAdapter.getItem(position);
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
	
	protected abstract Rent getRentFromAdapter(int position);
	
	public void resetListAdapter() {
		setupListAdapter(items, totalNoOfItems, loadNextPageTask, listItemLayoutId);
	}
}
