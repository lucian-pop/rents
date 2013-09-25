package com.personal.rents.activities.components;

import java.util.ArrayList;
import java.util.List;

import com.personal.rents.R;
import com.personal.rents.rest.clients.PlacesRESTClient;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class PlacesSuggestionsAdapter extends ArrayAdapter<String> implements Filterable {
	
	private List<String> placesNames = new ArrayList<String>();
	
	private List<String> placesRefs = new ArrayList<String>(); 
	
	private double latitude;
	
	private double longitude;
	
	private int layoutId;

	private LayoutInflater inflater;
	
	private final String lastLocation;
	
	private final String currentLocation;
	
	static class PlaceViewHolder {
		TextView placeName;
		TextView placeAddress;
	}

	public PlacesSuggestionsAdapter(Context context, int layoutId,
			double latitude, double longitude) {
		super(context, layoutId);
		this.latitude = latitude;
		this.longitude = longitude;
		this.layoutId = layoutId;
		lastLocation =  context.getString(R.string.last_location);
		currentLocation = context.getString(R.string.current_location);
		placesNames.add(lastLocation);
		placesNames.add(currentLocation);
		
		inflater = ((Activity)context).getLayoutInflater();
	}

	public List<String> getPlacesRefs() {
		return placesRefs;
	}
	
	@Override
	public int getCount() {
		return placesNames.size();
	}

	@Override
	public String getItem(int position) {
		return placesNames.get(position);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                	placesNames.clear();
                	placesRefs.clear();
                	placesNames.add(lastLocation);
                	placesNames.add(currentLocation);
                    PlacesRESTClient
                    	.getPlacesSuggestions(constraint.toString(), latitude, longitude, 
                    				placesNames, placesRefs);
                    filterResults.values = placesNames;
                    filterResults.count = placesNames.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
		};
		
        return filter;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PlaceViewHolder placeViewHolder;
		if(convertView == null) {
			convertView = inflater.inflate(layoutId, parent, false);
			placeViewHolder = new PlaceViewHolder();
			placeViewHolder.placeName = (TextView) convertView.findViewById(R.id.place_name);
			placeViewHolder.placeAddress = (TextView) convertView.findViewById(R.id.place_address);
			
			convertView.setTag(placeViewHolder);
		} else {
			placeViewHolder = (PlaceViewHolder) convertView.getTag();
		}
		String place = placesNames.get(position);
		int addressIndex = place.indexOf(44);
		if(addressIndex < 0) {
			placeViewHolder.placeName.setText(place);
			placeViewHolder.placeAddress.setVisibility(View.GONE);
		} else {
			if(placeViewHolder.placeAddress.getVisibility()==View.GONE) {
				placeViewHolder.placeAddress.setVisibility(View.VISIBLE);
			}

			placeViewHolder.placeName.setText(place.substring(0, addressIndex));
			placeViewHolder.placeAddress.setText(place.substring(addressIndex + 2, place.length()));
		}

		return convertView;
	}
}
