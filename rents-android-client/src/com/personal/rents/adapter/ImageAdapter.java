package com.personal.rents.adapter;

import java.util.List;

import com.android.volley.toolbox.NetworkImageView;
import com.personal.rents.R;
import com.personal.rents.model.RentImage;
import com.personal.rents.rest.client.RentsImageClient;
import com.personal.rents.task.LoadLocalImageTask;
import com.personal.rents.util.NetworkUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ImageAdapter extends BaseAdapter {
	
	private int imageLayoutId;
	
	private int defaultSize;
	
	private List<RentImage> images;
	
	private LayoutInflater inflater;
	
	private int imageDestSize;
	
	private Bitmap imagePlaceholder;
	
	private static class ImageViewHolder {
		NetworkImageView imageView;
	}
	
	public ImageAdapter(Context context, int imageLayoutId, int defaultSize, List<RentImage> images,
			int imageDestSize) {
		super();
		this.imageLayoutId = imageLayoutId;
		this.defaultSize = defaultSize;
		this.images = images;
		this.imageDestSize = imageDestSize;
		
		inflater = LayoutInflater.from(context);
		imagePlaceholder =  BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.app_add_picture_placeholder);
	}

	@Override
	public int getCount() {
		return defaultSize;
	}

	@Override
	public Object getItem(int position) {
		return images.size() > position ? images.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageViewHolder imageViewHolder;
		if(convertView == null) {
			convertView = inflater.inflate(imageLayoutId, null);
			imageViewHolder = new ImageViewHolder();
			imageViewHolder.imageView = (NetworkImageView) convertView.findViewById(R.id.picture);
			convertView.setTag(imageViewHolder);
		} else {
			imageViewHolder = (ImageViewHolder) convertView.getTag();
		}

		imageViewHolder.imageView.setImageBitmap(imagePlaceholder);
		if(images == null || images.size() <= position) {
			return convertView;
		}

		String imageURI = images.get(position).rentImageURI;
		if(imageURI != null) {
			setupImageViewBitmap(imageViewHolder, imageURI, parent);
		}

        return convertView;
	}
	
	private void setupImageViewBitmap(ImageViewHolder imageViewHolder, String imageURI, 
			ViewGroup parent) {
		if(NetworkUtil.isValidURI(imageURI)) {
	        imageViewHolder.imageView.setImageUrl(imageURI, 
	        		RentsImageClient.getImageLoader(parent.getContext().getApplicationContext()));
		} else {
			imageViewHolder.imageView.setImageBitmap(
					new LoadLocalImageTask(imageURI, imageDestSize)
					.execute(parent.getContext()));
		}
	}
}
