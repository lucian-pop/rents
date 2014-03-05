package com.personal.rents.activity;

import com.personal.rents.R;
import com.personal.rents.fragment.ProgressBarFragment;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.task.ChangePasswordAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.webservice.response.ResponseStatusReason;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class EditAccountActivity extends AccountActivity {
	
	private String fromActivity;
	
	private boolean taskInProgress;
	
	private ProgressBarFragment progressBarFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_user_account_activity_layout);

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
		fromActivity = bundle.getString(ActivitiesContract.FROM_ACTIVITY);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(taskInProgress) {
			progressBarFragment.setOnTaskFinishListener(new OnChangePasswordTaskFinishListener());
		}
		
		showAccountDetails();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		taskInProgress = progressBarFragment.getVisibility() == View.VISIBLE;
		outState.putBoolean(ActivitiesContract.TASK_IN_PROGRESS, taskInProgress);
		outState.putString(ActivitiesContract.FROM_ACTIVITY, fromActivity);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		progressBarFragment.resetTaskFinishListener();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_user_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			navigateUp();
			
			return true;
		}
		
		return false;
	}

	private void init() {
		setupActionBar();
		setupProgressBarFragment();
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Editare cont");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	private void setupProgressBarFragment() {
		if(progressBarFragment == null) {
			progressBarFragment = (ProgressBarFragment) getSupportFragmentManager()
					.findFragmentById(R.id.progressBarFragment);
		}
	}
	
	private void navigateUp() {
		Intent intent;
		if(fromActivity.equals(UserAddedRentsActivity.class.getSimpleName())) {
			intent = new Intent(this, UserAddedRentsActivity.class);
		} else {
			intent = new Intent(this, UserFavoriteRentsActivity.class);
		}

		NavUtils.navigateUpTo(this, intent);
	}
	
	private void showAccountDetails() {
		((EditText) findViewById(R.id.email)).setText(account.accountEmail);
		((EditText) findViewById(R.id.phone)).setText(account.accountPhone);
	}

	public void onUndoBtnClick(View view) {
		navigateUp();
	}
	
	public void onConfirmBtnClick(View view) {
		String email = ((EditText) findViewById(R.id.email)).getText().toString().trim();
		String password = ((EditText) findViewById(R.id.password)).getText().toString();
		String newPassword = ((EditText) findViewById(R.id.new_password)).getText().toString();
		String repeatedPassword = ((EditText) findViewById(R.id.repeated_password))
				.getText().toString();
		if(!newPassword.equals(repeatedPassword)) {
			Toast.makeText(this, "Parolele nu coincid.", Toast.LENGTH_LONG).show();

			return;
		}

		startChangePasswordAsyncTask(email, password, newPassword);
	}
	
	private void startChangePasswordAsyncTask(String email, String password, String newPassword) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		ChangePasswordAsyncTask changePasswordTask = new ChangePasswordAsyncTask();
		progressBarFragment.setTask(changePasswordTask);
		progressBarFragment.setOnTaskFinishListener(new OnChangePasswordTaskFinishListener());
		changePasswordTask.execute(email, password, newPassword, this.getApplicationContext());
	}
	
	private class OnChangePasswordTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String SUCCESS_MSG = "Parola a fost schimbata cu success.";
		
		private static final String FAILED_MSG = "Parola nu a putut fi schimbata." +
				" Va rugam incercati din nou.";
		
		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			taskInProgress = false;
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, EditAccountActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(EditAccountActivity.this, FAILED_MSG, Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			Toast.makeText(EditAccountActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT).show();
		}
	}
}
