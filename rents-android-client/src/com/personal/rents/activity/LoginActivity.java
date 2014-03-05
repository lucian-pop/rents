package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.task.LoginAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.webservice.response.ResponseStatusReason;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	
	private String email;
	
	private String password;
	
	private boolean taskInProgress;
	
	private ProgressBarFragment progressBarFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);
		
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
			progressBarFragment.setOnTaskFinishListener(new OnLoginTaskFinishListener());
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
		getSupportActionBar().setTitle("Autentificare");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	private void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	public void onCreateAccBtnClick(View view) {
		Intent intent = new Intent(this, SignupActivity.class);
		startActivity(intent);
	}
	
	public void onLoginBtnClick(View view) {
		if(!isEmailPasswordValid()) {
			return;
		}
		
		startLoginTask();
	}
	
	private boolean isEmailPasswordValid() {
		boolean valid = false;
		email = ((EditText) findViewById(R.id.email)).getText().toString().trim();
		password = ((EditText) findViewById(R.id.password)).getText().toString();
		
		if(email.equals("")) {
			Toast.makeText(this, "Introduceti emailul", Toast.LENGTH_LONG).show();
		} else if(password.equals("")) {
			Toast.makeText(this, "Introduceti parola", Toast.LENGTH_LONG).show();
		} else {
			valid = true;
		}
		
		return valid;
	}
	
	private void startLoginTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		LoginAsyncTask loginTask = new LoginAsyncTask();
		progressBarFragment.setTask(loginTask);
		progressBarFragment.setOnTaskFinishListener(new OnLoginTaskFinishListener());
		loginTask.execute(email, password, this.getApplicationContext());
		
	}
	
	private class OnLoginTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String SUCCESS_MSG = "Autentificarea s-a realizat cu success.";
		
		private static final String FAILED_MSG = "Nu am reusit sa va autentificam." +
				" Va rugam incercati din nou.";

		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			taskInProgress = false;
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, LoginActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(LoginActivity.this, FAILED_MSG, Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			Toast.makeText(LoginActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(LoginActivity.this, RentsMapActivity.class);
			startActivity(intent);
		}
	}
}
