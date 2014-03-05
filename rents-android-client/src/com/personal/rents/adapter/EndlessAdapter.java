package com.personal.rents.adapter;

import java.util.List;

import com.commonsware.cwac.adapter.AdapterWrapper;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.task.LoadNextPageAsyncTask;
import com.personal.rents.task.listener.OnLoadNextPageTaskFinishListener;
import com.personal.rents.webservice.response.ResponseStatusReason;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class EndlessAdapter<T> extends AdapterWrapper {
	
	private List<T> items;
	
	private int totalNoOfItems;
	
	private boolean loading = false;
	
	private boolean keepOnAppending;
	
	private boolean loadError = false;
	
	private LoadNextPageAsyncTask<?, List<T>, T> loadNextPageTask;
	
	private int pendingResource;
	
	private int loadErrorResource;
	
	private View pendingView;
	
	private Context context;
	
	private LayoutInflater inflater;
	
	private OnLoadNextPageTaskFinishListener<T> dummyTaskFinishListener = 
			new OnLoadNextPageTaskFinishListener<T>() {
				@Override
				public void onTaskFinish(List<T> nextPageItems, ResponseStatusReason status) {
				}
	};

	public EndlessAdapter(Context context, ListAdapter adapter, int pendingResource, 
			int loadErrorResource, List<T> items, int totalNoOfItems,
			LoadNextPageAsyncTask<?, List<T>, T> loadNextPageTask) {
		super(adapter);
		
		this.items = items;
		this.totalNoOfItems = totalNoOfItems;
		this.loadNextPageTask = loadNextPageTask;
		this.context = context;
		this.pendingResource = pendingResource;
		this.loadErrorResource = loadErrorResource;
		
		keepOnAppending = items.size() < totalNoOfItems;
		inflater = ((Activity) context).getLayoutInflater();
	}
	
	public void setItems(List<T> items) {
		this.items = items;
	}

	public void setTotalNoOfItems(int totalNoOfItems) {
		this.totalNoOfItems = totalNoOfItems;
	}

	public void setLoadNextPageTask(
			LoadNextPageAsyncTask<?, List<T>, T> loadNextPageTask) {
		this.loadNextPageTask = loadNextPageTask;
	}

	/**
	 * How many items are in the data set represented by this
	 * Adapter.
	 */
	@Override
	public int getCount() {
		if (keepOnAppending) {
	      return super.getCount() + 1;
	    }

	    return super.getCount();
	}

	/**
	 * Masks ViewType so the AdapterView replaces the
	 * "Pending" row when new data is loaded.
	 */
	public int getItemViewType(int position) {
	    if (position == getWrappedAdapter().getCount()) {
	      return(IGNORE_ITEM_VIEW_TYPE);
	    }

	    return super.getItemViewType(position);
	}

	/**
	 * Masks ViewType so the AdapterView replaces the
	 * "Pending" row when new data is loaded.
	 * 
	 * @see #getItemViewType(int)
	 */
	public int getViewTypeCount() {
	    return super.getViewTypeCount() + 1;
	}

	@Override
	public Object getItem(int position) {
	    if (position >= super.getCount()) {
	      return null;
	    }

	    return super.getItem(position);
	}
	
	public void remove(int position) {
		items.remove(position);
		--totalNoOfItems;
		onDataReady();
	}

	@Override
	public boolean areAllItemsEnabled() {
	    return false;
	}

	@Override
	public boolean isEnabled(int position) {
		if (position >= super.getCount()) {
	      return(false);
	    }

	    return super.isEnabled(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position == super.getCount()) {
			if(loadError) {
				pendingView = getLoadErrorView(parent);

				return pendingView;
			} 

			if(keepOnAppending && !loading) {
				launchTask();
			}
			
			pendingView = getPendingView(parent);

			return pendingView;
		}

		return super.getView(position, convertView, parent);
	}

	private View getPendingView(ViewGroup parent) {
		return inflater.inflate(pendingResource, parent, false);
	}
	
	private View getLoadErrorView(ViewGroup parent) {
		return inflater.inflate(loadErrorResource, parent, false);
	}

	@TargetApi(11)
	private void launchTask() {
		if(items.size() < 1) {
			return;
		}

		cancelTaskIfLoading();
		loading = true;
		loadError = false;

		loadNextPageTask = loadNextPageTask.newInstance(new OnLoadNextPageTaskFinishListenerImpl());
		T lastElement = items.get(items.size() - 1);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			loadNextPageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					lastElement);
		} else {
			loadNextPageTask.execute(lastElement);
		}
	}
	
	public void retryLoadingNextPage() {
		loadError = false;
		onDataReady();
	}
	
	public void restartTaskIfLoading() {
		if(loading) {
			launchTask();
		}
	}
	
	public void resetTask() {
		if(loadNextPageTask != null) {
			cancelTaskIfLoading();
			loadNextPageTask.setOnLoadNextPageTaskFinishListener(dummyTaskFinishListener);
		}
	}
	
	private void cancelTaskIfLoading() {
		if(loading) {
			loadNextPageTask.cancel(true);
		}
	}

	public void onDataReady() {
		pendingView = null;
		notifyDataSetChanged();
	}

	private class OnLoadNextPageTaskFinishListenerImpl 
			implements OnLoadNextPageTaskFinishListener<T> {

		@Override
		public void onTaskFinish(List<T> nextPageItems, ResponseStatusReason status) {
			loading = false;

			if(!status.equals(ResponseStatusReason.OK)) {
				loadError = true;
				NetworkErrorHandler.handleRetrofitError(status, nextPageItems, (Activity) context);
				onDataReady();

				return;
			}
			
			if(nextPageItems == null) {
				onDataReady();
				keepOnAppending = false;
	
				return;
			}

			onLoadSuccessfull(nextPageItems);
		}
		
		private void onLoadSuccessfull(List<T> nextPageItems) {
			items.addAll(nextPageItems);
			keepOnAppending = items.size() < totalNoOfItems;
			
			onDataReady();
		}
	}
}
