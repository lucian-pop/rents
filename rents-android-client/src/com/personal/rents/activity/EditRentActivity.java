package com.personal.rents.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.personal.rents.R;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;
import com.personal.rents.rest.util.NetworkErrorHandler;
import com.personal.rents.task.AddRentImageAsyncTask;
import com.personal.rents.task.DeleteRentImageAsyncTask;
import com.personal.rents.task.GetRentAsyncTask;
import com.personal.rents.task.ReplaceImageAsyncTask;
import com.personal.rents.task.UpdateRentAsyncTask;
import com.personal.rents.task.listener.OnNetworkTaskFinishListener;
import com.personal.rents.util.ActivitiesContract;
import com.personal.rents.webservice.response.ResponseStatusReason;

public class EditRentActivity extends ManageRentActivity {

	private int rentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_rent_activity_layout);
		
		Bundle bundle = null;
		if(savedInstanceState != null) {
			bundle = savedInstanceState;
		} else {
			bundle = getIntent().getExtras();
		}

		restoreInstanceState(bundle);

		init();
	}

	@Override
	protected void restoreInstanceState(Bundle bundle) {
		super.restoreInstanceState(bundle);
		
		if(bundle == null) {
			return;
		}
		
		rentId = bundle.getInt(ActivitiesContract.RENT_ID);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if(taskInProgress) {
			if(taskName.equals(ReplaceImageAsyncTask.class.getSimpleName())) {
				progressBarFragment.setOnTaskFinishListener(
						new OnReplaceRentImageTaskFinishListener());
			} else if(taskName.equals(AddRentImageAsyncTask.class.getSimpleName())) {
				progressBarFragment.setOnTaskFinishListener(new OnAddRentImageTaskFinishListener());
			} else if(taskName.equals(DeleteRentImageAsyncTask.class.getSimpleName())){
				progressBarFragment.setOnTaskFinishListener(
						new OnDeleteRentImageTaskFinishListener());
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(rent.rentId == 0) {
			startGetRentAsyncTask();
		} else {
			showRentDetails();
		}
	}

	@Override
	protected void saveInstanceState(Bundle outState) {
		updateAddressValues();
		updateRentValues();
		
		super.saveInstanceState(outState);
		
		outState.putInt(ActivitiesContract.RENT_ID, rentId);
	}
	
	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		getSupportActionBar().setTitle("Editare chirie");
	}
	
	private void showRentDetails() {
		setupLocateRentBtn();

		showAddress();
		
		showSpecifications();
		
		showOtherDetails();
		
		showContact();
		
		showImages();
	}
	
	private void showSpecifications() {
		EditText rentPrice = (EditText)	findViewById(R.id.rent_price);
		rentPrice.setText(Integer.toString(rent.rentPrice));
		
		EditText rentSurface = (EditText) findViewById(R.id.rent_surface);
		rentSurface.setText(Integer.toString(rent.rentSurface));
		
		EditText rentRooms = (EditText) findViewById(R.id.rent_rooms);
		rentRooms.setText(Short.toString(rent.rentRooms));
		
		EditText rentBaths = (EditText) findViewById(R.id.rent_baths);
		rentBaths.setText(Short.toString(rent.rentBaths));
		
		Spinner partiesSpinner = (Spinner) findViewById(R.id.rent_party);
		partiesSpinner.setSelection(rent.rentParty + 1);

		Spinner typesSpinner = (Spinner) findViewById(R.id.rent_type);
		typesSpinner.setSelection(rent.rentType + 1);

		Spinner structSpinner = (Spinner) findViewById(R.id.rent_structure);
		structSpinner.setSelection(rent.rentArchitecture + 1);

		Spinner ageSpinner = (Spinner) findViewById(R.id.rent_age);
		ageSpinner.setSelection(rent.rentAge + 1);
	}

	private void showOtherDetails() {
		EditText rentDesc = (EditText) findViewById(R.id.rent_desc);
		rentDesc.setText(rent.rentDescription);
		
		CheckBox  petsAllowed = (CheckBox) findViewById(R.id.pets_allowed);
		petsAllowed.setChecked(rent.rentPetsAllowed);
	}
	
	private void showContact() {
		EditText phone = (EditText) findViewById(R.id.rent_phone);
		phone.setText(rent.rentPhone);
	}

	private void showImages() {
		imagesFragment.setupGridView(rent.rentImages);
	}
	
	@Override
	protected void replaceRentImage() {
		startReplaceRentImageAsyncTask();
	}

	@Override
	protected void addRentImage() {
		startAddRentImageAsyncTask();
	}

	@Override
	protected void deleteRentImage() {
		startDeleteRentImageAsyncTask();
	}
	
	public void onCancelBtnClick(View view) {
		redirectToUserAddedRents();
	}
	
	@Override
	public void onConfirmBtnClick(View view) {
		super.onConfirmBtnClick(view);
		
		startUpdateRentAsyncTask();
	}

	private void startGetRentAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		GetRentAsyncTask getRentTask = new GetRentAsyncTask();
		progressBarFragment.setTask(getRentTask);
		progressBarFragment.setOnTaskFinishListener(new OnGetRentTaskFinishListener());
		getRentTask.execute(rentId);
	}
	
	private void startReplaceRentImageAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		ReplaceImageAsyncTask replaceImageTask = new ReplaceImageAsyncTask();
		progressBarFragment.setTask(replaceImageTask);
		progressBarFragment.setOnTaskFinishListener(new OnReplaceRentImageTaskFinishListener());
		replaceImageTask.execute(selectedImagePath,
				rent.rentImages.get(imagesFragment.getSelectedPicPosition()), account.tokenKey, 
				this.getApplicationContext());
	}
	
	private void startAddRentImageAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		AddRentImageAsyncTask addRentImageTask = new AddRentImageAsyncTask();
		progressBarFragment.setTask(addRentImageTask);
		progressBarFragment.setOnTaskFinishListener(new OnAddRentImageTaskFinishListener());
		addRentImageTask.execute(selectedImagePath, rentId, account.tokenKey, 
				this.getApplicationContext());
	}
	
	private void startDeleteRentImageAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		DeleteRentImageAsyncTask deleteRentImageTask = new DeleteRentImageAsyncTask();
		progressBarFragment.setTask(deleteRentImageTask);
		progressBarFragment.setOnTaskFinishListener(new OnDeleteRentImageTaskFinishListener());
		RentImage rentImage= rent.rentImages.get(imagesFragment.getSelectedPicPosition());
		deleteRentImageTask.execute(rentImage.rentImageId, account.tokenKey);
	}
	
	private void startUpdateRentAsyncTask() {
		progressBarFragment.cancelCurrentlyAssociatedTask();
		progressBarFragment.show();
		
		UpdateRentAsyncTask updateRentTask = new UpdateRentAsyncTask();
		progressBarFragment.setTask(updateRentTask);
		progressBarFragment.setOnTaskFinishListener(new OnUpdateRentTaskFinishListener());
		updateRentTask.execute(rent, account.tokenKey);
	}
	
	private class OnGetRentTaskFinishListener implements OnNetworkTaskFinishListener {
		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, EditRentActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(EditRentActivity.this, "Chiria nu a putut fi incarcata.", 
						Toast.LENGTH_LONG).show();
				
				return;
			}
			
			rent = (Rent) result;
			showRentDetails();
		}
	}
	
	private class OnReplaceRentImageTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String FAILED_MSG = "Imaginea nu a putut fii schimbata." +
				" Va rugam incercati din nou.";

		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, EditRentActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(EditRentActivity.this, FAILED_MSG, Toast.LENGTH_LONG).show();
				
				return;
			}
			
			Toast.makeText(EditRentActivity.this, "Imaginea a fost schimbate cu success", 
					Toast.LENGTH_SHORT).show();

			rent.rentImages.set(imagesFragment.getSelectedPicPosition(), (RentImage) result);
			imagesFragment.setupGridView(rent.rentImages);
		}
	}
	
	private class OnAddRentImageTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String SUCCESS_MSG = "Imaginea a fost adaugata cu success";
		
		private static final String FAILED_MSG = "Imaginea nu a putut fi adaugata." +
				" Va rugam incercati din nou";

		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, EditRentActivity.this);

				return;
			}
			
			if(result == null) {
				Toast.makeText(EditRentActivity.this, FAILED_MSG, Toast.LENGTH_LONG).show();
				
				return;
			}
			
			Toast.makeText(EditRentActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT).show();
			rent.rentImages.add((RentImage) result);
			imagesFragment.setupGridView(rent.rentImages);
		}
	}
	
	private class OnDeleteRentImageTaskFinishListener implements OnNetworkTaskFinishListener {

		private static final String SUCCESS_MSG = "Imaginea a fost stearsa cu success";

		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, EditRentActivity.this);

				return;
			}
			
			Toast.makeText(EditRentActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT).show();

			rent.rentImages.remove(imagesFragment.getSelectedPicPosition());
			imagesFragment.setupGridView(rent.rentImages);
		}
	}
	
	private class OnUpdateRentTaskFinishListener implements OnNetworkTaskFinishListener {
		
		private static final String FAILED_MSG = "Chiria nu a putut fi salvata." +
				" Va rugam incercati din nou.";
		
		private static final String SUCCESS_MSG = "Chiria a fost salvata cu succes.";

		@Override
		public void onTaskFinish(Object result, ResponseStatusReason status) {
			if(!status.equals(ResponseStatusReason.OK)) {
				NetworkErrorHandler.handleRetrofitError(status, result, EditRentActivity.this);

				return;
			}
			
			if((Integer) result != 1) {
				Toast.makeText(EditRentActivity.this, FAILED_MSG, Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			Toast.makeText(EditRentActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT).show();
			redirectToUserAddedRents();
		}
		
	}
}
