package com.personal.rents.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.android.volley.toolbox.NetworkImageView;
import com.personal.rents.R;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.client.RentsImageClient;
import com.personal.rents.util.GeneralConstants;
import com.personal.rents.util.RentInfoBuilder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class RentsListAdapter extends ArrayAdapter<Rent> implements ListAdapter {

	private LayoutInflater inflater;
	
	private int layoutId;
	
	private List<Rent> rents;
	
	SimpleDateFormat dateFormatter = new SimpleDateFormat(GeneralConstants.RO_DATE_FORMAT,
			new Locale(GeneralConstants.RO_LOCALE, GeneralConstants.RO_LOCALE));
	
	private static class RentViewHolder {

		private NetworkImageView rentImg;

		private TextView rentDate;

		private TextView rentPrice;
		
		private TextView rentAddress;
		
		private TextView rentSpecs;
		
		private TextView rentTypeDesc;
	}

	public RentsListAdapter(Context context, int layoutId, List<Rent> rents) {
		super(context, layoutId, rents);
		
		inflater = ((Activity) context).getLayoutInflater();
		this.layoutId = layoutId;
		this.rents = rents;
	}
	
	@Override
	public int getCount() {
		return rents.size();
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
			rentViewHolder.rentImg = (NetworkImageView) convertView
					.findViewById(R.id.rent_list_item_img);
			rentViewHolder.rentDate = (TextView) convertView.findViewById(R.id.rent_list_item_date);
			rentViewHolder.rentPrice = (TextView) convertView
					.findViewById(R.id.rent_list_item_price);
			rentViewHolder.rentAddress = (TextView) convertView
					.findViewById(R.id.rent_list_item_address);
			rentViewHolder.rentSpecs = (TextView) convertView
					.findViewById(R.id.rent_list_item_specs);
			rentViewHolder.rentTypeDesc = (TextView) convertView
					.findViewById(R.id.rent_list_item_type_desc);
			
			convertView.setTag(rentViewHolder);
		} else {
			rentViewHolder = (RentViewHolder) convertView.getTag();
		}
		
		// Set the values of the rentViewHolder fields to the values of rents.get(position).
		// Nr. bai etc. will be moved in the layout.
		Rent rent = rents.get(position);
		rentViewHolder.rentImg.setImageUrl(GeneralConstants.BASE_URL + rent.rentImageURIs.get(0),
				RentsImageClient.getImageLoader(getContext().getApplicationContext()));
		rentViewHolder.rentDate.setText(dateFormatter.format(rent.rentAddDate).toString());
		rentViewHolder.rentPrice.setText(Integer.toString(rent.rentPrice) + " €");
		rentViewHolder.rentAddress.setText(rent.address.addressStreetName + " Nr. " 
				+ rent.address.addressStreetNo);
		rentViewHolder.rentSpecs.setText(rent.rentRooms + " cam., " + rent.rentBaths + " bai, "
				+ rent.rentSurface + " " + GeneralConstants.SQUARE_METERS);
		rentViewHolder.rentTypeDesc.setText(RentInfoBuilder.buildRentTypeDesc(getContext(),
				 rent.rentType, rent.rentAge));
		
		return convertView;
	}

}