package com.personal.rents.task.listener;

import android.content.Context;
import android.widget.Toast;

import com.personal.rents.model.Rent;

public abstract class OnAddRentTaskFinishListener {

	private static final String OK = "Chiria a fost adaugata cu success.";
	
	private static final String NETWORK_DOWN_ERROR = "Conectarea la serverele noastre a esuat."
			+ " Asteptati cateva momente si incercati din nou.";
	
	private static final String NETWORK_UNREACHABLE_ERROR = "Conectarea la internet a esuat."
			+ " Activati internetul si incercati din nou.";
	
	private static final String UNKNOWN_ERROR = "Chiria nu a putut fi adaugata. Va rog incercati"
			+ " din nou.";

	public abstract void onTaskFinish(Rent rent);

	protected void handleResponse(Rent rent, Context context) {

		if(rent == null) {
			Toast.makeText(context, UNKNOWN_ERROR, Toast.LENGTH_LONG).show();
			
			return;
		}
		
		switch (rent.rentUploadStatus) {
			case 1:
				Toast.makeText(context, NETWORK_DOWN_ERROR, Toast.LENGTH_LONG).show();

				break;
			case 2:
				Toast.makeText(context, NETWORK_UNREACHABLE_ERROR, Toast.LENGTH_LONG).show();

				break;
			case 3:
				Toast.makeText(context, UNKNOWN_ERROR, Toast.LENGTH_LONG).show();

				break;
			default:
				Toast.makeText(context, OK + rent.rentId, Toast.LENGTH_LONG).show();
				
				break;
		}
	}
}
