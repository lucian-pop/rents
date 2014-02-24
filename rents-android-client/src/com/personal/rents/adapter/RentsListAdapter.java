package com.personal.rents.adapter;

import java.util.List;

import android.content.Context;

import com.personal.rents.model.Rent;

public class RentsListAdapter extends RentsListAdapterTemplate<Rent>{

	public RentsListAdapter(Context context, int layoutId, List<Rent> items) {
		super(context, layoutId, items);
	}

	@Override
	protected void setupRentViewHolder(int position, RentViewHolder rentViewHolder) {
		populateRentViewHolder(rentViewHolder, items.get(position));
	}
}
