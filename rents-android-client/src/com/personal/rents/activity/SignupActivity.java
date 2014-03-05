package com.personal.rents.activity;

import java.util.Date;

import com.personal.rents.R;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.model.Account;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.task.SignupAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.webservice.response.ResponseStatusReason;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends BaseActivity {
	
	private boolean taskInProgress;
	
	private ProgressBarFragment progressBarFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_activity_layout);
		
		Bundle bundle;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}
		
		restoreInstanceState(bundle);
		
		init();
	}
	
	private void restoreInstanceState(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		
		taskInProgress = bundle.getBoolean(ActivitiesContract.TASK_IN_PROGRESS, false);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if(taskInProgress) {
			progressBarFragment.setOnTaskFinishListener(new OnSignupTaskFinishListener());
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		taskInProgress = progressBarFragment.getVisibility() == View.VISIBLE;
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, taskInProgress);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		progressBarFragment.resetTaskFinishListener();
	}

	private void init() {
		setupActionBar();
		setupProgressBarFragment();
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Creare cont");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	private void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	public void onSignupBtnClick(View view) {
		Account account = createAccount();
		String repeatedPassword = ((EditText) findViewById(R.id.repeat_password))
				.getText().toString();
		if(!passwordsMatch(account, repeatedPassword)) {
			Toast.makeText(this, "Parolele nu coincid.", Toast.LENGTH_LONG).show();
			
			return;
		}

		startSignupTask(account);
	}
	
	private void startSignupTask(Account account) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		SignupAsyncTask signupTask = new SignupAsyncTask();
		progressBarFragment.setTask(signupTask);
		progressBarFragment.setOnTaskFinishListener(new OnSignupTaskFinishListener());
		signupTask.execute(account, this.getApplicationContext());
	}
	
	private Account createAccount() {
		Account account = new Account();
		account.accountEmail = ((EditText) findViewById(R.id.email)).getText().toString().trim();
		account.accountPassword = ((EditText) findViewById(R.id.password)).getText().toString()
				.trim();
		account.accountPhone = ((EditText) findViewById(R.id.phonenumber)).getText().toString()
				.trim();
		account.accountType = (byte) 0;
		account.accountSignupDate = new Date();
		
		return account;
	}
	
	private boolean passwordsMatch(Account account, String repeatedPassword) {
		if(account.accountPassword.equals(repeatedPassword)) {
			return true;
		}
		
		return false;
	}
	
	private class OnSignupTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String SUCCESS_MSG = "Contul a fost creat cu success";
		
		private static final String FAILED_MSG = "Contul nu a putut fi creat." +
				" Va rugam incercati din nou.";

		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			taskInProgress = false;
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, SignupActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(SignupActivity.this, FAILED_MSG, Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			Toast.makeText(SignupActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(SignupActivity.this, RentsMapActivity.class);
			startActivity(intent);
		}
	}
}
