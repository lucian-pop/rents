//package com.personal.rents.adapter;
//
//import java.util.List;
//
//import com.commonsware.cwac.endless.EndlessAdapter;
//import com.personal.rents.model.Rent;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListAdapter;
//
//public class RentsListEndlessAdapter extends EndlessAdapter {
//
//	private int pendingResourceId;
//	
//	private LayoutInflater inflater;
//	
//	private List<Rent> rents;
//	
//	private List<Rent> result;
//	
//	private boolean loading = false;
//
//	private int totalNoOfItems;
//	
//	public RentsListEndlessAdapter(ListAdapter wrapped, boolean keepOnAppending) {
//		super(wrapped, keepOnAppending);
//	}
//	
//	public RentsListEndlessAdapter(Context context, int adapterLayoutId, int pendingResourceId,
//			List<Rent> rents, int totalNoOfItems) {
//		this(new RentsListAdapter(context, adapterLayoutId, rents), rents.size() < totalNoOfItems);
//		this.pendingResourceId = pendingResourceId;
//		this.rents = rents;
//		this.totalNoOfItems = totalNoOfItems;
//		
//		
//		inflater = ((Activity) context).getLayoutInflater();
//	}
//
//	@Override
//	protected View getPendingView(ViewGroup parent) {
//		return inflater.inflate(pendingResourceId, parent, false);
//	}
//
//	@Override
//	protected boolean cacheInBackground() throws Exception {
//		Log.e("TEST_TAG", "**************Cache in background called");
//		SystemClock.sleep(10000);
//		// result = Task.execute();
//		
//		return rents.size() + 50 < totalNoOfItems;
//	}
//	
//	@Override
//	protected void appendCachedData() {
////		if(result != null) {
////			rents.addAll(result);
////		}
//		rents.addAll(rents);
//		Log.e("TEST_TAG", "*******************Append cached data: " + rents.size());
//
//		result = null;
//	}
//
//	@Override
//	protected boolean onException(View pendingView, Exception e) {
//		// Handle RetrofitError.
//		return super.onException(pendingView, e);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		return super.getView(position, convertView, parent);
//	}
//	
//	
//
//}
