package com.personal.rents.adapter;

import java.util.List;

import android.content.Context;

import com.personal.rents.model.view.RentView;

public class RentsViewsListAdapter<T extends RentView> extends RentsListAdapterTemplate<T> {

	public RentsViewsListAdapter(Context context, int layoutId, List<T> items) {
		super(context, layoutId, items);
	}

	@Override
	protected void setupRentViewHolder(int position, RentViewHolder rentViewHolder) {
		populateRentViewHolder(rentViewHolder, items.get(position).rent);
	}
}
