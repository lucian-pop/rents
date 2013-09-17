package com.personal.rents.activities;

import com.personal.rents.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RentDetailsActivity extends ActionBarActivity {
	
	private static final int MAX_NO_LINES = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent_details_activity_layout);
		
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rent_details_menu, menu);

		return true;
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		final TextView phoneNumberTextView = (TextView)findViewById(R.id.rent_call_btn);
		phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneNumberUri = "tel:" + phoneNumberTextView.getText().toString().trim();
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse(phoneNumberUri));
				startActivity(intent);
			}
		});
		
		final ImageButton moreDescBtn = (ImageButton) findViewById(R.id.more_desc);
		final ImageButton lessDescBtn = (ImageButton) findViewById(R.id.less_desc);
		final TextView descriptionTextView = (TextView) findViewById(R.id.rent_desc);
		moreDescBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				descriptionTextView.setMaxLines(Integer.MAX_VALUE);
				descriptionTextView.setEllipsize(null);
				moreDescBtn.setVisibility(View.INVISIBLE);
				lessDescBtn.setVisibility(View.VISIBLE);
			}
		});
		
		lessDescBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				descriptionTextView.setMaxLines(MAX_NO_LINES);
				moreDescBtn.setVisibility(View.VISIBLE);
				lessDescBtn.setVisibility(View.INVISIBLE);
			}
		});
	}
}
