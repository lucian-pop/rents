package com.personal.rents.activity;

import java.util.Date;

import com.personal.rents.R;
import com.personal.rents.model.Account;
import com.personal.rents.task.SignupAsyncTask;
import com.personal.rents.task.listener.OnSignupTaskFinishListener;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends ActionBarActivity {
	
	private EditText email;
	
	private EditText password;
	
	private EditText repeatPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_activity_layout);
		
		init();
	}

	private void init() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Creare cont");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		repeatPassword = (EditText) findViewById(R.id.repeat_password);
	}
	
	public void onSignupBtnClick(View view) {
		Account account = new Account();
		account.setEmail(email.getText().toString());
		account.setPassword(password.getText().toString());
		account.setAccountType((byte) 0);
		account.setSignupDate(new Date());
		
		SignupAsyncTask signupTask = new SignupAsyncTask(new OnSignupTaskFinishListener() {
			@Override
			public void onTaskFinish(Account account) {
				Toast.makeText(SignupActivity.this, "My token keu is: "
						+ account.getTokenKey(), Toast.LENGTH_LONG).show();
			}
		});
		signupTask.execute(account, this.getApplicationContext());
	}
}
