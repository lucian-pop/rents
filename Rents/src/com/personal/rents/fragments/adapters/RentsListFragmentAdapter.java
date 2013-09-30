package com.personal.rents.fragments.adapters;

import java.util.List;

import com.personal.rents.R;
import com.personal.rents.model.Rent;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RentsListFragmentAdapter extends ArrayAdapter<Rent> {

	private LayoutInflater inflater;
	
	private int layoutId;
	
	private List<Rent> rents;
	
	private static class RentViewHolder {

		private ImageView rentImg;

		private TextView rentDate;

		private TextView rentPrice;
		
		private TextView rentAddress;
		
		private TextView rentSpecs;
		
		private TextView rentTypeDesc;
	}

	public RentsListFragmentAdapter(Context context, int layoutId, List<Rent> rents) {
		super(context, layoutId, rents);
		
		inflater = ((Activity) context).getLayoutInflater();
		this.layoutId = layoutId;
		this.rents = rents;
	}
	
	@Override
	public int getCount() {
		return 6;
	}

	@Override
	public Rent getItem(int position) {
		return rents.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RentViewHolder rentViewHolder;
		if(convertView == null) {
			convertView = inflater.inflate(layoutId, parent, false);
			rentViewHolder = new RentViewHolder();
			rentViewHolder.rentImg = (ImageView) convertView.findViewById(R.id.rent_list_item_img);
			rentViewHolder.rentDate = (TextView) convertView.findViewById(R.id.rent_list_item_date);
			rentViewHolder.rentPrice = (TextView) convertView.findViewById(R.id.rent_list_item_price);
			rentViewHolder.rentSpecs = (TextView) convertView.findViewById(R.id.rent_list_item_specs);
			rentViewHolder.rentTypeDesc = (TextView) convertView.findViewById(R.id.rent_list_item_type_desc);
			
			convertView.setTag(rentViewHolder);
		} else {
			rentViewHolder = (RentViewHolder) convertView.getTag();
		}
		
		// Set the values of the rentViewHolder fields to the values of rents.get(position).
		
		return convertView;
	}

}
