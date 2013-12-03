package com.personal.rents.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContextMenuFragment extends DialogFragment {
	
	private static final String LOG_TAG = DialogFragment.class.getSimpleName();

	private int listLayoutId;
	
	private ContextMenuItemClickListener contextMenuItemClickListener;
	
	public interface ContextMenuItemClickListener {
		
		public void onContextMenuItemClickListener();
	}
	
	public ContextMenuFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
