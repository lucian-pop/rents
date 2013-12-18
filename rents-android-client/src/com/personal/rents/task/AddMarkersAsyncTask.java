package com.personal.rents.task;

import java.util.List;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.personal.rents.util.RentsGenerator;

public class AddMarkersAsyncTask extends AsyncTask<VisibleRegion, Void, List<LatLng>> {

	@Override
	protected List<LatLng> doInBackground(VisibleRegion... params) {
		VisibleRegion visibleRegion = params[0];

		List<LatLng> positions = 
				RentsGenerator.generatePositions(visibleRegion);

		return positions;
	}

	@Override
	protected void onPostExecute(List<LatLng> result) {		
//		// Launch task for adding rents to database.
//		final ProgressDialog progressDialog = new ProgressDialog(RentsMapActivity.this);
//		progressDialog.setIndeterminate(true);
//		progressDialog.setCancelable(true);
//		progressDialog.show();
//		progressDialog.setContentView(R.layout.progress_bar_layout);
//		AddRentsAsyncTask addRentsTask = new AddRentsAsyncTask(result,
//				new OnAddRentsTaskFinishListener() {
//			@Override
//			public void onTaskFinish(Integer result) {
//				progressDialog.dismiss();
//				Toast.makeText(RentsMapActivity.this, "Rents successfully added to database: " 
//						+ result, Toast.LENGTH_LONG).show();	
//			}
//		});
//		addRentsTask.execute(RentsMapActivity.this.getApplicationContext());
	}
}