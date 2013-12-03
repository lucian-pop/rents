package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.model.Account;
import com.personal.rents.task.LoginAsyncTask;
import com.personal.rents.task.listener.OnLoginTaskFinishListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
	
	private EditText email;
	
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);
		
		init();
	}
	
	private void init() {
		setupActionBar();
		
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Autentificare");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	public void onCreateAccBtnClick(View view) {
		Intent intent = new Intent(this, SignupActivity.class);
		startActivity(intent);
	}
	
	public void onLoginBtnClick(View view) {
		LoginAsyncTask loginTask = new LoginAsyncTask(new OnLoginTaskFinishListener() {
			@Override
			public void onTaskFinish(Account account) {
				Toast.makeText(LoginActivity.this, "My token key is: "
						+ account.getTokenKey(), Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(LoginActivity.this, RentsMapActivity.class);
				startActivity(intent);
			}
		});
		
		loginTask.execute(email.getText().toString(), password.getText().toString(), 
				this.getApplicationContext());
	}
}
