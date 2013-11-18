package com.personal.rents.activities;

import com.personal.rents.R;
import com.personal.rents.model.User;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class EditUserAccountActivity extends ActionBarActivity {

	private User user;
	
	private EditText userEmail;
	
	private EditText userPhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_user_account_activity_layout);
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_user_menu, menu);

		return true;
	}

	public void onUndoBtnClick(View view) {
		userEmail.setText(user.getUserEmail());
		userPhone.setText(user.getUserPhone());
	}
	
	public void onConfirmBtnClick(View view) {
		// set user properties to the text fields values.
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Editare cont");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		user = new User();
		userEmail = (EditText) findViewById(R.id.user_email);
		userPhone = (EditText) findViewById(R.id.user_phone);
		userEmail.setText(user.getUserEmail());
		userPhone.setText(user.getUserPhone());
	}
}
