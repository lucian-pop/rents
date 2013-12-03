package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.logic.UserAccountManager;
import com.personal.rents.model.Account;
import com.personal.rents.task.ChangePasswordAsyncTask;
import com.personal.rents.task.listener.OnChangePasswordTaskFinishListener;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class EditUserAccountActivity extends ActionBarActivity {
	
	private EditText email;
	
	private EditText phone;
	
	private EditText password;
	
	private EditText newPassword;
	
	private Account account;
	
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
	}
	
	public void onConfirmBtnClick(View view) {
		// set user properties to the text fields values.
		ChangePasswordAsyncTask changePasswordTask = new ChangePasswordAsyncTask(
				new OnChangePasswordTaskFinishListener() {
			@Override
			public void onTaskFinish(String tokenKey) {
				Toast.makeText(EditUserAccountActivity.this, "New token key is: " + tokenKey,
						Toast.LENGTH_LONG).show();
			}
		});
		
		changePasswordTask.execute(account.getEmail(), password.getText().toString(), 
				newPassword.getText().toString(), this.getApplicationContext());
	}

	private void init() {
		setupActionBar();
		
		account = UserAccountManager.getAccount(this);
		
		email = (EditText) findViewById(R.id.user_email);
		phone = (EditText) findViewById(R.id.user_phone);
		password = (EditText) findViewById(R.id.password);
		newPassword = (EditText) findViewById(R.id.newPassword);
		
		email.setText(account.getEmail());
		phone.setText(account.getPhone());
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Editare cont");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
