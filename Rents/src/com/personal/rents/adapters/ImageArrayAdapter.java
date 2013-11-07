package com.personal.rents.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;

import com.personal.rents.R;

public class ImageArrayAdapter extends ArrayAdapter<Bitmap> implements ListAdapter {
	
	private ArrayList<Bitmap> images;
	
	private int initSize;
	
	private int imgLayoutId;
	
	private LayoutInflater inflater;
	
	private Bitmap imagePlaceHolder;
	
	private final int imageMaxSize;
	
	private static class ImageViewHolder {
		ImageView imgView;
	}

	public ImageArrayAdapter(Context context, int imgLayoutId, ArrayList<Bitmap> imagesList, int initSize) {
		super(context, imgLayoutId);
		this.initSize = initSize;
		this.imgLayoutId = imgLayoutId;

		if(imagesList == null) {
			images = new ArrayList<Bitmap>(initSize);
			for(int i = 0; i < initSize; i++) {
				images.add(null);
			}
		} else {
			images = imagesList;
		}

		imagePlaceHolder = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.app_add_picture_placeholder);

		inflater = ((Activity)context).getLayoutInflater();
		
		float imageWidth = context.getResources().getDimension(R.dimen.add_picture_width);
		float imageHeight = context.getResources().getDimension(R.dimen.add_picture_height);
		imageMaxSize = (int) Math.max(imageWidth, imageHeight);
	}

	@Override
	public int getCount() {
		return initSize;
	}

	@Override
	public Bitmap getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageViewHolder imgViewHolder;
		if(convertView == null) {
			convertView = inflater.inflate(imgLayoutId, null);
			imgViewHolder = new ImageViewHolder();
			imgViewHolder.imgView = (ImageView) convertView.findViewById(R.id.picture);
			convertView.setTag(imgViewHolder);
		} else {
			imgViewHolder = (ImageViewHolder) convertView.getTag();
		}
		
		if(images.get(position) != null) {
			imgViewHolder.imgView.setImageBitmap(images.get(position));
		} else {
			imgViewHolder.imgView.setImageBitmap(imagePlaceHolder);
		}

		return convertView;
	}
	
	public ArrayList<Bitmap> getImages() {
		return images;
	}
	
	public int getImageWidth() {
		return images.get(0).getWidth();
	}
	
	public int getImageMaxSize() {
		return imageMaxSize;
	}
	
	public void replaceImage(Bitmap image, int position) {
		Bitmap currImage = images.get(position);
		images.remove(position);
		images.add(position, image);
		if(currImage != null) {
			currImage.recycle();
			currImage = null;
		}

		notifyDataSetChanged();
	}
	
	public void removeImage(int position) {
		Bitmap currImage = images.get(position);
		currImage.recycle();
		currImage = null;

		notifyDataSetChanged();
	}
}	
