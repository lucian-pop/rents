package com.personal.rents.activity;

import java.util.concurrent.atomic.AtomicBoolean;

import com.personal.rents.R;
import com.personal.rents.model.Rent;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.rest.util.RetrofitResponseStatus;
import com.personal.rents.task.AddRentAsyncTask;
import com.personal.rents.task.UploadImagesAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddRentActivity extends ManageRentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rent_activity_layout);
		
		Bundle bundle = null;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
			AddRentAsyncTask.completed = new AtomicBoolean(false);
			UploadImagesAsyncTask.completed = new AtomicBoolean(false);
		}

		restoreInstanceState(bundle);

		init();
	}

	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		getSupportActionBar().setTitle("Adaugare chirie");
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(taskInProgress) {
			if(AddRentAsyncTask.completed.get() 
					&& !UploadImagesAsyncTask.completed.get()
					&& taskName.equals(UploadImagesAsyncTask.class.getSimpleName())) {
				progressBarFragment.setOnTaskFinishListener(new OnUploadImagesTaskFinishListener());
			} else if(AddRentAsyncTask.completed.get() || UploadImagesAsyncTask.completed.get()) {
				redirectToUserAddedRents();
			} else {
				progressBarFragment.setOnTaskFinishListener(new OnAddRentTaskFinishListener());
			}
		}
	}
	
	@Override
	public void onLocateRentBtnClick(View view) {
		Intent intent = new Intent(this, AddLocationActivity.class);
		intent.putExtra(ActivitiesContract.ADDRESS, rent.address);
		intent.putExtra(ActivitiesContract.FROM_ACTIVITY, AddRentActivity.class.getSimpleName());

		startActivityForResult(intent, ActivitiesContract.ADD_LOCATION_REQ_CODE);
	}
	
	@Override
	public void onConfirmBtnClick(View view) {
		super.onConfirmBtnClick(view);

		startAddRentAsyncTask(rent);
	}
	
	private void startAddRentAsyncTask(Rent rent) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		AddRentAsyncTask addRentTask = new AddRentAsyncTask();
		progressBarFragment.setTask(addRentTask);
		progressBarFragment.setOnTaskFinishListener(new OnAddRentTaskFinishListener());
		addRentTask.execute(rent, account.tokenKey);
	}
	
	private void startUploadImagesAsyncTask(Rent rent) {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();

		UploadImagesAsyncTask uploadImagesTask = new UploadImagesAsyncTask();
		progressBarFragment.setTask(uploadImagesTask);
		progressBarFragment.setOnTaskFinishListener(new OnUploadImagesTaskFinishListener());
		uploadImagesTask.execute(rent, account, this.getApplicationContext());
	}
	
	private class OnAddRentTaskFinishListener implements OnNetworkTaskFinishListener {

		private static final String RENT_ADDED = "Chiria a fost adaugata cu success";
		
		private static final String RENT_NOT_ADDED = "Chiria nu a putut fi adaugata. Va rugam"
				+ " incercati din nou.";

		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			taskInProgress = false;
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, AddRentActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText( AddRentActivity.this, RENT_NOT_ADDED, Toast.LENGTH_LONG).show();
				
				return;
			}
			
			Toast.makeText(AddRentActivity.this, RENT_ADDED, Toast.LENGTH_LONG).show();
			startUploadImagesAsyncTask((Rent) result);
		}
	}
	
	private class OnUploadImagesTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String SUCCESS_MSG = "Finished uploading images";

		@Override
		public void onTaskFinish(Object result, RetrofitResponseStatus status) {
			taskInProgress = false;
			if(!status.equals(RetrofitResponseStatus.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, AddRentActivity.this);
				
				Intent intent = new Intent(AddRentActivity.this, EditRentActivity.class);
				intent.putExtra(ActivitiesContract.RENT, (Rent) result);
				startActivity(intent);

				return;
			}
			
			Toast.makeText(AddRentActivity.this, SUCCESS_MSG, Toast.LENGTH_LONG).show();
			
			redirectToUserAddedRents();
		}
	}
}
