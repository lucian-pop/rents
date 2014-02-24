package com.personal.rents.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.android.volley.toolbox.NetworkImageView;
import com.personal.rents.R;
import com.personal.rents.R.color;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentStatus;
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

public abstract class RentsListAdapterTemplate<T> extends ArrayAdapter<T> implements ListAdapter {

	private LayoutInflater inflater;
	
	private int layoutId;
	
	private String unavailableStatus;
	
	private int unavailableColor;
	
	protected List<T> items;
	
	protected SimpleDateFormat dateFormatter = new SimpleDateFormat(GeneralConstants.RO_DATE_FORMAT,
			new Locale(GeneralConstants.RO_LOCALE, GeneralConstants.RO_LOCALE));
	
	protected static class RentViewHolder {

		private NetworkImageView rentImg;

		private TextView rentDate;

		private TextView rentPrice;
		
		private TextView rentStatus;
		
		private TextView rentAddress;
		
		private TextView rentSpecs;
		
		private TextView rentTypeDesc;
	}

	public RentsListAdapterTemplate(Context context, int layoutId, List<T> items) {
		super(context, layoutId, items);
		
		inflater = ((Activity) context).getLayoutInflater();
		this.layoutId = layoutId;
		this.items = items;
		
		unavailableStatus = context.getResources().getString(R.string.rent_status_unavailable);
		unavailableColor = context.getResources().getColor(color.red);
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public T getItem(int position) {
		return items.get(position);
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
			rentViewHolder.rentPrice = 
					(TextView) convertView.findViewById(R.id.rent_list_item_price);
			rentViewHolder.rentStatus = (TextView) convertView.findViewById(R.id.rent_status);
			rentViewHolder.rentAddress = 
					(TextView) convertView.findViewById(R.id.rent_list_item_address);
			rentViewHolder.rentSpecs = 
					(TextView) convertView.findViewById(R.id.rent_list_item_specs);
			rentViewHolder.rentTypeDesc = 
					(TextView) convertView.findViewById(R.id.rent_list_item_type_desc);
			
			convertView.setTag(rentViewHolder);
		} else {
			rentViewHolder = (RentViewHolder) convertView.getTag();
		}
		
		setupRentViewHolder(position, rentViewHolder);
		
		return convertView;
	}

	protected abstract  void setupRentViewHolder(int position, RentViewHolder rentViewHolder);
	
	protected void populateRentViewHolder(RentViewHolder rentViewHolder, Rent rent) {
		// Nr. bai etc. will be moved in the layout.
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
		
		if(rentViewHolder.rentStatus != null) {
			if(rent.rentStatus == RentStatus.NOT_AVAILABLE.getStatus()) {
				rentViewHolder.rentStatus.setText(unavailableStatus);
				rentViewHolder.rentStatus.setTextColor(unavailableColor);
			}
		}
	}
}