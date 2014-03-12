package com.personal.rents.activity;

import com.personal.rents.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UpgradeAppActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upgrade_app_activity_layout);
		Button upgradeAppBtn = (Button) findViewById(R.id.upgrade_app_btn);
		final String appName = getResources().getString(R.string.app_name);
		upgradeAppBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("market://details?id=" + appName));
				startActivity(intent);
			}
		});
	}
}
